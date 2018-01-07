package com.kv.des3;

public class ByteUtil {

	static byte[] mask = new byte[128];

	static {
		initMask();
	}
	static byte[] asciiMask = new byte[] { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static void initMask() { // init mask
		for (int i = 0; i <= 9; i++) {
			mask[i + 48] = (byte) i;
		}
		for (int i = 0; i <= 5; i++) {
			mask[i + 97] = (byte) (10 + i);
		}
		for (int i = 0; i <= 5; i++) {
			mask[i + 65] = (byte) (10 + i);
		}
	}

	public static boolean[] byte2Bit(int b) {
		boolean[] flags = new boolean[8];
		for (int i = 0; i < 8; i++) {
			flags[i] = (((b >> (8 - i - 1)) & 0x01) == 1);
		}
		return flags;
	}

	public static byte[] intToBCD(int n, int balen) {
		byte[] ret = new byte[balen];
		int tmp;
		for (int i = 1; i <= balen; i++) {
			tmp = n % 100;
			ret[balen - i] = (byte) (tmp / 10 * 16 + tmp % 10);

			n -= tmp;
			if (n == 0) {
				break;
			}
			n /= 100;
		}
		return ret;
	}

	public static int bcdToInt(byte[] ba, int idx, int len) {
		int jinwei = len * 2;
		int ret = 0;
		int temp = 0;
		int pow;
		int posNum; //
		for (int i = 0, n = len; i < n; i++) {
			pow = pow(10, (jinwei - 1));
			posNum = ba[idx + i] >= 0 ? ba[idx + i] : ba[idx + i] + 256;
			temp = (posNum / 16) * pow + posNum % 16 * pow / 10;
			ret += temp;
			jinwei -= 2;
		}
		return ret;
	}

	public static int pow(int x, int y) {
		int n = x;
		for (int i = 1; i < y; i++) {
			n *= x;
		}
		return n;
	}

	public static byte[] hexStr2Byte(String hex) {
		hex += hex.length() % 2 != 0 ? "f" : "";
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	public static byte[] hexStr2Byte(String hex, int len) {
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	public static int revAsciiHexToInt(byte[] ah) {
		int ret = 0;
		for (int i = 0; i < ah.length / 2; i++) {
			int hex = (mask[ah[i * 2]] << 4) + (mask[ah[i * 2 + 1]]);
			ret += (hex << (8 * i));
		}
		return ret;
	}

	public static int revHexToInt(byte[] data, int off, int len) {
		int ret = 0;
		for (int i = 0; i < len; i++) {
			ret += (data[off + i] & 0xff) << (8 * i);
		}

		return ret;
	}

	public static int revAsciiHexToInt(byte[] ah, int off, int len) {
		int ret = 0;
		for (int i = 0, n = len / 2; i < n; i++) {
			int hex = (mask[ah[i * 2 + off]] << 4)
					+ (mask[ah[i * 2 + off + 1]]);
			ret += (hex << (8 * i));
		}
		return ret;
	}

	public static byte[] intToRevAsciiHex(int value, int hexlen) {
		byte[] ret = new byte[hexlen * 2];

		for (int i = 0; i < hexlen; i++) {
			if (value > 0) {
				ret[i * 2] = asciiMask[((value & 0xf0) >> 4)];
				ret[i * 2 + 1] = asciiMask[(value & 0xf)];
				value >>= 8;
			} else {
				ret[i * 2] = '0';
				ret[i * 2 + 1] = '0';
			}
		}
		return ret;
	}

	public static String intToRevHexString(int value, int hexlen) {

		byte[] ret = new byte[hexlen];
		for (int i = 0; i < hexlen; i++) {
			ret[i] = (byte) (value & 0xff);
			value >>= 8;
			if (value == 0)
				break;
		}
		return hexToStr(ret);
	}

	public static byte[] intToAsciiHex(int value, int len) {
		byte[] ret = new byte[len * 2];
		for (int i = 0; i < len; i++) {
			ret[i * 2] = asciiMask[((value & 0xf0) >> 4)];
			ret[i * 2 + 1] = asciiMask[value & 0xf];
			value >>= 8;
		}
		return ret;
	}

	public static byte[] intToHex(int value, int len) {
		byte[] ret = new byte[len];
		for (int i = 0; i < len; i++) {
			ret[i] = (byte) ((value >> 8 * (len - i - 1)) & 0xff);
			// ret[i] = (byte) (value&0xff);
			// if(ret[i]==0)
			// ret[i]=(byte) 0xff;
			// value>>=8;
		}
		return ret;
	}

	public static byte[] intToRevHex(int value, int len) {
		byte[] ret = new byte[len];
		for (int i = 0; i < len; i++) {
			ret[i] = (byte) (value & 0xff);
			// if(ret[i]==0)
			// ret[i]=(byte) 0xff;
			value >>= 8;
		}
		return ret;
	}

	public static int hexToInt(byte[] buf, int idx, int len) {
		return Integer.parseInt(hexToStr(buf, idx, len), 16);
	}

	public static int hexToInt(byte[] buf) {
		return Integer.parseInt(hexToStr(buf), 16);
	}

	public static String hexToStr(byte[] buf) {
		if (buf == null) {
			return "";
		}
		return hexToStr(buf, 0, buf.length);
	}

	public static String hexToStr(byte[] buf, int idx, int len) {
		StringBuffer sb = new StringBuffer();
		int n;
		for (int i = 0; i < len; i++) {
			n = buf[i + idx] & 0xff;
			if (n < 0x10) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(n));
		}

		return sb.toString();
	}

	private static byte toByte(char c) {
		return mask[c];
	}

	/**
	 *
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String addToByte(String str, int len) {
		StringBuffer buffer = new StringBuffer().append(str);
		String retS = "";
		if (str != null && len != 0) {
			int lastlen = str.length() % len;
			int addlen = len - lastlen;

			for (int i = 0; i < addlen; i++) {
				buffer.append("0");
			}
		}
		retS = new String(buffer);

		return retS;
	}

}
