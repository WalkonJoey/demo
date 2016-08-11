package com.hqdna.permission.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hqdna.common.baseController.BaseController;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.page.Combobox;
import com.hqdna.common.page.Json;
import com.hqdna.common.page.TreeNode;
import com.hqdna.permission.service.IPermissionService;
import com.hqdna.permission.util.IPermConstants;
import com.hqdna.permission.vo.EntityProperty;
import com.hqdna.permission.vo.PermItem;
import com.hqdna.permission.vo.RuleVo;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/permManage")
public class PermController extends BaseController {
	@Resource(name = "permissionService")
	private IPermissionService permissionService;

	@RequestMapping(value = "/getPermAttriTree", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getPermAttriTreeViaAssignPermPg(String permID,
			HttpSession session) {
		PermItem permItem = permissionService.getPermItemById(permID);
		List<EntityProperty> list = permissionService
				.getEntityProperties(permItem.getRefObjID());
		List<TreeNode> treeList = transEntityPropertyToTree(list);
		JSONArray ja = new JSONArray();
		ja.addAll(treeList);
		return ja.toString();
	}

	/**
	 * 将List<EntityProperty>转换为List<TreeNode>用于前台显示树
	 * 
	 * @param list
	 * @return
	 */
	private List<TreeNode> transEntityPropertyToTree(List<EntityProperty> list) {
		List<TreeNode> reList = new ArrayList<TreeNode>();
		if (list != null) {
			for (EntityProperty entityProperty : list) {
				TreeNode tree = new TreeNode();
				tree.setId(entityProperty.getPropName());
				tree.setText(entityProperty.getPropTitle());
				tree.setState("open");
				Map<String, Object> attri = new HashMap<String, Object>();
				attri.put("propType", entityProperty.getPropType());
				if (entityProperty.getPropRef() != null) {
					tree.setChildren(transToNode(entityProperty.getPropName(),
							entityProperty.getPropTitle(),
							entityProperty.getPropRef()));
				} else {
					attri.put("hasChildren", false);
				}
				tree.setAttributes(attri);
				reList.add(tree);
			}
			return reList;
		}

		return null;
	}

	/**
	 * 将List<EntityProperty>转换为List<TreeNode>用于前台显示树
	 * 
	 * @param parentFieldName
	 * @param propRef
	 * @return List<TreeNode>
	 */
	private List<TreeNode> transToNode(String parentFieldName,
			String parentFieldTitle, List<EntityProperty> propRef) {
		List<TreeNode> reList = new ArrayList<TreeNode>();
		if (propRef != null) {
			for (EntityProperty entityProperty : propRef) {
				TreeNode tNode = new TreeNode();
				String currentFieldName = parentFieldName + "."
						+ entityProperty.getPropName();
				String currentFieldTitle = parentFieldTitle + "."
						+ entityProperty.getPropTitle();
				tNode.setId(currentFieldName);
				tNode.setText(currentFieldTitle);
				Map<String, Object> attri = new HashMap<String, Object>();
				attri.put("propType", entityProperty.getPropType());
				if (entityProperty.getPropRef() != null) {
					tNode.setState("closed");// 树的第二层后的节点就关闭状态
					tNode.setChildren(transToNode(currentFieldName,
							currentFieldTitle, entityProperty.getPropRef()));
				} else {
					tNode.setChildren(null);
				}
				tNode.setAttributes(attri);

				reList.add(tNode);
			}
			return reList;
		}
		return null;
	}

	@RequestMapping(value = "/addDataFilter", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addDataFilterViaAssignPermPg(String roleID, String permID,
			String dataFilter, HttpSession session) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		JSONArray jsonArray = JSONArray.fromObject(dataFilter);
		RuleVo rules[] = new RuleVo[jsonArray.size()];
		// List<RuleVo> list=null;
		// RuleVo[] rules=list.toArray(new RuleVo[]{});
		Date now = new Date();
		String nowTime = DateUtil.formatDateTime(now);

		@SuppressWarnings("unchecked")
		HashMap<String, List<RuleVo>> ruleMap = (HashMap<String, List<RuleVo>>) session
				.getAttribute(IPermConstants.EDIT_RULE);
		List<RuleVo> ruleList = ruleMap.get(permID);
		if (ruleList == null) {
			ruleList = new ArrayList<RuleVo>();
		} else {
			ruleMap.remove(permID);
			ruleList = new ArrayList<RuleVo>();
		}

		// 解析dataFilter
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray sonJsonArray = jsonArray.getJSONArray(i);
			Object[] attr = sonJsonArray.toArray();
			RuleVo ruleVo = new RuleVo();
			transToRoleVo(attr, ruleVo);
			ruleVo.setInnerNo(i);
			ruleVo.setOperateDt(Timestamp.valueOf(nowTime));
			ruleVo.setOperatorID(currentUser.getUserID());
			ruleVo.setOperatorName(currentUser.getUserCnName());
			ruleList.add(ruleVo);
			rules[i] = ruleVo;
		}
		ruleMap.put(permID, ruleList);
		session.setAttribute(permID, ruleMap);
		// permissionService
		// .saveRules4RolePerm(currentUser, roleID, permID, rules);
		Json js = new Json();
		js.setSuccess(true);
		js.setMsg("保存成功！");
		return js.toString();
	}

