package VerifiableCredentialsModel.protocol.base;

public class IdPrivateKey {
	
	private String  privateKey;

	public IdPrivateKey() {
		super();
	}
	
	public IdPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}
