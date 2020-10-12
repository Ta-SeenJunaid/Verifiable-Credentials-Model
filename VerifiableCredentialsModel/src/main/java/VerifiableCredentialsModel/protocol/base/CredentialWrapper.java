package VerifiableCredentialsModel.protocol.base;

import java.util.Map;

import VerifiableCredentialsModel.protocol.inf.Hashable;


public class CredentialWrapper implements Hashable {
	
	private Credential credential;
	
	private Map<String, Object> disclosure;
	
	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public Map<String, Object> getDisclosure() {
		return disclosure;
	}

	public void setDisclosure(Map<String, Object> disclosure) {
		this.disclosure = disclosure;
	}

	@Override
	public String getHash() {
		// TODO Auto-generated method stub
		return null;
	}

}

