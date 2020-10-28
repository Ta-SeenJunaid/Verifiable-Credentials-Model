package VerifiableCredentialsModel;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

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
		// TODO Auto-generated method stub

	}

}
