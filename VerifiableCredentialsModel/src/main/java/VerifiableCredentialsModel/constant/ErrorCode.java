package VerifiableCredentialsModel.constant;


public enum ErrorCode {
	
    SUCCESS(0, "success"),
    
    CPT_ID_ILLEGAL(100303, "cptId illegal"),
    
    CREDENTIAL_ERROR(100400, "error occured during processing credential tasks"),
    
    CREDENTIAL_EXPIRED(100402, "credential is expired"),
    
    CREDENTIAL_SIGNATURE_BROKEN(100405, "credential signature cannot be extracted"),
    
    CREDENTIAL_ISSUANCE_DATE_ILLEGAL(100408, "credential issuance date illegal"),
    
    CREDENTIAL_EXPIRE_DATE_ILLEGAL(100409, "expire date illegal"),
    
    CREDENTIAL_CLAIM_NOT_EXISTS(100410, "claim data does not exist"),
    
    CREDENTIAL_ID_NOT_EXISTS(100412, "credential id does not exist"),
    
    CREDENTIAL_CONTEXT_NOT_EXISTS(100413, "credential context does not exist"),

    CREDENTIAL_PRIVATE_KEY_NOT_EXISTS(100415, "private key for signing credential does not exist"),

    CREDENTIAL_ISSUER_INVALID(100418, "credential issuer invalid or mismatch the WeID auth"),
 
    CREDENTIAL_SIGNATURE_TYPE_ILLEGAL(100429, "credential signature type unknown"),
   
    CREDENTIAL_VERIFY_FAIL(100441, "credential verify fail."),
    
    UNKNOW_ERROR(160003, "unknow error, please check the error log."),
    
    ILLEGAL_INPUT(160004, "input parameter is illegal."),

    BASE_ERROR(160007, "baes exception error, please check the error log."),
    
    DATA_TYPE_CASE_ERROR(160008, "data type cast exception error, please check the error log.");
	
	private int code;
	
	private String codeDesc;
	
	ErrorCode(int code, String codeDesc) {
		this.code = code;
		this.codeDesc = codeDesc;
	}

	public static ErrorCode geetTypeByErrorCode(int errorCode) {
		for (ErrorCode type : ErrorCode.values()) {
			if (type.getCode() == errorCode) {
				return type;
			}
		}
		return ErrorCode.UNKNOW_ERROR;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	

}