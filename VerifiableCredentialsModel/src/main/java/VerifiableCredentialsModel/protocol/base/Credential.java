package VerifiableCredentialsModel.protocol.base;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import VerifiableCredentialsModel.constant.ParamKeyConstant;
import VerifiableCredentialsModel.protocol.inf.Hashable;


public class Credential implements Hashable{
	
	private String context;
	
	private String id;
	
	private Integer cptId;
	
	private String issuer;
	
	private Long issuanceDate;
	
	private Long expirationDate;
	
	private Map<String, Object> claim;
	
	private Map<String, String> proof;

	
	public String getSignature() {
		return getValueFromProof(ParamKeyConstant.CREDENTIAL_SIGNATURE);
	}
	
	public String getProofType() {
		return getValueFromProof(ParamKeyConstant.PROOF_TYPE);
	}

	private String getValueFromProof(String key) {
		if (proof != null) {
			return proof.get(key);
		}
		return StringUtils.EMPTY;
	}

	@Override
	public String getHash() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Long getIssuanceDate() {
		return issuanceDate;
	}

	public void setIssuanceDate(Long issuanceDate) {
		this.issuanceDate = issuanceDate;
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

	public Map<String, String> getProof() {
		return proof;
	}

	public void setProof(Map<String, String> proof) {
		this.proof = proof;
	}
}

