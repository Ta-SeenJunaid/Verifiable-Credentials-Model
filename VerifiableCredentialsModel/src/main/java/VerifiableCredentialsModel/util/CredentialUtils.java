package VerifiableCredentialsModel.util;

import java.util.regex.Pattern;

import VerifiableCredentialsModel.constant.CredentialConstant;
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
	

}
