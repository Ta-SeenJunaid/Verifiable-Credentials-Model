package VerifiableCredentialsModel.constant;

public enum CredentialFieldDisclosureValue {
	
	EXISTED(2),
	
	DISCLOSED(1),
	
	NOT_DISCLOSED(0);
	
	private Integer status;
	
	CredentialFieldDisclosureValue(Integer status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return status;
	}

}