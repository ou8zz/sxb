package com.sxb.util;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.Arrays;

import com.sxb.constant.AccountType;
import com.sxb.qcloud.Utilities.Json.JSONObject;

/**
 * @description 腾讯云登录校验TLS签名工具类
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/07/22
 * @version 1.0
 */
public class TLSSigature {
	public static class GenTLSSignatureResult
	{
		public String errMessage;
		public String urlSig;
		public int expireTime;
		public int initTime;
		public GenTLSSignatureResult()
		{
			errMessage = "";
			urlSig = "";
		}
	}

	public static class CheckTLSSignatureResult
	{
		public String errMessage;
		public boolean verifyResult;
		public int expireTime;
		public int initTime;
		public CheckTLSSignatureResult()
		{
			errMessage = "";
			verifyResult = false;
		}
	}

	public static void main(String[] args) {
		try{			
			//Use pemfile keys to test
		    String privStr = "-----BEGIN PRIVATE KEY-----\n" +
			"MIGEAgEAMBAGByqGSM49AgEGBSuBBAAKBG0wawIBAQQgiBPYMVTjspLfqoq46oZd\n" +
			"j9A0C8p7aK3Fi6/4zLugCkehRANCAATU49QhsAEVfIVJUmB6SpUC6BPaku1g/dzn\n" +
			"0Nl7iIY7W7g2FoANWnoF51eEUb6lcZ3gzfgg8VFGTpJriwHQWf5T\n" +
			"-----END PRIVATE KEY-----";
		    
			//change public pem string to public string
			String pubStr = "-----BEGIN PUBLIC KEY-----\n"+
			"MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAE1OPUIbABFXyFSVJgekqVAugT2pLtYP3c\n"+
			"59DZe4iGO1u4NhaADVp6BedXhFG+pXGd4M34IPFRRk6Sa4sB0Fn+Uw==\n"+
			"-----END PUBLIC KEY-----";
			
			
            // generate signature
            GenTLSSignatureResult result = GenTLSSignatureEx(1400008806, "86-15651780625", privStr);
            if (0 == result.urlSig.length()) {
                System.out.println("GenTLSSignatureEx failed: " + result.errMessage);
                return;
            }
            
            System.out.println("---\ngenerate sig:\n" + result.urlSig + "\n---\n");
		    	
            CheckTLSSignatureResult checkTLSSignatureEx = CheckTLSSignatureEx(result.urlSig, "1400008806", 1400008806, "86-15651780625", "0", pubStr);
            System.out.println("\n---\ncheck sig ok -- expire time " + checkTLSSignatureEx.expireTime + " -- init time " + checkTLSSignatureEx.initTime + "\n---\n");
            
            
//            {"sig":"eJxlkFFPgzAQx9-5FAQfMdrSAsUnnejCNhIW0MheCJZuaxTalc7AjN9dxCWS*Hq-393-7j4N0zStbJVelZSKY6ML3UtmmTemBazLPyglr4pSF0hV-yDrJFesKLeaqRE6buAAMFV4xRrNt-ws3A61C4gxhC4MfOw4iCDsB5OGtnorxtDRh3gYBwgB3lThuxHGD-l9tA4PXS0i3KVR0GTP*zbs8nUm3Edcp70Ms17M3u0qmW1UQnfR-m4xl*W1IOA4d2mcL5b25gmdDis-pmz5kti056CXVEE7fp1Eal6z80IeIcjDCE-oB1MtF83vC8BwGoTBz96W8WV8Ax2XYGM_", "userphone":"@TLS#144115197422383479"}
            String sig =  "eJxlkEFPg0AQhe-8CoJHjdmFBRaTJiIBq7TRQjXpiawwNNMKrLC1GON-F7GJJCZzmu-NvDfzqem6bqwX6aXI8*ZQq0x9SDD0K90gxsUflBKLTKjMaot-EHqJLWSiVNCO0LQ9k5CpBAuoFZZ4ElwPvTPKGKU29VxmWnQoZk0GumKfjaajnrJhHeGcOFMJbke4DFfBXZDf*se5CnEbxWV7Q6WTpA87gF0gXhY0wkdZxfcpPPt26KOfVDl3m*RV7HEOHjkuBXt66zZqA26-Pi-NSh3iPoog5qvZbGKpsIJTIIdzz7QdNqHv0HbY1L8vIMNplHo-uQ3tS-sG4H1gIg__";
            String userphone = "@TLS#144115197423123143";
            
//            {"sig":"eJx1jkFrgzAYhu-*iuB5G4k2iRZ6KCNbXbuKKJbtEpxGCa02jalVxv77RArzsu-6PHzP*20BAOxkFz9leX6*NoabQQkbLIG9wJTaD39cKVnwzHBXFxNHCzie50Eys0SvpBY8K43Qk*Vg3xm1mSIL0RhZyrvgkUeECUZ0fOTgmdcWRz5F-6*1sprgO4ueg02GDzlhSdheal1u6r3XVWlOun6A3SlhL8j1-Q8dxTT-Wst1E4hdyFL62dfb1-3tLd1eKkxu1wGGZyZamKIgjvxjqk7VajVLGlmL*yAyrsHEpbb1Y-0CwKZYcw__", "userphone":"86-15651780625"}
            sig = "eJx1jkFrgzAYhu-*iuB5G4k2iRZ6KCNbXbuKKJbtEpxGCa02jalVxv77RArzsu-6PHzP*20BAOxkFz9leX6*NoabQQkbLIG9wJTaD39cKVnwzHBXFxNHCzie50Eys0SvpBY8K43Qk*Vg3xm1mSIL0RhZyrvgkUeECUZ0fOTgmdcWRz5F-6*1sprgO4ueg02GDzlhSdheal1u6r3XVWlOun6A3SlhL8j1-Q8dxTT-Wst1E4hdyFL62dfb1-3tLd1eKkxu1wGGZyZamKIgjvxjqk7VajVLGlmL*yAyrsHEpbb1Y-0CwKZYcw__";
            userphone = "86-15651780625";
            
            
            // check signature
            CheckTLSSignatureResult checkResult = CheckTLSSignatureEx(sig, userphone, AccountType.PHONE);
            if(checkResult.verifyResult == false) {
                System.out.println("CheckTLSSignature failed: " + checkResult.errMessage);
                return;
            }
            
            System.out.println("\n---\ncheck sig ok -- expire time " + checkResult.expireTime + " -- init time " + checkResult.initTime + "\n---\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证用户是否有效
	 * @param sig			签名token
	 * @param userphone		用户账号
	 * @param atype			账号类型 目前区分为俩类：PHONE验证类型和第三方验证类型的方式不同
	 * @return
	 * @throws NumberFormatException 
	 * @throws DataFormatException
	 */
	public static CheckTLSSignatureResult CheckTLSSignatureEx(String sig, String userphone, AccountType atype) throws NumberFormatException, DataFormatException {
		String appid_at_3rd = "0";
		String accountType = "0";
		String sdkAppId = ResourceUtil.getConf("tls.sign.appId");
		String pubStr = ResourceUtil.getConf("tls.sign.publicKey");
		if(AccountType.PHONE.equals(atype)) {
			userphone = "86-" + userphone;
			appid_at_3rd = sdkAppId;
			accountType = ResourceUtil.getConf("tls.sign.accountType");
			pubStr = ResourceUtil.getConf("tls.sign.publicKey");
		}
		CheckTLSSignatureResult checkTLSSignatureEx = CheckTLSSignatureEx(sig, appid_at_3rd, Long.parseLong(sdkAppId), userphone, accountType, pubStr);
		return checkTLSSignatureEx;
	}

	/**
	 * @brief 生成 tls 票据
	 * @param expire 有效期，单位是秒，推荐一个月
	 * @param strAppid3rd 填写与 sdkAppid 一致字符串形式的值
	 * @param skdAppid 应用的 appid
	 * @param identifier 用户 id
	 * @param accountType 创建应用后在配置页面上展示的 acctype
	 * @param privStr 生成 tls 票据使用的私钥内容
	 * @return 如果出错，GenTLSSignatureResult 中的 urlSig为空，errMsg 为出错信息，成功返回有效的票据
	 * @throws IOException
	 */
	@Deprecated
	public static GenTLSSignatureResult GenTLSSignature(long expire, 
			String strAppid3rd, long skdAppid, 
			String identifier, long accountType, 
			String privStr ) throws IOException
	{

		GenTLSSignatureResult result = new GenTLSSignatureResult();
		
        Security.addProvider(new BouncyCastleProvider());
        Reader reader = new CharArrayReader(privStr.toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PEMParser parser = new PEMParser(reader);
        Object obj = parser.readObject();
        parser.close();
    	PrivateKey privKeyStruct = converter.getPrivateKey((PrivateKeyInfo) obj);
		
		//Create Json string and serialization String 
		String jsonString = "{" 
		+ "\"TLS.account_type\":\"" + accountType +"\","
		+"\"TLS.identifier\":\"" + identifier +"\","
		+"\"TLS.appid_at_3rd\":\"" + strAppid3rd +"\","
	    +"\"TLS.sdk_appid\":\"" + skdAppid +"\","
		+"\"TLS.expire_after\":\"" + expire +"\""
		+"}";
		//System.out.println("#jsonString : \n" + jsonString);
		
		String time = String.valueOf(System.currentTimeMillis()/1000);
		String SerialString = 
			"TLS.appid_at_3rd:" + strAppid3rd + "\n" +
			"TLS.account_type:" + accountType + "\n" +
			"TLS.identifier:" + identifier + "\n" + 
			"TLS.sdk_appid:" + skdAppid + "\n" + 
			"TLS.time:" + time + "\n" +
			"TLS.expire_after:" + expire +"\n";
	
		
		//System.out.println("#SerialString : \n" + SerialString);
		//System.out.println("#SerialString Hex: \n" + Hex.encodeHexString(SerialString.getBytes()));
		
		try{
			//Create Signature by SerialString
			Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
			signature.initSign(privKeyStruct);
			signature.update(SerialString.getBytes(Charset.forName("UTF-8")));
			byte[] signatureBytes = signature.sign();
			
			String sigTLS = Base64.encodeBase64String(signatureBytes);
			//System.out.println("#sigTLS : " + sigTLS);
			
			//Add TlsSig to jsonString
		    JSONObject jsonObject= new JSONObject(jsonString);
		    jsonObject.put("TLS.sig", (Object)sigTLS);
		    jsonObject.put("TLS.time", (Object)time);
		    jsonString = jsonObject.toString();
		    
		   // System.out.println("#jsonString : \n" + jsonString);
		    
		    //compression
		    Deflater compresser = new Deflater();
		    compresser.setInput(jsonString.getBytes(Charset.forName("UTF-8")));

		    compresser.finish();
		    byte [] compressBytes = new byte [512];
		    int compressBytesLength = compresser.deflate(compressBytes);
		    compresser.end();
		    //System.out.println("#compressBytes "+ compressBytesLength+": " + Hex.encodeHexString(Arrays.copyOfRange(compressBytes,0,compressBytesLength)));

		    //String userSig = Base64.encodeBase64URLSafeString(Arrays.copyOfRange(compressBytes,0,compressBytesLength));
		    String userSig = new String(base64_url.base64EncodeUrl(Arrays.copyOfRange(compressBytes,0,compressBytesLength)));
    
		    result.urlSig = userSig;
		    //System.out.println("urlSig: "+ userSig);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result.errMessage = "generate usersig failed";
		}
		
		return result;
	}

	/**
	 * @brief 校验 tls 票据
	 * @param urlSig 返回 tls 票据
	 * @param strAppid3rd 填写与 sdkAppid 一致的字符串形式的值
	 * @param skdAppid 应的 appid
	 * @param identifier 用户 id
	 * @param accountType 创建应用后在配置页面上展示的 acctype
	 * @param publicKey 用于校验 tls 票据的公钥内容，但是需要先将公钥文件转换为 java 原生 api 使用的格式，下面是推荐的命令
	 *         openssl pkcs8 -topk8 -in ec_key.pem -outform PEM -out p8_priv.pem -nocrypt
	 * @return 如果出错 CheckTLSSignatureResult 中的 verifyResult 为 false，错误信息在 errMsg，校验成功为 true
	 * @throws DataFormatException
	 */
	@Deprecated
	public static CheckTLSSignatureResult CheckTLSSignature( String urlSig,
			String strAppid3rd, long skdAppid, 
			String identifier, long accountType, 
			String publicKey ) throws DataFormatException
	{
		CheckTLSSignatureResult result = new CheckTLSSignatureResult();	
        Security.addProvider(new BouncyCastleProvider());
		
		//DeBaseUrl64 urlSig to json
		Base64 decoder = new Base64();

		//byte [] compressBytes = decoder.decode(urlSig.getBytes());
		byte [] compressBytes = base64_url.base64DecodeUrl(urlSig.getBytes(Charset.forName("UTF-8")));
		
		//System.out.println("#compressBytes Passing in[" + compressBytes.length + "] " + Hex.encodeHexString(compressBytes));
	
		//Decompression
		Inflater decompression =  new Inflater();
		decompression.setInput(compressBytes, 0, compressBytes.length);
		byte [] decompressBytes = new byte [1024];
		int decompressLength = decompression.inflate(decompressBytes);
		decompression.end();
		
		String jsonString = new String(Arrays.copyOfRange(decompressBytes, 0, decompressLength));
		
		//System.out.println("#Json String passing in : \n" + jsonString);
		
		//Get TLS.Sig from json
		JSONObject jsonObject= new JSONObject(jsonString);
		String sigTLS = jsonObject.getString("TLS.sig");
		
		//debase64 TLS.Sig to get serailString
		byte[] signatureBytes = decoder.decode(sigTLS.getBytes(Charset.forName("UTF-8")));
		
		try{
			
			String sigTime = jsonObject.getString("TLS.time");
			String sigExpire = jsonObject.getString("TLS.expire_after");
			
			//checkTime
			//System.out.println("#time check: "+ System.currentTimeMillis()/1000 + "-" 
					//+ Long.parseLong(sigTime) + "-" + Long.parseLong(sigExpire));
			if( System.currentTimeMillis()/1000 - Long.parseLong(sigTime) > Long.parseLong(sigExpire))
			{
				result.errMessage = new String("TLS sig is out of date ");
				System.out.println("Timeout");
				return result;
			}
			
			//Get Serial String from json
			String SerialString = 
				"TLS.appid_at_3rd:" + strAppid3rd + "\n" +
				"TLS.account_type:" + accountType + "\n" +
				"TLS.identifier:" + identifier + "\n" + 
				"TLS.sdk_appid:" + skdAppid + "\n" + 
				"TLS.time:" + sigTime + "\n" + 
				"TLS.expire_after:" + sigExpire + "\n";
		
			//System.out.println("#SerialString : \n" + SerialString);
		
	        Reader reader = new CharArrayReader(publicKey.toCharArray());
	        PEMParser  parser = new PEMParser(reader);
	        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
	        Object obj = parser.readObject();
	        parser.close();
	        PublicKey pubKeyStruct  = converter.getPublicKey((SubjectPublicKeyInfo) obj);

			Signature signature = Signature.getInstance("SHA256withECDSA","BC");
			signature.initVerify(pubKeyStruct);
			signature.update(SerialString.getBytes(Charset.forName("UTF-8")));
			boolean bool = signature.verify(signatureBytes);
			//System.out.println("#jdk ecdsa verify : " + bool);
			result.verifyResult = bool;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result.errMessage = "Failed in checking sig";
		}
		
		return result;
	}

	/**
	 * @brief 生成 tls 票据，精简参数列表，有效期默认为 180 天
	 * @param skdAppid 应用的 sdkappid
	 * @param identifier 用户 id
	 * @param privStr 私钥文件内容
	 * @return
	 * @throws IOException
	 */
	public static GenTLSSignatureResult GenTLSSignatureEx(
			long skdAppid,
			String identifier,
			String privStr) throws IOException {
		return GenTLSSignatureEx(skdAppid, identifier, privStr, 3600*24*180);
	}

	/**
	 * @brief 生成 tls 票据，精简参数列表
	 * @param skdAppid 应用的 sdkappid
	 * @param identifier 用户 id
	 * @param privStr 私钥文件内容
	 * @param expire 有效期，以秒为单位，推荐时长一个月
	 * @return
	 * @throws IOException
	 */
	public static GenTLSSignatureResult GenTLSSignatureEx(
			long skdAppid,
			String identifier,
			String privStr,
			long expire) throws IOException {

		GenTLSSignatureResult result = new GenTLSSignatureResult();
		
        Security.addProvider(new BouncyCastleProvider());
        Reader reader = new CharArrayReader(privStr.toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PEMParser parser = new PEMParser(reader);
        Object obj = parser.readObject();
        parser.close();
    	PrivateKey privKeyStruct = converter.getPrivateKey((PrivateKeyInfo) obj);
		
		String jsonString = "{" 
		+ "\"TLS.account_type\":\"" + 0 +"\","
		+"\"TLS.identifier\":\"" + identifier +"\","
		+"\"TLS.appid_at_3rd\":\"" + 0 +"\","
	    +"\"TLS.sdk_appid\":\"" + skdAppid +"\","
		+"\"TLS.expire_after\":\"" + expire +"\","
        +"\"TLS.version\": \"201512300000\""
		+"}";
		
		String time = String.valueOf(System.currentTimeMillis()/1000);
		String SerialString = 
			"TLS.appid_at_3rd:" + 0 + "\n" +
			"TLS.account_type:" + 0 + "\n" +
			"TLS.identifier:" + identifier + "\n" + 
			"TLS.sdk_appid:" + skdAppid + "\n" + 
			"TLS.time:" + time + "\n" +
			"TLS.expire_after:" + expire +"\n";
		
		try {
			//Create Signature by SerialString
			Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
			signature.initSign(privKeyStruct);
			signature.update(SerialString.getBytes(Charset.forName("UTF-8")));
			byte[] signatureBytes = signature.sign();
			
			String sigTLS = Base64.encodeBase64String(signatureBytes);
			
			//Add TlsSig to jsonString
		    JSONObject jsonObject= new JSONObject(jsonString);
		    jsonObject.put("TLS.sig", (Object)sigTLS);
		    jsonObject.put("TLS.time", (Object)time);
		    jsonString = jsonObject.toString();
		    
		    //compression
		    Deflater compresser = new Deflater();
		    compresser.setInput(jsonString.getBytes(Charset.forName("UTF-8")));

		    compresser.finish();
		    byte [] compressBytes = new byte [512];
		    int compressBytesLength = compresser.deflate(compressBytes);
		    compresser.end();
		    String userSig = new String(base64_url.base64EncodeUrl(Arrays.copyOfRange(compressBytes,0,compressBytesLength)));
    
		    result.urlSig = userSig;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result.errMessage = "generate usersig failed";
		}
		
		return result;
	}
	
	public static CheckTLSSignatureResult CheckTLSSignatureEx(
			String urlSig,
			String appid_at_3rd,
			long sdkAppid, 
			String identifier, 
			String accountType,
			String publicKey ) throws DataFormatException {

		CheckTLSSignatureResult result = new CheckTLSSignatureResult();	
        Security.addProvider(new BouncyCastleProvider());
		
		//DeBaseUrl64 urlSig to json
		Base64 decoder = new Base64();

		byte [] compressBytes = base64_url.base64DecodeUrl(urlSig.getBytes(Charset.forName("UTF-8")));
		
		//Decompression
		Inflater decompression =  new Inflater();
		decompression.setInput(compressBytes, 0, compressBytes.length);
		byte [] decompressBytes = new byte [1024];
		int decompressLength = decompression.inflate(decompressBytes);
		decompression.end();
		
		String jsonString = new String(Arrays.copyOfRange(decompressBytes, 0, decompressLength));
		
		//Get TLS.Sig from json
		JSONObject jsonObject= new JSONObject(jsonString);
		String sigTLS = jsonObject.getString("TLS.sig");
		
		//debase64 TLS.Sig to get serailString
		byte[] signatureBytes = decoder.decode(sigTLS.getBytes(Charset.forName("UTF-8")));
		
		try {
			String strSdkAppid = jsonObject.getString("TLS.sdk_appid");
			String sigTime = jsonObject.getString("TLS.time");
			String sigExpire = jsonObject.getString("TLS.expire_after");
			
			if (Integer.parseInt(strSdkAppid) != sdkAppid)
			{
				result.errMessage = new String(	"sdkappid "
						+ strSdkAppid
						+ " in tls sig not equal sdkappid "
						+ sdkAppid
						+ " in request");
				return result;
			}

			if ( System.currentTimeMillis()/1000 - Long.parseLong(sigTime) > Long.parseLong(sigExpire)) {
				result.errMessage = new String("TLS sig is out of date");
				return result;
			}
			
			//Get Serial String from json
			String SerialString = 
				"TLS.appid_at_3rd:" + appid_at_3rd + "\n" +
				"TLS.account_type:" + accountType + "\n" +
				"TLS.identifier:" + identifier + "\n" + 
				"TLS.sdk_appid:" + sdkAppid + "\n" + 
				"TLS.time:" + sigTime + "\n" + 
				"TLS.expire_after:" + sigExpire + "\n";
		
	        Reader reader = new CharArrayReader(publicKey.toCharArray());
	        PEMParser  parser = new PEMParser(reader);
	        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
	        Object obj = parser.readObject();
	        parser.close();
	        PublicKey pubKeyStruct  = converter.getPublicKey((SubjectPublicKeyInfo) obj);

			Signature signature = Signature.getInstance("SHA256withECDSA","BC");
			signature.initVerify(pubKeyStruct);
			signature.update(SerialString.getBytes(Charset.forName("UTF-8")));
			boolean bool = signature.verify(signatureBytes);
            result.expireTime = Integer.parseInt(sigExpire);
            result.initTime = Integer.parseInt(sigTime);
			result.verifyResult = bool;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result.errMessage = "Failed in checking sig";
		}
		
		return result;
	}

}
