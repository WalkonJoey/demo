package com.hqdna.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqdna.common.baseController.BaseController;
import com.hqdna.common.page.Json;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.common.page.TreeNode;
import com.hqdna.permission.service.IPermissionService;
import com.hqdna.permission.util.IPermConstants;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.Perm;
import com.hqdna.permission.vo.PermItem;
import com.hqdna.permission.vo.PermType;
import com.hqdna.permission.vo.RuleVo;
import com.hqdna.user.dao.IRoleDao;
import com.hqdna.user.service.IRoleService;
import com.hqdna.user.vo.RoleVo;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;

/**
 * 
 * @author Joey
 * 
 *         2014-7-25上午9:25:33
 */

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
	@Resource(name = "roleService")
	private IRoleService roleService;
	@Resource(name = "permissionService")
	private IPermissionService permissionService;
	@Resource(name = "roleDao")
	private IRoleDao roleDao;

	/**
	 * 返回userManagement的页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "roleManagePage")
	public String forward() {
		return "roleMgr/roleMgr";
	}

	/**
	 * 返回addOrUpdateRolePerm的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdateRolePermPage")
	public String addOrUpdateRolePermPage(HttpServletRequest request,
			HttpSession session) throws IOException {
		request.setAttribute("roleID4Perm", request.getParameter("roleID"));
		HashMap<String, List<RuleVo>> ruleMap = new HashMap<String, List<RuleVo>>();
		session.setAttribute(IPermConstants.EDIT_RULE, ruleMap);
		return "roleMgr/addOrUpdateRolePerm";
	}

	/**
	 * 返回addRole的页面
	 * 
	 * @return
	 */
