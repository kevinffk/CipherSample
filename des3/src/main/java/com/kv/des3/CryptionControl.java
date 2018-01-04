package com.kv.des3;


import android.util.Log;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;




/**
 * 加解密命令,此类为单例类
 */
public class CryptionControl {
	public final static String DES_TYPE = "DES/ECB/NoPadding";//DES加密类型
	private static final String TAG = CryptionControl.class.getSimpleName();
	private static CryptionControl instance = new CryptionControl();
	
	public static CryptionControl getInstance(){
		return instance;
	}
	
	/**
	 * @roseuid 4DF71C3B029F
	 */
	private CryptionControl() {
	}


	
	/**
	 * 
	 * 3倍长密钥加密
	 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P))) 
	 */
	public byte[] encryptoCBCKey3(byte[] text,byte[] password) {
		if(password.length!=24)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		byte[] key3 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		System.arraycopy(password, 16, key3, 0, 8);
		byte[] result = encryptoCBCKey1(text,key1);
		result = decryptCBCKey1(result,key2);
		result = encryptoCBCKey1(result,key3);
		return result;
	}
	
	/**
	 * 
	 * 3倍长密钥解密
	 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
	 */
	public byte[] decryptCBCKey3(byte[] text,byte[] password) {
		if(password.length!=24)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		byte[] key3 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		System.arraycopy(password, 16, key3, 0, 8);
		byte[] result = decryptCBCKey1(text,key3);
		result = encryptoCBCKey1(result,key2);
		result = decryptCBCKey1(result,key1);
		return result;
	}
	
	/**
	 * 
	 * 双倍长密钥加密
	 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P))) 
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public byte[] encryptoCBCKey2(byte[] text,byte[] password) {
		if(password.length!=16)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		byte[] result = encryptoCBCKey1(text,key1);
		result = decryptCBCKey1(result,key2);
		result = encryptoCBCKey1(result,key1);
		return result;
	}
	
	/**
	 * 
	 * 双倍长密钥解密
	 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public byte[] decryptCBCKey2(byte[] text,byte[] password) {
		if(password.length!=16)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		byte[] result = decryptCBCKey1(text,key1);
		result = encryptoCBCKey1(result,key2);
		result = decryptCBCKey1(result,key1);
		return result;
	}
	
	/**
	 * DES CBC加密
	 * @return String
	 * @roseuid 4DF71E7A02AF
	 */
	public byte[] encryptoCBCKey1(byte[] text,byte[] password) {
		if(password.length!=8)
			return null;
		byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		
		// 构造消息摘要
		int blockNum = result.length / 8;
		byte[] checkBlock = new byte[8];

		System.arraycopy(text, 0, checkBlock, 0, 8);
		byte[] temp = encryptoECB(checkBlock,password);
		System.arraycopy(temp, 0, result, 0, 8);
		
		for (int i = 1; i < blockNum; i++) {
			if(text.length<i * 8+8){
				Arrays.fill(checkBlock, (byte)0x00);
				System.arraycopy(text, i * 8, checkBlock, 0, text.length%8);
			}else{
				System.arraycopy(text, i * 8, checkBlock, 0, 8);
			}
			
			for (int j = 0; j < 8; j++) {
				temp[j] = (byte) (temp[j] ^ checkBlock[j]);
			}
			temp = encryptoECB(temp,password);
			System.arraycopy(temp, 0, result, i*8, 8);
		}
		return result;
	}	
	
	/**
	 * DES CBC解密
	 * @return String
	 * @roseuid 4DF71E7A02AF
	 */
	public byte[] decryptCBCKey1(byte[] text,byte[] password) {
		if(password.length!=8)
			return null;
		byte[] result = new byte[text.length];
		// 构造消息摘要
		int blockNum = text.length / 8;
		
		byte[] checkBlock = new byte[8];
		System.arraycopy(text, 0, checkBlock, 0, 8);
		
		byte[] temp = decryptECB(checkBlock,password);
		System.arraycopy(temp, 0, result, 0, 8);

		for (int i = 1; i < blockNum; i++) {
			System.arraycopy(text, i * 8, checkBlock, 0, 8);
			temp = decryptECB(checkBlock,password);
			
			for (int j = 0; j < 8; j++) {
				temp[j] = (byte) (temp[j] ^ text[(i-1)*8+j]);
			}
			System.arraycopy(temp, 0, result, i*8, 8);
		}
		return result;
	}	
	
	
	/**
	 * 
	 * 3倍长密钥加密
	 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P))) 
	 */
	public byte[] encryptoECBKey3(byte[] text,byte[] password) {
		if(password.length!=24)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		byte[] key3 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		System.arraycopy(password, 16, key3, 0, 8);
		byte[] result = encryptoECB(text,key1);
		result = decryptECB(result,key2);
		result = encryptoECB(result,key3);
		return result;
	}
	
	/**
	 * 
	 * 3倍长密钥解密
	 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
	 */
	public byte[] decryptECBKey3(byte[] text,byte[] password) {
		if(password.length!=24)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		byte[] key3 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		System.arraycopy(password, 16, key3, 0, 8);
		byte[] result = decryptECB(text,key3);
		result = encryptoECB(result,key2);
		result = decryptECB(result,key1);
		return result;
	}
	
