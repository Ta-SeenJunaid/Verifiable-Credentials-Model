package VerifiableCredentialsModel.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import VerifiableCredentialsModel.constant.CredentialFieldDisclosureValue;
import VerifiableCredentialsModel.constant.ErrorCode;
import VerifiableCredentialsModel.protocol.base.Credential;
import VerifiableCredentialsModel.protocol.base.CredentialWrapper;
import VerifiableCredentialsModel.protocol.request.CreateCredentialArgs;
import VerifiableCredentialsModel.protocol.response.ResponseData;
import VerifiableCredentialsModel.util.CredentialUtils;
import VerifiableCredentialsModel.util.DateUtils;




public class CredentialServiceImpl implements CredentialService{
	
    private static final Logger logger = LoggerFactory.getLogger(CredentialServiceImpl.class);

    @Override
	public ResponseData<CredentialWrapper> createCredential(CreateCredentialArgs args){
		
		CredentialWrapper credentialWrapper = new CredentialWrapper();
		try {
			ErrorCode innerResponse = checkCreateCredentialArgsValidity(args, true);
			if (ErrorCode.SUCCESS.getCode() != innerResponse.getCode()) {
				logger.error("Generate Credential input format errror!");
				return new ResponseData<>(null, innerResponse);
			}
			
			Credential result = new Credential();
			String context = CredentialUtils.getDefaultCredentialContext();
			result.setContext(context);
			result.setId(UUID.randomUUID().toString());
			result.setCptId(args.getCptId());
			result.setIssuer(args.getIssuer());
			Long issuanceDate = args.getIssuanceDate();
			if (issuanceDate == null) {
				result.setIssuanceDate(DateUtils.getNoMillisecondTimeStamp());
			} else {

				result.setIssuanceDate(issuanceDate);
			}
			
			Long expirationDate = args.getExpirationDate();
			
			if(expirationDate == null) {
              logger.error("Create Credential Args illegal.");
              return new ResponseData<>(null, ErrorCode.CREDENTIAL_EXPIRE_DATE_ILLEGAL);
			} else {
				result.setExpirationDate(expirationDate);
			}
                
            result.setClaim(args.getClaim());
            Map<String, Object> disclosureMap = new HashMap<>(args.getClaim());
            for (Map.Entry<String, Object> entry : disclosureMap.entrySet()) {
            	disclosureMap.put(
            			entry.getKey(),
            			CredentialFieldDisclosureValue.DISCLOSED.getStatus());
            }
            credentialWrapper.setDisclosure(disclosureMap);
            
            Map<String, String> credentialProof = CredentialUtils.buildCredentialProof(
            		result, 
            		args.getIdPrivateKey().getPrivateKey(), 
            		disclosureMap);
            result.setProof(credentialProof);
                        
            credentialWrapper.setCredential(result);
            ResponseData<CredentialWrapper> responseData = new ResponseData<>(
            		credentialWrapper,
            		ErrorCode.SUCCESS);
            
            return responseData;			
			
		} catch(Exception e) {
			logger.error("Generate Credential failed due to system error. ", e);
			return new ResponseData<>(null, ErrorCode.CREDENTIAL_ERROR);
		}
	}
    
    private ErrorCode checkCreateCredentialArgsValidity(
			CreateCredentialArgs args, boolean privateKeyReired) {
		ErrorCode innerResponseData = CredentialUtils.isCreateCredentialArgsValid(args);
		if(ErrorCode.SUCCESS.getCode() != innerResponseData.getCode()) {
			logger.error("Create Credential Args illegal: {}", innerResponseData.getCodeDesc());
			return innerResponseData;
		}
		if (privateKeyReired && StringUtils.isEmpty(args.getIdPrivateKey().getPrivateKey())) {
			logger.error(ErrorCode.CREDENTIAL_PRIVATE_KEY_NOT_EXISTS.getCodeDesc());
			return ErrorCode.CREDENTIAL_PRIVATE_KEY_NOT_EXISTS;
		}
		return ErrorCode.SUCCESS;

	}

}
