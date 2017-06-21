package org.sysuboys.diaryu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author origin from cevencheng ({@linkplain l1
 *         http://blog.csdn.net/liang0000zai/article/details/51493744}) updated
 *         by Sine
 */
public class SecurityUtil {

	private static final int SESSION_ID_BYTES = 16;

	public static String Md5Hash(String string, String salt) {
		MessageDigest md = getDigest(); // 取摘要,默认是"MD5"算法
		byte[] bytes = (string + salt).getBytes();
		String hexadecimal = byteToHexadecimal(md.digest(bytes));
		return hexadecimal;
	}

	/**
	 * 
	 * @return random String of hexadecimal digits of 32 characters (from MD5)
	 */
	public static synchronized String generate32() {
		Random random = getRandom(); // 取随机数发生器, 默认是SecureRandom
		MessageDigest md = getDigest(); // 取摘要,默认是"MD5"算法

		byte bytes[] = new byte[SESSION_ID_BYTES];
		random.nextBytes(bytes);
		String hexadecimal = byteToHexadecimal(md.digest(bytes));
		return hexadecimal;
	}

	// 转化为16进制字符串
	public static String byteToHexadecimal(byte[] bytes) {
		StringBuffer result = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
			byte b2 = (byte) (bytes[i] & 0x0f);
			if (b1 < 10)
				result.append((char) ('0' + b1));
			else
				result.append((char) ('A' + (b1 - 10)));
			if (b2 < 10)
				result.append((char) ('0' + b2));
			else
				result.append((char) ('A' + (b2 - 10)));
		}
		return result.toString();

	}

	private static Random getRandom() {
		return new SecureRandom();
	}

	private static MessageDigest getDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		System.out.println("will generate  forever, press enter to continue");
		while (true)
			System.out.println(SecurityUtil.generate32());
	}

}