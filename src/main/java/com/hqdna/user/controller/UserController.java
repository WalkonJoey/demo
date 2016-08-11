package com.hqdna.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.hqdna.common.baseController.BaseController;
import com.hqdna.common.commonTools.Cache;
import com.hqdna.common.commonTools.MD5;
import com.hqdna.common.page.Combobox;
import com.hqdna.common.page.Json;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.common.page.TreeNode;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.Perm;
import com.hqdna.permission.vo.PermType;
import com.hqdna.user.dao.IUserDao;
import com.hqdna.user.service.IRoleService;
import com.hqdna.user.service.IUserService;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.DepartmentVo;
import com.hqdna.user.vo.RoleVo;
import com.hqdna.user.vo.UserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/user")
@SessionAttributes(IUserConstants.CURRENT_USER)
public class UserController extends BaseController {
	@Resource(name = "userService")
	private IUserService userService;
	@Resource(name = "userDao")
	private IUserDao userDao;
	@Resource(name = "roleService")
	private IRoleService roleService;

	/**
	 * 返回userManagement的页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "userManagePage")
	public String forward() {
		return "userMgr/userManage";
	}

	/**
	 * 获取所有用户的列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getUsersForDatagrid")
	//@Perm(module = Module.user, type = PermType.View)
	public ModelAndView getAllUsersViaListPg(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser)
			throws IOException {

		QueryResult<UserInfo> users = userService
				.getUserList(currentUser, null);
		ModelAndView mav = new ModelAndView();
		mav.addObject("total", users.getTotalrecord());
		mav.addObject("rows", users.getResultlist());
		return mav;
	}

	/**
	 * 分页获取用户列表
	 * 
	 * @param datagrid
	 * @param userInfo
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUsersForDatagridPagination")
	@Perm(module = Module.user, type = PermType.View)
	@ResponseBody
	public ModelAndView getPageUsersViaListPg(PageInfo pgInfo,
			HttpServletRequest request, HttpSession session) throws IOException {
		// 过滤SQL
		Map<String, Object> whereSqlMap = getWhereSqlMap(request, pgInfo);
		// @ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
		// 这里如果使用这种方式，loginID
		// departmentID等字段在UserInfo中也有，就会修改currentUser的内容，导致出错
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		// 添加查询条件
		String loginID = request.getParameter("loginID");
		String userCnName = request.getParameter("userCnName");
		String isEnable = request.getParameter("isEnable");
		String departJob = request.getParameter("departJob");
		String departName = request.getParameter("departmentID");

		if (loginID != null && !loginID.trim().equals("")) {
			whereSqlMap.put("  lower(o.loginID) like '%"
					+ loginID.trim().toLowerCase() + "%' ", null);
		}

		if (userCnName != null && !userCnName.trim().equals("")) {
			whereSqlMap.put(" lower(o.userCnName) like '%"
					+ userCnName.trim().toLowerCase() + "%' ", null);
		}

		if (isEnable != null && !isEnable.trim().equals("")) {
			byte isEnableT = Byte.valueOf(isEnable);
			if (isEnableT >= 0) {
				whereSqlMap.put(" o.isEnable =? ", isEnableT);
			}
		}
		if (departJob != null && !departJob.trim().equals("")) {

			whereSqlMap.put(" lower(o.departJob) like '%"
					+ departJob.trim().toLowerCase() + "%'", null);
		}
		if (departName != null && !departName.trim().equals("")) {
			int departmentID = Integer.parseInt(departName);
			whereSqlMap.put(" o.department.departID=? ", departmentID);
		}

		if (currentUser.isAdministrator()) {

		} else if (currentUser.isBizAdministrator()) {//如果是业务管理员，那么不显示超级管理员的信息
			whereSqlMap.put(" o.type!=? ", (byte) 127);
		} else {
			whereSqlMap.put(" o.loginID=? ", currentUser.getLoginID());
		}

		QueryResult<UserInfo> users = userService.getUserList(currentUser,
				pgInfo, whereSqlMap, getOrderMap(pgInfo));

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", users.getTotalrecord());
		attributes.put("rows", users.getResultlist());
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
	}

	/**
	 * 新增用户，返回创建成功用户的原始信息
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ResponseBody
	// 使不经过@RequestMapping过滤到对应页面，返回原始数据
	public Json addUserViaAddPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, UserInfo userInfo) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			userService.createUser(currentUser, userInfo);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 编辑时更新用户信息
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	// 使不经过@RequestMapping过滤到对应页面，返回原始数据
	public Json updateUserViaUpdatePg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, UserInfo userInfo) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		String encryptPassword = MD5.MD5Encode(userInfo.getLoginID()
				+ userInfo.getPassword() + IUserConstants.VERIFY_CODE);
		userInfo.setPassword(encryptPassword);
		Json json = new Json();
		try {
			userService.updateUser(currentUser, userInfo);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/deleteUser")
	@ResponseBody
	public Json deleteUserViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String userIDs) {
		Json json = new Json();
		if (userIDs == null || userIDs.length() == 0) {
			json.setSuccess(false);
			return json;
		}
		try {
			String[] userIDArray = userIDs.split(",");
			boolean isOk = userService.deleteUser(userIDArray);
			json.setSuccess(isOk);
		} catch (Exception e) {
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 以json树的形式返回所有的用户角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getRolesTree")
	// 加上后面的一句返回的数据不会出现乱码
	@ResponseBody
	@Perm(module = Module.user, type = PermType.View)
	public String getRolesTreeViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		String targetUserID = request.getParameter("userID");
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		List<RoleVo> userRoles = roleService.getOwnRole4User(currentUser,
				targetUserID);
		JSONArray tree = new JSONArray();
		// 只查询启用的角色，所以这里加上角色状态的过滤SQL
		Map<String, Object> whereSqlMap = new HashMap<String, Object>();
		whereSqlMap.put(" isEnable=? ", (byte) 1);
		QueryResult<RoleVo> roleVoList = roleService.getRoleList(currentUser,
				null, whereSqlMap);
		for (RoleVo vo : roleVoList.getResultlist()) {
			TreeNode t = new TreeNode();
			t = transVoToTree(vo, userRoles);
			tree.add(t);
		}
		return tree.toString();
	}

	private TreeNode transVoToTree(RoleVo vo, List<RoleVo> userRoles) {
		TreeNode node = new TreeNode();
		node.setId(vo.getRoleID());
		node.setText(vo.getRoleName());
		if (hasThisRole(vo, userRoles)) {
			node.setChecked(true);
		}
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("isEnable", vo.getIsEnable());
		node.setAttributes(attributes);
		node.setIconCls("");
		return node;
	}

	private boolean hasThisRole(RoleVo vo, List<RoleVo> userRoles) {
		for (RoleVo roleVo : userRoles) {
			if (roleVo == vo || roleVo.getRoleID() == vo.getRoleID()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 新增用户，返回创建成功用户的原始信息
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdateUserPg22")
	// 使不经过@RequestMapping过滤到对应页面，返回原始数据
	public ModelAndView addOrUpdateUserViaListPg22(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String userID = request.getParameter("userID");
		ModelAndView mav = new ModelAndView();
		if (userID != null && !userID.equals("")) {
			// List<UserInfo> userInfos = new ArrayList<UserInfo>();
			UserInfo userInfo = userService.queryUserByID(userID);
			// userInfos.add(userInfo);
			model.addAttribute("daaa", userInfo);
			mav = new ModelAndView("userMgr/addOrUpdateUser", "userInfo",
					userInfo);

		} else {
			mav = new ModelAndView("userMgr/addOrUpdateUser");
		}
		return mav;
	}

	@RequestMapping(value = "/addOrUpdateUserPg")
	// 使不经过@RequestMapping过滤到对应页面，返回原始数据
	public String addOrUpdateUserViaListPg(String userID, Model model) {
		if (userID == null || "".equals(userID)) {
			return "userMgr/addOrUpdateUser";
		}

		UserInfo userInfo = userService.queryUserByID(userID);
		model.addAttribute("userInfo", userInfo);

		return "userMgr/addOrUpdateUser";
	}

	/**
	 * 返回userManagement的页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "userStaManagePage")
	public String forward2() {
		return "userMgr/userStaManage";
	}

	/**
	 * 获取所有在线用户的列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getOnlinUsers")
	@ResponseBody
	public Map<String, Object> getOnlineUsersViaUserStaPg(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<UserInfo> onlineUsers = cm.<UserInfo> getCacheByName(
				IUserConstants.ONLINE_USERS).getList();

		String loginID = request.getParameter("loginID");
		String userCnName = request.getParameter("userCnName");

		List<UserInfo> onUsers = new ArrayList<UserInfo>();
		if (onlineUsers != null && !onlineUsers.isEmpty()) {
			{
				for (UserInfo user : onlineUsers) {
					if (user.getLoginID() != null
							&& user.getLoginID().trim().length() > 0) {
						if (user.getLoginID().equalsIgnoreCase(loginID)) {
							onUsers.add(user);
						}
					} else if (user.getUserCnName() != null
							&& user.getUserCnName().trim().length() > 0) {
						if (user.getUserCnName().equalsIgnoreCase(userCnName)) {
							onUsers.add(user);
						}
					} else {
						onUsers.add(user);
					}
				}
			}
		}
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", onlineUsers.size());
		attributes.put("rows", onlineUsers);
		return attributes;
	}
	@RequestMapping(value = "/deleteOnlineUser")
	@ResponseBody
	public Json deleteOnlineUserViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String userID) {
		Json json = new Json();
		Cache<UserInfo> cache = getOnlineUserCache();
		if(cache.containsKey(userID)){
			try {
				cache.removeFromCache(userID);
			} catch (Exception e) {
				e.printStackTrace();
				json.setMsg(e.getMessage());
				json.setSuccess(false);
			}
		}
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 获取所有部门列表 并且选取用户所在部门项
	 * 
	 * @param userID
	 *            指定用户ID
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getDepartmentList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDepartmentListViaAddUserPg(String userID,
			HttpSession session) {
		List<DepartmentVo> departmentList = userService.getDepartmentList(null);
		UserInfo targetUser = null;
		if (userID != null && !"".equals(userID)) {
			targetUser = userService.queryUserByID(userID);
		}
		JSONArray jsonArray = new JSONArray();
		if (targetUser == null) {
			for (DepartmentVo departmentVo : departmentList) {
				Combobox tNode = new Combobox();
				tNode.setId(String.valueOf(departmentVo.getDepartID()));
				tNode.setText(departmentVo.getDepartName());
				JSONObject json = JSONObject.fromObject(tNode);
				jsonArray.add(json);
			}
		} else {
			for (DepartmentVo departmentVo : departmentList) {
				Combobox tNode = new Combobox();
				tNode.setId(String.valueOf(departmentVo.getDepartID()));
				tNode.setText(departmentVo.getDepartName());
				if (targetUser.getDepartmentID() == departmentVo.getDepartID()) {
					tNode.setSelected(true);
				}
				JSONObject json = JSONObject.fromObject(tNode);
				jsonArray.add(json);
			}
		}
		// String json =
		// "[{\"id\":1,\"text\":\"你好\"},{\"id\":2,\"text\":\"你很好\"},{\"id\":3,\"text\":\"你相当好\",\"selected\":true},{\"id\":4,\"text\":\"text4\"},{\"id\":5,\"text\":\"text5\"}]";
		return jsonArray.toString();
	}

	/**
	 * 获取用户列表，用过弹出对话框选择
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @param pgInfo
	 * @param vendorID
	 * @return
	 */
	@RequestMapping(value = "/getUser4Dialog/{tag}")
	public ModelAndView getUser4Dialog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, PageInfo pgInfo,
			@PathVariable byte tag) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);

		Map<String, Object> whereSqlMap = getWhereSqlMap(pgInfo);
		String name = request.getParameter("param_name");
		String value = request.getParameter("param_value");
		if (name != null && value != null) {
			String sqlName = "o." + name;
			whereSqlMap.put(
					" lower(" + sqlName + ") like '%" + value.toLowerCase()
							+ "%'", null);
		}
		ModelAndView mav = new ModelAndView();
		Map<String, Object> attributes = new HashMap<String, Object>();
		if (tag == 0) {
			// 0的时候表示查询可以设置为跟单的人员
			whereSqlMap.put("o.type=?", (byte) 0);
			QueryResult<UserInfo> users = userService.getUserList(currentUser,
					pgInfo, whereSqlMap);
			attributes.put("total", users.getTotalrecord());
			attributes.put("rows", users.getResultlist());
		}

		mav.addAllObjects(attributes);
		return mav;
	}
	/**
	 * 获取下一个拥有权限的审批人
	 */
	@RequestMapping(value = "/getNextApprover/{moduleName}")
	public ModelAndView getNextApprover(PageInfo pgInfo,
			@PathVariable String moduleName) {
		List<UserInfo> userList = new ArrayList<UserInfo>();

		List<UserInfo> list = userService.getUserListWithPermission(
				Module.valueOf(moduleName), PermType.Approve);
		if (list != null) {
			userList.addAll(list);
		}
		Collections.sort(userList);
		ModelAndView mav = new ModelAndView();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", userList.size());
		attributes.put(
				"rows",
				userList.subList(pgInfo.getStartIndex(), pgInfo.getStartIndex()
						+ Math.min(pgInfo.getRows(), userList.size())));

		mav.addAllObjects(attributes);
		return mav;
	}
}
