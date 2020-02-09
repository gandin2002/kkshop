package cn.bohoon.wx.miniapp.service;

import java.security.AlgorithmParameters;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AesCbcUtil {

	public static boolean initialized = false;  
	
	
	/**
	 * AES解密
	 *
	 * @param data
	 *            //密文，被加密的数据
	 * @param key
	 *            //秘钥
	 * @param iv
	 *            //偏移量
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String key, String iv) throws Exception {
		 initialize();

		// 被加密的数据
		byte[] dataByte = Base64.decodeBase64(data);
		// 加密秘钥
		byte[] keyByte = Base64.decodeBase64(key);
		// 偏移量
		byte[] ivByte = Base64.decodeBase64(iv);

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));

			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				return result;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void initialize(){  
        if (initialized) return;  
        Security.addProvider(new BouncyCastleProvider());  
        initialized = true;  
    }
	
	
}
