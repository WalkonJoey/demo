package com.hqdna.common.baseVo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AbstractBizObject implements Serializable{
	private static final long serialVersionUID = 6671021679892183344L;
	// 最后操作用户ID
	private String operatorID;
	// 最后操作用户名
	private String operatorName;
	// 操作时间
	private Timestamp operateDt;

	public String getOperatorID() {
		return operatorID;
	}

	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	public Timestamp getOperateDt() {
		return operateDt;
	}

	public void setOperateDt(Timestamp operateDt) {
		this.operateDt = operateDt;
	}
}
