package com.hqdna.user.util;

import com.hqdna.common.IGlobalConstants;

public interface IUserConstants extends IGlobalConstants {
	// 放入session中的当前用户的key
	public static final String CURRENT_USER = "CURRENT_USER";

	// 在线用户
	public static final String ONLINE_USERS = "ONLINE_USERS";

	//密码末尾校验码,
	public static final String VERIFY_CODE = "aaron-joey";

	// 系统管理员用户
	public static final String SYSTEM_USER_ID = "user00000000-0000-0000-0000-000000000000";
}
