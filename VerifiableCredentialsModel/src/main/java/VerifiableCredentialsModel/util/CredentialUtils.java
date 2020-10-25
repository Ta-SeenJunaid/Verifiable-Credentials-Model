package VerifiableCredentialsModel.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import VerifiableCredentialsModel.constant.CredentialConstant;
import VerifiableCredentialsModel.constant.CredentialConstant.CredentialProofType;
import VerifiableCredentialsModel.constant.CredentialFieldDisclosureValue;
import VerifiableCredentialsModel.constant.ErrorCode;
import VerifiableCredentialsModel.constant.IdConstant;
import VerifiableCredentialsModel.constant.ParamKeyConstant;
import VerifiableCredentialsModel.protocol.base.Credential;
import VerifiableCredentialsModel.protocol.request.CreateCredentialArgs;



public class CredentialUtils {
	
	public static String getDefaultCredentialContext() {
		return CredentialConstant.DEFAULT_CREDENDTIAL_CONTEXT;
	}
	
	public static CreateCredentialArgs extractCredentialMetadata(Credential arg) {
		
		if (arg == null) {
			return null;
		}
		
		CreateCredentialArgs generateCredentialArgs = new CreateCredentialArgs();
		generateCredentialArgs.setCptId(arg.getCptId());
		generateCredentialArgs.setIssuer(arg.getIssuer());
		generateCredentialArgs.setIssuanceDate(arg.getIssuanceDate());
        generateCredentialArgs.setExpirationDate(arg.getExpirationDate());
		generateCredentialArgs.setClaim(arg.getClaim());
		return generateCredentialArgs;
				
	}
	
	public static boolean isValidUuid(String id) {
		Pattern p = Pattern.compile(IdConstant.UUID_PATTERN);
		return p.matcher(id).matches();
	}
	
	public static ErrorCode isCreateCredentialArgsValid(
			CreateCredentialArgs args) {
		if (args == null) {
			return ErrorCode.ILLEGAL_INPUT;
		}
		
		if (args.getCptId() == null || args.getCptId().intValue() < 0) {
			return ErrorCode.CPT_ID_ILLEGAL;
		}
		
		///***********Have to check issuer Validity ********
		
		Long issuanceDate = args.getIssuanceDate();

		if (issuanceDate != null && issuanceDate <= 0) {
			return ErrorCode.CREDENTIAL_ISSUANCE_DATE_ILLEGAL;
		}
		Long expirationDate = args.getExpirationDate();
		if (expirationDate == null
				|| expirationDate.longValue() <0 
				|| expirationDate.longValue() == 0) {
			return ErrorCode.CREDENTIAL_EXPIRE_DATE_ILLEGAL;
		}

		if(!DateUtils.isAfterCurrentTime(expirationDate)) {
			return ErrorCode.CREDENTIAL_CLAIM_NOT_EXISTS;
		}
		
		if (issuanceDate != null && expirationDate < issuanceDate) {
			return ErrorCode.CREDENTIAL_ISSUANCE_DATE_ILLEGAL;
		}
		
		if (args.getClaim() == null || args.getClaim().isEmpty()) {
			return ErrorCode.CREDENTIAL_CLAIM_NOT_EXISTS;
		}
		
		return ErrorCode.SUCCESS;
	}
	
	public static ErrorCode isCredentialContentValid(Credential args) {
		String credentialId = args.getId();
		if (StringUtils.isEmpty(credentialId) || 
				!CredentialUtils.isValidUuid(credentialId)) {
			return ErrorCode.CREDENTIAL_ID_NOT_EXISTS;
		}
		String context = args.getContext();
		if (StringUtils.isEmpty(context)) {
			return ErrorCode.CREDENTIAL_CONTEXT_NOT_EXISTS;
		}
		Long issuanceDate = args.getIssuanceDate();
		if (issuanceDate == null) {
			return ErrorCode.CREDENTIAL_ISSUANCE_DATE_ILLEGAL;
		}
		
		if (issuanceDate.longValue() > args.getExpirationDate().longValue()) {
			return ErrorCode.CREDENTIAL_EXPIRED;
		}
		
		Map<String, String> proof = args.getProof();
		return isCredentialProofValid(proof);
	}
	
