package VerifiableCredentialsModel.protocol.request;

import java.util.Map;

import VerifiableCredentialsModel.protocol.base.IdPrivateKey;


public class CreateCredentialArgs {
	
	private Integer cptId;
	
	private String issuer;
	
	private Long expirationDate;
	
	private Map<String, Object> claim;
	
	private IdPrivateKey idPrivateKey;
	
	private Long issuanceDate = null;

	public Integer getCptId() {
		return cptId;
	}

	public void setCptId(Integer cptId) {
		this.cptId = cptId;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public Long getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Long expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Map<String, Object> getClaim() {
		return claim;
	}

	public void setClaim(Map<String, Object> claim) {
		this.claim = claim;
	}

	public IdPrivateKey getIdPrivateKey() {
		return idPrivateKey;
	}

	public void setIdPrivateKey(IdPrivateKey idPrivateKey) {
		this.idPrivateKey = idPrivateKey;
	}

	public Long getIssuanceDate() {
		return issuanceDate;
	}

	public void setIssuanceDate(Long issuanceDate) {
		this.issuanceDate = issuanceDate;
	}
}
