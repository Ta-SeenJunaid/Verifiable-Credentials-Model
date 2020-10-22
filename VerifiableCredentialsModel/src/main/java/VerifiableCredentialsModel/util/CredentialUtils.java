package VerifiableCredentialsModel.util;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import VerifiableCredentialsModel.constant.CredentialConstant;
import VerifiableCredentialsModel.constant.CredentialConstant.CredentialProofType;
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

}
