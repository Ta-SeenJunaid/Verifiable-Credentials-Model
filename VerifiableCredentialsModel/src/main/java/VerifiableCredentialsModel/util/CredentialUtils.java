package VerifiableCredentialsModel.util;

import java.util.regex.Pattern;


import VerifiableCredentialsModel.constant.CredentialConstant;
import VerifiableCredentialsModel.constant.ErrorCode;
import VerifiableCredentialsModel.constant.IdConstant;
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

}
