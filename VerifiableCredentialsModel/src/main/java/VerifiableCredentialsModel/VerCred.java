package VerifiableCredentialsModel;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.HashMap;

import VerifiableCredentialsModel.protocol.base.CredentialWrapper;
import VerifiableCredentialsModel.protocol.base.IdPrivateKey;
import VerifiableCredentialsModel.protocol.request.CreateCredentialArgs;
import VerifiableCredentialsModel.protocol.response.ResponseData;
import VerifiableCredentialsModel.service.CredentialServiceImpl;

public class VerCred {

	public static void main(String[] args) throws Exception {
		//Issuer cryptomaterial generation
		ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
		KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
		g.initialize(ecSpec, new SecureRandom());
		KeyPair keypair = g.generateKeyPair();
		PublicKey publicKey = keypair.getPublic();
		PrivateKey privateKey = keypair.getPrivate();
		
		String issuerPublicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
		String issuerPrivateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
		
		//Prover cryptomaterial generation
		ECGenParameterSpec ecSpecP = new ECGenParameterSpec("secp256k1");
		KeyPairGenerator gP = KeyPairGenerator.getInstance("EC");
		gP.initialize(ecSpecP, new SecureRandom());
		KeyPair keypairP = gP.generateKeyPair();
		PublicKey publicKeyP = keypairP.getPublic();
		PrivateKey privateKeyP = keypairP.getPrivate();
		
		String proverPublicKeyString = Base64.getEncoder().encodeToString(publicKeyP.getEncoded());
		String proverPrivateKeyString = Base64.getEncoder().encodeToString(privateKeyP.getEncoded());
		
		//Issuer Operation
		HashMap<String, Object> cptJsonSchemaData = new HashMap<String, Object>();
        cptJsonSchemaData.put("name", "Satoshi");
        cptJsonSchemaData.put("gender", "M");
        cptJsonSchemaData.put("age", 32);
        cptJsonSchemaData.put("licence_info", "Non commercial");
        cptJsonSchemaData.put("id", proverPublicKeyString);
        
        IdPrivateKey idPrivateKey = new IdPrivateKey(issuerPrivateKeyString);

        CreateCredentialArgs createCredentialArgs = new CreateCredentialArgs();
        
        createCredentialArgs.setIssuer(issuerPublicKeyString);      
        createCredentialArgs.setIssuanceDate(System.currentTimeMillis());
        createCredentialArgs.setExpirationDate(System.currentTimeMillis() + 100000000000L);      
        createCredentialArgs.setIdPrivateKey(idPrivateKey);
        createCredentialArgs.setClaim(cptJsonSchemaData);
        
        createCredentialArgs.setCptId(Integer.valueOf(1001));
       
        CredentialServiceImpl credentialServiceImpl = new CredentialServiceImpl();
        
        ResponseData<CredentialWrapper> response =
        		credentialServiceImpl.createCredential(createCredentialArgs);               
        ResponseData<String> credentialJsonDataIsuer = credentialServiceImpl.
        		getCredentialJson(response.getResult().getCredential());
        
        System.out.println("Created credential by Issuer: ");
        System.out.println(credentialJsonDataIsuer.getResult());

	}

}
