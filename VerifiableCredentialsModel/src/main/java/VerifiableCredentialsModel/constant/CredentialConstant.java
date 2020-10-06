package VerifiableCredentialsModel.constant;

public class CredentialConstant {
	
	public static final String DEFAULT_CREDENDTIAL_CONTEXT =
			"https://www.w3.org/2018/credentials/v1";
	
    public static final String 
    CREDENTIAL_CONTEXT_PORTABLE_JSON_FIELD = "@context";


    public static enum CredentialProofType {
        ECDSA("Secp256k1");


        private String typeName;


        CredentialProofType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }
}
