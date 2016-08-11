package com.hqdna.permission.util.exception;

import com.hqdna.common.baseException.BaseException;

/**
 * 用户角色权限关系异常类
 * 
 */
public class PermissionException extends BaseException {
	private static final long serialVersionUID = 6529181185141734356L;

	public PermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionException(String message) {
		super(message);
	}
}
