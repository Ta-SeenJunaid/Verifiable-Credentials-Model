package VerifiableCredentialsModel.service;

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

}
