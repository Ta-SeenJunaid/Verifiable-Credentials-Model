package VerifiableCredentialsModel.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.fge.jackson.JsonLoader;

import VerifiableCredentialsModel.exception.DataTypeCastException;


public class DataToolUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(DataToolUtils.class);
    
    private static final String KEY_FROM_TOJSON = "$from";
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    private static final int SERIALIZED_SIGNATUREDATA_LENGTH = 65;
    

    public static boolean isValidBase64String(String string) {
        return org.apache.commons.codec.binary.Base64.isBase64(string);
    }
    
    public static JsonNode laodJsonObject(String jsonString) throws IOException {
    	return JsonLoader.fromString(jsonString);
    }
    
    public static boolean isValidFromJson(String json) {
    	if(StringUtils.isBlank(json)) {
    		logger.error("input json param is null.");
    		return false;
    	}
    	JsonNode jsonObject = null;
    	try {
    		jsonObject = laodJsonObject(json);
    	} catch (IOException e) {
    		logger.error("convert jsonString to JSONObject failed." +e);
    		return false;
    	}
    	return jsonObject.has(KEY_FROM_TOJSON);
    }
    
    public static <T> T deserialize(String json, Class<T> clazz) {
    	Object object = null;
    	try {
    		if (isValidFromJson(json)) {
    			logger.error("this jsonString is converted by toJson(), "
    					+ "please use fromJson() to deserialize it");
    			throw new DataTypeCastException("deserialize json to Object error");
    		}
    		object = OBJECT_MAPPER.readValue(json, TypeFactory.rawClass(clazz));
    	} catch (JsonProcessingException e) {
    		logger.error("JsonParseException when deserialize json to object", e);
    		throw new DataTypeCastException(e);
    	} catch(IOException e) {
    		logger.error("IOException when deserialize json to object", e);
    		throw new DataTypeCastException(e);
    	}
    	
    	return (T) object;
    }
    
    public static <T> String serialize(T object) {
    	Writer write = new StringWriter();
    	try {
    		OBJECT_MAPPER.writeValue(write, object);
    	} catch (JsonGenerationException e) {
    		logger.error("JsonGenerationException when serialize object to json", e);
    	} catch (JsonMappingException e) {
    		logger.error("JsonMappingExecption when serialize object to json",e );
    	} catch (IOException e) {
    		logger.error("IOException when serialze onject to json", e);
    	}
    	return write.toString();
    }
    
    public static Map<String, Object> objToMap(Object object) throws Exception {
    	JsonNode jsonNode = OBJECT_MAPPER.readTree(serialize(object));
    	return (HashMap<String, Object>) OBJECT_MAPPER.convertValue(jsonNode, HashMap.class);
    }
    
    public static String sha256(String  utfString) {
    	return DigestUtils.sha256Hex(utfString);
    }
    
    public static String mapToCompactJson(Map<String, Object> map) throws Exception {
        return OBJECT_MAPPER.readTree(serialize(map)).toString();
    }
    
    public static String secp256k1Sign(String rawData, String privateKeyString) 
    		throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
    		SignatureException, UnsupportedEncodingException {
    	
    	EncodedKeySpec privateKeySpec = 
				new PKCS8EncodedKeySpec(Base64.getDecoder()
						.decode(privateKeyString));
    	
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		
		
		Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
		ecdsaSign.initSign(privateKey);
		ecdsaSign.update(rawData.getBytes("UTF-8"));
		byte[] signature = ecdsaSign.sign();
		
		return Base64.getEncoder().encodeToString(signature);	
    }

    public static boolean verifySecp256k1Signature(
    		String rawData,
    		String signature,
    		String publickeyString) throws NoSuchAlgorithmException, 
    InvalidKeySpecException, InvalidKeyException, 
    SignatureException, UnsupportedEncodingException {
    	

		EncodedKeySpec publicKeySpec = 
				new X509EncodedKeySpec(Base64.getDecoder()
						.decode(publickeyString));
		
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		
		
		Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
		KeyFactory kf = KeyFactory.getInstance("EC");
		
		ecdsaVerify.initVerify(publicKey);
		ecdsaVerify.update(rawData.getBytes("UTF-8"));
		
		return ecdsaVerify.verify(Base64.getDecoder().decode(signature)); 	
    }

}
