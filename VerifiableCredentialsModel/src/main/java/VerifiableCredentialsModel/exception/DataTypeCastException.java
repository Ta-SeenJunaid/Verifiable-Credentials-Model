package VerifiableCredentialsModel.exception;

import VerifiableCredentialsModel.constant.ErrorCode;

@SuppressWarnings("serial")
public class DataTypeCastException extends BaseException {

	public DataTypeCastException(String msg) {
		super(msg);
	}
	
	public DataTypeCastException(Throwable cause) {
		super(ErrorCode.DATA_TYPE_CASE_ERROR.getCodeDesc(), cause);
	}
	
	public ErrorCode getErrorCode() {
		return ErrorCode.DATA_TYPE_CASE_ERROR;
	}

}