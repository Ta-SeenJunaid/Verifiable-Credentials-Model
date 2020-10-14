package VerifiableCredentialsModel.protocol.response;

import VerifiableCredentialsModel.constant.ErrorCode;

public class ResponseData<T> {
	
	private T result;
	
	private Integer code;
	
	private String errroMessage;
	 
	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getErrroMessage() {
		return errroMessage;
	}

	public void setErrroMessage(String errroMessage) {
		this.errroMessage = errroMessage;
	}

	public ResponseData() {
		this.setErrorCode(ErrorCode.SUCCESS);
	}
	
	public ResponseData(T result, ErrorCode errorCode) {
		this.result = result;
		if (errorCode != null ) {
			this.code = errorCode.getCode();
			this.errroMessage = errorCode.getCodeDesc();
		}
	}
	
	public ResponseData(T result, Integer code, String errorMessage) {
		this.result = result;
		this.code = code;
		this.errroMessage = errorMessage;
	}
	
	public void setErrorCode(ErrorCode errorCode) {
		if (errorCode != null) {
			this.code = errorCode.getCode();
			this.errroMessage = errorCode.getCodeDesc();
		}
	}
}