	private static boolean isCredentialProofTypeValid(String type) {
		
		if (!StringUtils.isEmpty(type)) {
			for (CredentialProofType proofType : CredentialConstant.CredentialProofType.values()) {
				if(StringUtils.equalsIgnoreCase(type, proofType.getTypeName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static ErrorCode isCredentialProofValid(Map<String, String> proof) {
		if (proof == null) {
			return ErrorCode.ILLEGAL_INPUT;
		}
		String type = proof.get(ParamKeyConstant.PROOF_TYPE);
		if(!isCredentialProofTypeValid(type)) {
			return ErrorCode.CREDENTIAL_SIGNATURE_TYPE_ILLEGAL;
		}
		
        Long created = Long.valueOf(proof.get(ParamKeyConstant.PROOF_CREATED));
        if (created.longValue() <= 0) {
            return ErrorCode.CREDENTIAL_ISSUANCE_DATE_ILLEGAL;
        }
        
        //************* Have to work with creator ***************
        
        if (type.equalsIgnoreCase(CredentialProofType.ECDSA.getTypeName())) {
        	String signature = proof.get(ParamKeyConstant.CREDENTIAL_SIGNATURE);
        	if (StringUtils.isAllEmpty(signature) || !DataToolUtils.isValidBase64String(signature)) {
        		return ErrorCode.CREDENTIAL_SIGNATURE_BROKEN;
        	}
        }
        
        return ErrorCode.SUCCESS;
	}
	
	public static ErrorCode isCredentialValid(Credential args) {
		if (args == null) {
			return ErrorCode.ILLEGAL_INPUT;
		}
		CreateCredentialArgs createCredentialArgs = extractCredentialMetadata(args);
		ErrorCode metadataResponseData = isCreateCredentialArgsValid(createCredentialArgs);
		if (ErrorCode.SUCCESS.getCode() != metadataResponseData.getCode()) {
			return metadataResponseData;
		}
		
		ErrorCode contentResponseData = isCredentialContentValid(args);
		if (ErrorCode.SUCCESS.getCode() != contentResponseData.getCode()) {
			return contentResponseData;
		}
		return ErrorCode.SUCCESS;
	}
	
    public static Map<String, String> buildCredentialProof(
            Credential credential,
            String privateKey,
            Map<String, Object> disclosureMap) throws Exception{
            Map<String, String> proof = new HashMap<>();
            proof.put(ParamKeyConstant.PROOF_CREATED, credential.getIssuanceDate().toString());
            proof.put(ParamKeyConstant.PROOF_CREATOR, credential.getIssuer());
            proof.put(ParamKeyConstant.PROOF_TYPE, getDefaultCredentialProofType());
            proof.put(ParamKeyConstant.CREDENTIAL_SIGNATURE,
                getCredentialSignature(credential, privateKey, disclosureMap));
            
            return proof;
    }
	
	public static String getCredentialSignature(Credential credential, String privateKey,
			Map<String, Object> disclosureMap) throws Exception {
		String rawData = CredentialUtils.getCredentialThumbprintWithoutSig(credential, 
				disclosureMap);
		
		return DataToolUtils.secp256k1Sign(rawData, privateKey);
	}
	
	public static String getDefaultCredentialProofType() {
		return CredentialConstant.CredentialProofType.ECDSA.getTypeName();
	}
	
	public static Credential copyCredential(Credential credential) {
		Credential ct = new Credential();
		ct.setContext(credential.getContext());
		
		Map<String, String> originalProof = credential.getProof();
		if (originalProof != null) {
			Map<String, String> proof = DataToolUtils
					.deserialize(DataToolUtils.serialize(originalProof), HashMap.class);
			ct.setProof(proof);
		}
		
		Map<String, Object> originalClaim = credential.getClaim();
		if(originalClaim != null) {
			Map<String, Object> claim = DataToolUtils.
					deserialize(DataToolUtils.serialize(originalClaim), HashMap.class);
			ct.setClaim(claim);
		}
		
        ct.setIssuanceDate(credential.getIssuanceDate());
        ct.setCptId(credential.getCptId());
        ct.setExpirationDate(credential.getExpirationDate());
        ct.setIssuer(credential.getIssuer());
        ct.setId(credential.getId());
        return ct;
	}
	
	public static String getCredentialThumbprintWithoutSig(
			Credential credential, Map<String, Object> disclosures) {
		try {
			Credential rawCredential = copyCredential(credential);
			rawCredential.setProof(null);
			return getCredentialThumbprint(rawCredential, disclosures);
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}
	
	public static String getCredentialThumbprint(
			Credential credential, Map<String, Object> disclosures) {
		
		try {
			Map<String, Object> credMap = DataToolUtils.objToMap(credential);
			String claimHash = getClaimHash(credential, disclosures);
			credMap.put(ParamKeyConstant.CLAIM, claimHash);
			return DataToolUtils.mapToCompactJson(credMap);
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
		
	}
	
	public static String getClaimHash(Credential credential, Map<String, Object> disclosures) {
		Map<String, Object> claim = credential.getClaim();
		Map<String, Object> claimHashMap = new HashMap<>(claim); 
		Map<String, Object> disclosureMap;
		
		if (disclosures == null) {
			disclosureMap = new HashMap<>(claim);
			for (Map.Entry<String, Object> entry : disclosureMap.entrySet()) {
				disclosureMap.put(
						entry.getKey(),
						CredentialFieldDisclosureValue.DISCLOSED.getStatus()
						);
			}
		} else {
			disclosureMap = disclosures;
		}
		
		for (Map.Entry<String, Object> entry : disclosureMap.entrySet()) {
			
			if(CredentialFieldDisclosureValue.DISCLOSED.
    				getStatus().equals(entry.getValue())) {
				claimHashMap.put(entry.getKey(), getFieldHash(claimHashMap.get(entry.getKey())));
			}
			else {
				claimHashMap.put(entry.getKey(), claimHashMap.get(entry.getKey()));
			}
		}
		
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(
				claimHashMap.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
			
			@Override
			public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
				
		StringBuffer hash = new StringBuffer();
		for (Map.Entry<String, Object> en : list) {
			hash.append(en.getKey()).append(en.getValue());
		}
		
		return hash.toString();
	}
	
	public static String getFieldHash(Object field) {
		return DataToolUtils.sha256(String.valueOf(field));
	}
}
