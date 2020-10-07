package VerifiableCredentialsModel.exception;

import VerifiableCredentialsModel.constant.ErrorCode;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException {
	
	private ErrorCode errorCode = ErrorCode.BASE_ERROR;
	
	public BaseException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public BaseException(String msg) {
		super(msg);
	}
	
	public BaseException(ErrorCode errorCode) {
		this(errorCode.getCode() + "-" + errorCode.getCodeDesc());
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String toString() {
		String s = getClass().getName();
		StringBuilder builder = new StringBuilder();
		builder.append(s).append(". Error code = ").append(getErrorCode().getCode())
			.append(", Error message : ").append(getMessage());
		
		return builder.toString();
	}

}