	private void transToRoleVo(Object[] attr, RuleVo ruleVo) {
		ruleVo.setLeftParentheses(attr[0].toString());
		ruleVo.setPropertyName(attr[1].toString());
		ruleVo.setMidOperator(attr[2].toString());
		ruleVo.setPropertyValue(attr[3].toString());
		ruleVo.setRightParenthese(attr[4].toString());
		ruleVo.setLogical(attr[5].toString());
		ruleVo.setPropertyTitle(attr[6].toString());
	}

	/**
	 * 给用户分配权限，这里不包括
	 * 
	 * @param roleID
	 * @param treeNodes
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/assignRolePerm", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json assignRolePermViaAssignPermPg(String roleID, String treeNodes,
			HttpSession session) {
		// 解析的数据格式：
		// [
		// {"id":"perm2aabe936-185b-42a0-8825-46f4ff8d85ed","text":"采购订单查看权限","state":"open","children":[],"attributes":{"enableDataPermission":1,"permType":"View"},"checked":true,"iconCls":"","domId":"_easyui_tree_16","target":{}},
		// {"id":"permb5bcbd6a-deaa-4327-9cb3-3feb428af088","text":"采购订单编辑权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Edit"},"checked":true,"iconCls":"","domId":"_easyui_tree_17","target":{}},
		// {"id":"perm7d6a5f77-5b31-4df8-b0d1-8b77a87d7371","text":"销售订单管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":true,"iconCls":"","domId":"_easyui_tree_27","target":{}}
		// ]
		System.out.println(treeNodes);
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		@SuppressWarnings("unchecked")
		HashMap<String, List<RuleVo>> ruleMap = (HashMap<String, List<RuleVo>>) session
				.getAttribute(IPermConstants.EDIT_RULE);
		JSONArray jsonTreeNodes = JSONArray.fromObject(treeNodes);
		Set<String> newPermIdSet = new HashSet<String>();
		Set<String> oldPermIdSet = new HashSet<String>();
//[{"id":"0","text":"所有权限","state":"open","children":[{"id":"perm7484f78d-4335-4cbb-a9c2-6cae70cd2f12","text":"用户权限管理","state":"open","children":[{"id":"permcc08d659-85cd-4116-a0c0-0c24a2e68322","text":"用户管理","state":"open","children":[{"id":"perm21d59a65-fda5-48ed-afb1-0098634ffa69","text":"用户信息查看权限","state":"open","children":[],"attributes":{"enableDataPermission":1,"permType":"View"},"checked":true,"iconCls":"","domId":"_easyui_tree_4","target":{}},{"id":"perm3af2805e-f33f-4b0c-922c-14c93bb5d5ac","text":"用户信息修改权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Edit"},"checked":true,"iconCls":"","domId":"_easyui_tree_5","target":{}},{"id":"perm9af8f5f8-b3d1-4bcb-86af-df7fb11fde32","text":"用户信息删除权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Delete"},"checked":true,"iconCls":"","domId":"_easyui_tree_6","target":{}},{"id":"perm0e54fe3d-8450-4145-be31-172707f7dad4","text":"用户授权权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Assign"},"checked":true,"iconCls":"","domId":"_easyui_tree_7","target":{}}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":true,"iconCls":"","domId":"_easyui_tree_3","target":{}},{"id":"perm9bf9f8c5-a30e-41a8-9e4b-4fd627798054","text":"角色管理","state":"open","children":[{"id":"perm37758a94-5085-492e-999f-061c99102df3","text":"角色信息删除权限","state":"open","children":[],"attributes":{"enableDataPermission":1,"permType":"Delete"},"checked":true,"iconCls":"","domId":"_easyui_tree_9","target":{}},{"id":"permaa62f134-51b2-4e59-9630-939eb06c316f","text":"角色信息查看权限","state":"open","children":[],"attributes":{"enableDataPermission":1,"permType":"View"},"checked":true,"iconCls":"","domId":"_easyui_tree_10","target":{}},{"id":"permc176b444-128a-4e21-8aeb-d1d83a27ce7e","text":"角色信息修改权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Edit"},"checked":true,"iconCls":"","domId":"_easyui_tree_11","target":{}}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":true,"iconCls":"","domId":"_easyui_tree_8","target":{}}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":true,"iconCls":"","domId":"_easyui_tree_2","target":{}},{"id":"perme8313d14-a39a-4b1f-8613-6bd8eed233e4","text":"基础信息管理","state":"open","children":[{"id":"perm32dbbb4c-d0c8-4986-9bad-2c0214fc046e","text":"币种管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_13"},{"id":"perme9743dcf-df65-4e81-a7d4-a200d2a932e1","text":"国家地区信息","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_14"},{"id":"perm8847063a-c9f4-49c3-93cd-38164141cb4b","text":"运输方式","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_15"},{"id":"perm6476046d-85e0-40aa-a5cf-1127008a843a","text":"物流公司维护","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_16"},{"id":"permbe54f827-021a-44f1-869f-4175505d2b5c","text":"仓库信息维护","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_17"},{"id":"perm13b4203c-0692-49ca-9375-bf84bac66bf6","text":"参数管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_18"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_12"},{"id":"perm90f89ec1-f5f8-470c-92ce-575f7dea2d99","text":"采购管理","state":"open","children":[{"id":"permc7cc0aa7-129c-4835-ae78-1e98f16aa47f","text":"采购订单管理","state":"open","children":[{"id":"perm2aabe936-185b-42a0-8825-46f4ff8d85ed","text":"采购订单查看权限","state":"open","children":[],"attributes":{"enableDataPermission":1,"permType":"View"},"checked":false,"iconCls":"","domId":"_easyui_tree_21"},{"id":"permb5bcbd6a-deaa-4327-9cb3-3feb428af088","text":"采购订单编辑权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Edit"},"checked":false,"iconCls":"","domId":"_easyui_tree_22"},{"id":"perm53d14e53-86f5-484c-9c59-b0a34aacc54c","text":"采购订单删除权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Delete"},"checked":false,"iconCls":"","domId":"_easyui_tree_23"},{"id":"perme1faf8c0-b273-4cc2-8185-ecb447d98cd0","text":"采购订单审批权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Approve"},"checked":false,"iconCls":"","domId":"_easyui_tree_24"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_20"},{"id":"perm4c1c7a0a-8e3e-4728-9887-1b2e99264576","text":"供应商管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_25"},{"id":"permf564d5ee-3ff6-418c-a3c8-9bdcf5c559dd","text":"发货单管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_26"},{"id":"permaee323b8-7789-459c-8ef5-71a460fc3f86","text":"快递单查询","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_27"},{"id":"perm83848f75-c575-43d0-b9fa-ad5726748dc8","text":"备货参考信息","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_28"},{"id":"permfd7bbfd6-6557-4f83-b889-24834701fe57","text":"采购订单统计","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_29"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_19"},{"id":"perm13b4203c-0692-49ca-9375-bf84bac66bf2","text":"产品管理","state":"open","children":[{"id":"perm1cdd743e-1756-4b63-9467-089b2e250eea","text":"产品分类","state":"open","children":[{"id":"perm6ec464dd-31e8-46a7-bce6-de64244e05af","text":"产品分类查看","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"View"},"checked":false,"iconCls":"","domId":"_easyui_tree_32"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_31"},{"id":"perme1faf8c0-b273-4cc2-8185-ecb447d98111","text":"属性管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_33"},{"id":"perme1faf8c0-b273-4cc2-8185-ecb447d98117","text":"产品信息","state":"open","children":[{"id":"perme1faf8c0-b273-4cc2-8185-ecb447d98110","text":"产品信息查看权限","state":"open","children":[],"attributes":{"enableDataPermission":1,"permType":"View"},"checked":false,"iconCls":"","domId":"_easyui_tree_35"},{"id":"perm110c5c96-38e2-48f8-8d05-7d7bd46b6f69","text":"产品信息编辑权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Edit"},"checked":false,"iconCls":"","domId":"_easyui_tree_36"},{"id":"perm699ece68-ab59-4cc9-8e97-e5cd5dfd189d","text":"产品信息删除权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Delete"},"checked":false,"iconCls":"","domId":"_easyui_tree_37"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_34"},{"id":"perm0df187f5-33ed-40dc-bb40-f4265827f099","text":"入库单管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_38"},{"id":"perm3a305727-ab36-4e47-be7e-ffd1d4cfe67c","text":"库存管理","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_39"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_30"},{"id":"perm5df431c8-4d3b-4a90-af66-ac2fe83190fe","text":"销售管理","state":"open","children":[{"id":"perm7d6a5f77-5b31-4df8-b0d1-8b77a87d7371","text":"销售订单管理","state":"open","children":[{"id":"permcbe70627-d3fa-459e-980a-02f7de2514b1","text":"销售订单查看权限","state":"open","children":[],"attributes":{"enableDataPermission":1,"permType":"View"},"checked":false,"iconCls":"","domId":"_easyui_tree_42"},{"id":"permcf3e9a53-331e-47ac-9f5f-a6ff4f1aa01d","text":"销售订单编辑权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Edit"},"checked":false,"iconCls":"","domId":"_easyui_tree_43"},{"id":"perm2b91daf4-64d0-4450-816a-2428a66a5cc8","text":"销售订单审批权限","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Approve"},"checked":false,"iconCls":"","domId":"_easyui_tree_44"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_41"},{"id":"perm110192ba-c967-4e67-9023-c4af95a95004","text":"物流发运单","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_45"},{"id":"perm4dc5706f-ce2b-40f1-b0d5-1d30ab16c3f6","text":"境外物流查询","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_46"},{"id":"perm99ab45b3-2ddc-4a91-9621-4717bb1543a4","text":"销售单","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_47"},{"id":"permf8975705-0b87-4c94-a7c6-6de1a3475f97","text":"销售统计","state":"open","children":[],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_48"}],"attributes":{"enableDataPermission":0,"permType":"Menu"},"checked":false,"iconCls":"","domId":"_easyui_tree_40"},{"id":"perm3c7bac15-efc7-4001-9af8-d154f4aad967","text":"财务管理","sta...
		List<PermItem> permItemList = permissionService.getAssignedPerm4Role(
				currentUser, roleID);
		for (PermItem permItem : permItemList) {
			oldPermIdSet.add(permItem.getPermID());
		}
		for (Object tNode : jsonTreeNodes) {
			JSONObject jsonObj = JSONObject.fromObject(tNode);
			// TreeNode treeNode
			// =(TreeNode)JSONObject.toBean(jsonObj,TreeNode.class);//将建json对象转换为TreeNode对象
			String targetPermID = jsonObj.getString("id");
			if (!"0".equalsIgnoreCase(targetPermID)) {
				newPermIdSet.add(targetPermID);
			} else {
				continue;
			}
			// 添加对应perm的数据权限到数据库
			if (ruleMap.containsKey(targetPermID)) {
				List<RuleVo> containKeyList = ruleMap.get(jsonObj
						.getString("id"));
				RuleVo[] ruleVoArr = null;
				if (containKeyList != null && !containKeyList.isEmpty()) {
					ruleVoArr = new RuleVo[containKeyList.size()];
					int ii = 0;
					for (RuleVo ruleVo : containKeyList) {
						ruleVoArr[ii] = ruleVo;
						ii++;
					}
				}
				permissionService.assignPermission4RoleWithRule(currentUser,
						roleID, targetPermID, oldPermIdSet, ruleVoArr);
			} else if (!oldPermIdSet.contains(targetPermID)) {
				permissionService.assignPermission4RoleWithoutRule(currentUser,
						roleID, targetPermID);
			}

			// treeList.add(treeNode);
		}
		//最后处理需要删除的权限
		oldPermIdSet.removeAll(newPermIdSet);
		permissionService.deletePerm4Role(currentUser, roleID,
				oldPermIdSet.toArray(new String[] {}));
		Json json = new Json();
		json.setSuccess(true);
		return json;
	}

	/**
	 * 获取权限保存的数据权限过滤记录，并转换为[["((","userCNName","大于等于","aaa",")","并且"],["(",
	 * "loginID","等于","bbb","))",""]]这种数据格式，供前台dia调用
	 * 
	 * @param permID
	 * @return
	 */
	@RequestMapping(value = "/getExistDataFilter", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDataFilterByPermIDViaAssignPermPg(String roleID,
			String permID, HttpSession session) {
		if (logger.isDebugEnabled()) {
			logger.debug("当前选中进行分配数据权限的权限ID：" + permID);
		}
		Object session_ruleMap = session.getAttribute(IPermConstants.EDIT_RULE);
		if (session_ruleMap == null) {
			session.setAttribute(IPermConstants.EDIT_RULE,
					new HashMap<String, List<RuleVo>>());
		}
		@SuppressWarnings("unchecked")
		HashMap<String, List<RuleVo>> ruleMap = (HashMap<String, List<RuleVo>>) session
				.getAttribute(IPermConstants.EDIT_RULE);
		List<RuleVo> rules = ruleMap.get(permID);
		if (rules == null) {
			List<RuleVo> formerRules = permissionService.getRules4RolePerm(
					roleID, permID);
			if (formerRules != null && !formerRules.isEmpty()) {
				// 如果当前不存在编辑中的rule,则新增permID对应的list,并从库中查询到以前的rule放到list中
				rules = new ArrayList<RuleVo>();
				rules.addAll(formerRules);
				ruleMap.put(permID, rules);
			} else {
				ruleMap.put(permID, rules);
				session.setAttribute(permID, ruleMap);
				return null;
			}
		}
		if (logger.isDebugEnabled()) {
			if (rules != null) {
				for (RuleVo ruleVo : rules) {
					logger.debug(ruleVo.getPropertyName());
				}
			}
		}
		JSONArray jArray = JSONArray.fromObject(rules);
		return jArray.toString();
	}

	/**
	 * 获取对应用户对应权限对应属性名的属性值列表，供combobox显示 格式为[{\
	 * "id\":1,\"text\":\"text1\"},{\"id\":2,\"text\":\"text2\"},{\"id\":3,\"text\":\"text3\",\"selected\":true},{\"id\":4,\"text\":\"text4\"},{\"id\":5,\"text\":\"t
	 * e x t 5 \ " } ]
	 * 
	 * @param permID
	 * @param attributeName
	 * @return
	 */
	@RequestMapping(value = "/getValueByAttr", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getValueByAttrViaAssignPermPg(String permID,
			String attributeName) {
		//EntityProperty pt = new EntityProperty();
		PermItem permItem = permissionService.getPermItemById(permID);
		List<Object> perm = permissionService.getPropertyValue(
				permItem.getRefObjID(), attributeName);
		/*
		 * List<Map<String,Object>> listMap = new
		 * ArrayList<Map<String,Object>>(); int i = 0; for (Object object :
		 * perm) { Map<String,Object> map = new HashMap<String,Object>();
		 * map.put("id",i); map.put("text", object.toString()); if(i==0){
		 * map.put("selected", true); } else{ map.put("selected", false); } i++;
		 * listMap.add(map); } JSONArray jsonArr =
		 * JSONArray.fromObject(listMap); return jsonArr.toString();
		 */
		List<Combobox> listMap = new ArrayList<Combobox>();
		int i = 0;
		for (Object object : perm) {
			/*
			 * Map<String,Object> map = new HashMap<String,Object>();
			 * map.put("id",i); map.put("text", object.toString()); if(i==0){
			 * map.put("selected", true); } else{ map.put("selected", false); }
			 */
			Combobox tempCom = new Combobox();
			tempCom.setId(String.valueOf(i));
			tempCom.setText(object.toString());
			tempCom.setSelected(false);
			i++;
			listMap.add(tempCom);
		}
		JSONArray jsonArr = JSONArray.fromObject(listMap);
		return jsonArr.toString();

	}

	/**
	 * 获取对应用户对应权限以前的的数据过滤，显示在datagrid中
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getExistDFilterGrid", produces = "application/json;charset=UTF-8")
	public ModelAndView getExistDFilterGridViaAssignPermPg(String roleID,
			String permID, HttpSession session) {
		Map<String, Object> attributes = new HashMap<String, Object>();

		if (permID == null || "".equals(permID)) {
			return null;
		}
		/*
		 * HashMap<String, List<RuleVo>> ruleMap = (HashMap<String,
		 * List<RuleVo>>) session .getAttribute(IPermConstants.EDIT_RULE);
		 * List<RuleVo> ruleList = ruleMap.get(permID);
		 */
		Object session_ruleMap = session.getAttribute(IPermConstants.EDIT_RULE);
		if (session_ruleMap == null) {
			session.setAttribute(IPermConstants.EDIT_RULE,
					new HashMap<String, List<RuleVo>>());
		}
		@SuppressWarnings("unchecked")
		HashMap<String, List<RuleVo>> ruleMap = (HashMap<String, List<RuleVo>>) session
				.getAttribute(IPermConstants.EDIT_RULE);
		List<RuleVo> rules = ruleMap.get(permID);
		if (rules == null) {
			List<RuleVo> formerRules = permissionService.getRules4RolePerm(
					roleID, permID);
			if (formerRules != null && !formerRules.isEmpty()) {
				// 如果当前不存在编辑中的rule,则新增permID对应的list,并从库中查询到以前的rule放到list中
				rules = new ArrayList<RuleVo>();
				rules.addAll(formerRules);
				ruleMap.put(permID, rules);
			} else {
				ruleMap.put(permID, rules);
				session.setAttribute(permID, ruleMap);
			}
		}

		if (rules == null || rules.size() <= 0) {
			attributes.put("total", 0);
			attributes.put("rows", new ArrayList<RuleVo>(0));
		} else {
			attributes.put("total", rules.size());
			attributes.put("rows", rules);
		}
		// JSONObject jsonObj = JSONObject.fromObject(attributes);
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
		// return jsonObj.toString();
	}
}