/*	@RequestMapping(value = "/addRolePage")
	public String addRolePage(HttpServletRequest request, HttpSession session)
			throws IOException {
		return "roleMgr/addRole";
	}*/

	/**
	 * 返回updateRole的页面
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "/updateRolePage")
	public String updateRolePage(HttpServletRequest request, String roleID,
			HttpSession session) throws IOException {
		request.setAttribute("RoleID4Update", roleID);
		return "roleMgr/updateRole";
	}*/

	/**
	 * 获取所有角色，分页，以json格式传输
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getRolesForDatagrid")
	@Perm(module = Module.role, type = PermType.View)
	public Object getPageRolesViaListPg(HttpServletRequest request,
			PageInfo pgInfo, HttpServletResponse response, HttpSession session)
			throws IOException {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		QueryResult<RoleVo> roles = roleService
				.getRoleList(currentUser, pgInfo);
		if (roles != null) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("total", roles.getTotalrecord());
			attributes.put("rows", roles.getResultlist());
			return attributes;
		}
		return null;
	}

	/**
	 * 以json字符串的形式返回role列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getRolesForJson")
	@ResponseBody
	@Perm(module = Module.role, type = PermType.View)
	public String getAllRolesViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		QueryResult<RoleVo> roles = roleService.getRoleList(currentUser, null);
		if (roles != null) {
			JSONArray ja = JSONArray.fromObject(roles.getResultlist());
			return ja.toString();
		}
		return null;
	}

	/**
	 * 返回值定用户的角色 以json字符串的形式返回role列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getSpecialUserRoles")
	// 加上后面的一句返回的数据不会出现乱码
	@Perm(module = Module.role, type = PermType.View)
	@ResponseBody
	public String getSpecialUserRolesViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String userID)
			throws IOException {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		List<RoleVo> roles = roleService.getOwnRole4User(currentUser, userID);
		if (roles != null) {
			StringBuilder roleNames = new StringBuilder();
			for (RoleVo rolevo : roles) {
				roleNames.append(rolevo.getRoleName());
				roleNames.append(" ");
			}
			return roleNames.toString();
		}
		return null;
	}

	/**
	 * 增加角色，返回创建成功角色的原始信息
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	@Perm(module = Module.role, type = PermType.New)
	@ResponseBody
	// 使不经过@RequestMapping过滤到对应页面，返回原始数据
	public Json addRoleViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, RoleVo roleVo) {
		Json json = new Json();
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		try {
			roleService.addRole(currentUser, roleVo);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
		}
		return json;
	}

	@RequestMapping(value = "/updateRole", method = RequestMethod.POST)
	@Perm(module = Module.role, type = PermType.Edit)
	@ResponseBody
	// 使不经过@RequestMapping过滤到对应页面，返回原始数据
	public Json updateRoleViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, RoleVo roleVo) {
		Json json = new Json();
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		try {
			roleService.updateRole(currentUser, roleVo);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
		}
		return json;
	}

	@RequestMapping(value = "/deleteRole")
	@Perm(module = Module.role, type = PermType.Delete)
	@ResponseBody
	// 使不经过@RequestMapping过滤到对应页面，返回原始数据
	public Json deleteRoleViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String roleIds) {
		Json json = new Json();
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		try {
			roleService.deleteRoleByIds(currentUser, roleIds);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
		}
		return json;
	}

	@RequestMapping(value = "/getRolePermissionTree")
	@ResponseBody
	@Perm(module = Module.role, type = PermType.View)
	public String getRolePermTreeViaAssignPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, RoleVo roleVo)
			throws IOException {
		String roleID = request.getParameter("roleID");
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		// 获取原有的权限
		List<PermItem> permList = permissionService.getAssignedPerm4Role(
				currentUser, roleID);
		Map<String, List<PermItem>> allPermMap = permissionService
				.getAllPerms(currentUser);
		TreeNode tree = transPermVoToTree("0", "0", allPermMap, permList);
		JSONArray ja = new JSONArray();
		ja.add(tree);
		return ja.toString();

	}

	/**
	 * 给用户分配角色
	 */
	@RequestMapping(value = "/updateUserRole")
	@Perm(module = Module.role, type = PermType.Assign)
	@ResponseBody
	public String assignRole4UserViaAssignPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, Model model)
			throws IOException {
		String passCheckedRolesJson = request.getParameter("choosedRoles");
		JSONArray roleArray = JSONArray.fromObject(passCheckedRolesJson);
		Set<String> afterRoles = new HashSet<String>();
		for (int i = 0; i < roleArray.size(); i++) {
			afterRoles.add(roleArray.getJSONObject(i).getString("id"));
		}
		String targetUserID = request.getParameter("userID");
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		// 获取原有的角色
		List<RoleVo> roleList = roleService.getOwnRole4User(currentUser,
				targetUserID);

		Set<String> beforeRoles = new HashSet<String>();
		for (RoleVo role : roleList) {
			beforeRoles.add(role.getRoleID());
		}
		roleService.assignRole4User(currentUser, targetUserID, beforeRoles,
				afterRoles);
		return "true";

	}

	/**
	 * 将已知父节点code和其子节点列表的map对象转换为Tree对象
	 */
	private TreeNode transPermVoToTree(String parentNodeId,
			String currentNodeId, Map<String, List<PermItem>> allPermMap,
			List<PermItem> existPerm) {

		// 如果这个节点是父节点
		if (allPermMap.get(currentNodeId) != null) {
			TreeNode t = new TreeNode();
			List<TreeNode> child = new ArrayList<TreeNode>();
			for (PermItem pi : allPermMap.get(currentNodeId)) {
				TreeNode tt = new TreeNode();
				tt = transPermVoToTree(currentNodeId, pi.getCode(), allPermMap,
						existPerm);
				child.add(tt);
			}
			// 如果是根节点 就新建一个TreeNode
			if (currentNodeId.trim().equals("0")) {
				t = new TreeNode();
				t.setId("0");
				t.setText("所有权限");
				t.setChildren(child);
				return t;
			}
			// 如果不是根节点，而且也有子节点，那么找到对应节点的PermItem 并设置对应属性和子节点
			else {
				PermItem sonPi = new PermItem();
				for (PermItem tempPi : allPermMap.get(parentNodeId)) {
					if (tempPi.getCode() == currentNodeId) {
						sonPi = tempPi;
					}
				}
				t.setId(sonPi.getPermID());
				t.setText(sonPi.getPermName());
				Map<String, Object> attri = new HashMap<String, Object>();
				attri.put("enableDataPermission",
						sonPi.getEnableDataPermission());
				attri.put("permType", sonPi.getPermType());
				t.setAttributes(attri);
				t.setChildren(child);

				return t;
			}

		} else {// 如果不是父节点（及是叶子节点） 那么就将对应信息赋值给node 并返回，结束递归
			PermItem sonPi = new PermItem();
			// 遍历获得叶子节点
			for (PermItem tempPi : allPermMap.get(parentNodeId)) {
				if (tempPi.getCode() == currentNodeId) {
					sonPi = tempPi;
				}
			}
			TreeNode sonTreeNode = new TreeNode();
			sonTreeNode.setId(sonPi.getPermID());
			sonTreeNode.setText(sonPi.getPermName());
			if (hasPerm(sonPi, existPerm)) {
				sonTreeNode.setChecked(true);
			}
			// 给书树节点增加额外的属性
			Map<String, Object> attri = new HashMap<String, Object>();
			attri.put("enableDataPermission", sonPi.getEnableDataPermission());
			attri.put("permType", sonPi.getPermType());
			sonTreeNode.setAttributes(attri);
			return sonTreeNode;
		}

	}

	private boolean hasPerm(PermItem sonPi, List<PermItem> existPerm) {
		for (PermItem permItem : existPerm) {
			if (sonPi == permItem || sonPi.getCode() == permItem.getCode()) {
				return true;
			}
		}
		return false;
	}

}
