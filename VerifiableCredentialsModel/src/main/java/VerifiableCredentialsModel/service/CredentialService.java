package VerifiableCredentialsModel.service;

import VerifiableCredentialsModel.protocol.base.Credential;
import VerifiableCredentialsModel.protocol.base.CredentialWrapper;
import VerifiableCredentialsModel.protocol.request.CreateCredentialArgs;
import VerifiableCredentialsModel.protocol.response.ResponseData;

public interface CredentialService {
	
	/**
	 * Create credential
	 * 
	 * @param args CreateCredentialArgs
	 * @return CredentialWrapper
	 */
	ResponseData<CredentialWrapper> createCredential(CreateCredentialArgs args);
	
	/**
	 * Json String of a credential
	 * 
	 * @param credential the credential
	 * @return credential string
	 */
	ResponseData<String> getCredentialJson(Credential credential);
	
	/**
	 * Generate credential with selective disclosure
	 * 
	 * @param credential the credential
	 * @param the setting of disclosure, such as: {@code{"name":1,"info":0,"age":1}},
     *     which means you WILL disclose "name" and "age", and "info" WILL NOT be disclosed 
	 * @return CredentialWrapper
	 */
	ResponseData<CredentialWrapper> createSelectiveCredential(
			Credential credential,
			String disclosure);
	
	/**
	 * Verify the validity of credential
	 * 
	 * @param credentialWrapper
	 * @return true if valid & false otherwise
	 * @throws Exception
	 */
	ResponseData<Boolean> verify(CredentialWrapper credentialWrapper) throws Exception;

}
