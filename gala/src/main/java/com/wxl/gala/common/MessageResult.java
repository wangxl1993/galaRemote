package com.wxl.gala.common;

import java.io.Serializable;

/**
 * 通用的返回值信息
 * @author helen
 *
 */
public class MessageResult implements Serializable{

	//是否成功
	private boolean success;
	
	//消息
	private String message;
	
	//状态码
	private int code;//默认0 正常
	
	//结果数据
	private Object data;

	public MessageResult() {
		super();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