	/**
	 * 
	 * 双倍长密钥加密
	 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P))) 
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public byte[] encryptoECBKey2(byte[] text,byte[] password) {
		if(password.length!=16)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		byte[] result = encryptoECB(text,key1);
		result = decryptECB(result,key2);
		result = encryptoECB(result,key1);
		return result;
	}
	
	/**
	 * 
	 * 双倍长密钥解密
	 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
	 * 
	 * K1=K3，但不能K1=K2=K3（如果相等的话就成了DES算法了）
	 */
	public byte[] decryptECBKey2(byte[] text,byte[] password) {
		if(password.length!=16)
			return null;
		//byte[] result = new byte[text.length%8==0?text.length:(text.length/8+1)*8];
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		System.arraycopy(password, 0, key1, 0, 8);
		System.arraycopy(password, 8, key2, 0, 8);
		byte[] result = decryptECB(text,key1);
		result = encryptoECB(result,key2);
		result = decryptECB(result,key1);
		return result;
	}
	
	
	/**
	 * DES加密过程
	 * @param
	 * @param password
	 * @return
	 */
	public byte[] encryptoECB(byte[] src, byte[] password) {
		try {
			//return DESCryption.encryptionECB(datasource, password);
			if(src.length%8>0){//补足8字节整数倍
				byte[] temp = new byte[(src.length/8+1)*8];
				System.arraycopy(src, 0, temp, 0, src.length);
				src = temp;
			}

			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(DES_TYPE);
			// 用密匙初始化Cipher对象 DES/ECB/NoPadding
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(src);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * DES解密过程
	 * @param src
	 * @param password
	 * @return
	 */
	public byte[] decryptECB(byte[] src, byte[] password){
		try {
			//return DESCryption.discryptionECB(src, password);
			if(src.length%8>0){//补足8字节整数倍
				byte[] temp = new byte[(src.length/8+1)*8];
				System.arraycopy(src, 0, temp, 0, src.length);
				src = temp;
			}
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(DES_TYPE);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			// 真正开始解密操作
			return cipher.doFinal(src);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}
	


	
	/**
	 * 加密工作密钥
	 * @param workkeyInfo
	 * @return
	 */
	public byte[] encryptoWorkKey(byte[] rootKey, String workkeyInfo){
		if(workkeyInfo==null){
			return null;
		}
		byte[] workkeyInfoBytes = TypeConversion.hexStringToByte(workkeyInfo);
		if(workkeyInfoBytes.length%8>0){//补足8字节整数倍
			byte[] temp = new byte[(workkeyInfoBytes.length/8+1)*8];
			System.arraycopy(workkeyInfoBytes, 0, temp, 0, workkeyInfoBytes.length);
			workkeyInfoBytes = temp;
		}
		workkeyInfoBytes = CryptionControl.getInstance().encryptoECB(workkeyInfoBytes, rootKey);
		byte[] key = new byte[8];
		if(workkeyInfoBytes.length>8){
			System.arraycopy(workkeyInfoBytes, workkeyInfoBytes.length-8, key,0 , 8);
		}else{
			System.arraycopy(workkeyInfoBytes, 0, key,0, workkeyInfoBytes.length);
		}
		return key;
	}
//	/**
//	 * 加密信息
//	 * @param into
//	 * @return
//	 */
//	private byte[] encryptoInfo(byte[] workKey,String into){
//		return CryptionControl.getInstance().decryptECB(TypeConversion.hexStringToByte(into), workKey);
//	}
	
	public static void main(String[] args){
		/**
		byte[] key = TypeConversion.hexStringToByte("111111111111111122222222222222223333333333333333");
		byte[] source = ("1111111111111111CBCKey1").getBytes(); 
		MyLog.d(TAG,TypeConversion.byte2hex(source,0,source.length));
//		byte[] encodeResult = CryptionControl.getInstance().encryptoECB(source,key);
//		MyLog.d(TAG,"encodeResult:"+TypeConversion.byte2hex(encodeResult));
//		byte[] decodeResult =  CryptionControl.getInstance().decryptECB(encodeResult, key);
//		MyLog.d(TAG,"decodeResult:"+TypeConversion.byte2hex(decodeResult));
		
		byte[] encryptoDesResult = CryptionControl.getInstance().encryptoECBKey3(source,key);
		MyLog.d(TAG,"encryptoDesResult:"+TypeConversion.byte2hex(encryptoDesResult));
		byte[] decryptDesResult = CryptionControl.getInstance().decryptECBKey3(encryptoDesResult,key);
		MyLog.d(TAG,"decryptDesResult:"+TypeConversion.byte2hex(decryptDesResult));
		
		byte[] keypool=TypeConversion.hexStringToByte("D364FB15B07032B592FE0B8608B6C76E");
		byte[] MAK=TypeConversion.hexStringToByte("08E3E55F85D716BD0000000000000000293ECBD0");
		byte[] PIK=TypeConversion.hexStringToByte("1E857463A16D1C570C73CDBFA2003ADC9473B891");

		
		MyLog.d(TAG,"Result1:"+TypeConversion.byte2hex(CryptionControl.instance.decryptECBKey2(MAK, keypool)));
		MyLog.d(TAG,"Result2:"+TypeConversion.byte2hex(CryptionControl.instance.encryptoCBCKey2(CryptionControl.instance.decryptCBCKey2(MAK, keypool), keypool)));
		*/
		byte[] rootKey = TypeConversion.hexStringToByte("DF83647F322E113D");
		byte[] workKey = CryptionControl.instance.encryptoWorkKey(rootKey,"FSK_POS&2012-03-16");
		
		byte[] infos = TypeConversion.hexStringToByte("6EAE1B6231BD928B824F4EF4A8B3ABC1ED9783F3A4E72C2E5582E59BE990CA1EDE22AD864C87E34B033C4EB8B8D1878871607A2ECBB563820528639BA2C95175F18C92CE5EA19C88F7B5BD3B8228A251E48403E439F61B191DF7F1A408DB30E871EBA4E8501A15F092C3E5E4594F1F2D");
		try{
			Log.d(TAG,"LOG Result:"+TypeConversion.asciiToString(CryptionControl.instance.decryptECB(infos, workKey)));
		}catch(Exception e){
			
		}
		
		
		
	}
}
