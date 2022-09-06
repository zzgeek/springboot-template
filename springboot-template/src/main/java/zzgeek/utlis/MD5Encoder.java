package zzgeek.utlis;

import zzgeek.conf.GlobalConfig;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Encoder {

    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final char[] hexadecimal = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] hexadecimalH = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private MD5Encoder() {
        // Hide default constructor for utility class
    }

    public static String encode(String s) {
        return encode(s, DEFAULT_CHARSET, hexadecimal);
    }

    public static String encodeHigh(String s) {
        return encode(s, DEFAULT_CHARSET, hexadecimalH);
    }

    public static String encodeHigh(String s, String charset) {
        return encode(s, charset, hexadecimalH);
    }

    public static String encode(String s, String charset) {
        return encode(s, charset, hexadecimal);
    }

    public static String encode(String s, String charset, final char[] codes) {

        if (s == null)
            return null;

        byte[] strTemp = null;
        try {
            strTemp = s.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        MessageDigest mdTemp = null;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        mdTemp.update(strTemp);
        byte[] binaryData = mdTemp.digest();

        if (binaryData.length != 16)
            return null;

        char[] buffer = new char[32];

        for (int i = 0; i < 16; i++) {
            int low = binaryData[i] & 0x0f;
            int high = (binaryData[i] & 0xf0) >> 4;
            buffer[i * 2] = codes[high];
            buffer[i * 2 + 1] = codes[low];
        }

        return new String(buffer);
    }

    public static String MD5Base64(String content) {
        return Base64.encodeBase64String(encode(content).getBytes());
    }

    public static String getSignEncode(String encodeMethod, Integer encodeNum, String numStr, String signStr, String traceKey) {
        if (encodeMethod.equals(GlobalConfig.md5)) {
            if (encodeNum != null && encodeNum > 1) {
                String s = "";
                for (int i = 0; i < encodeNum; i++) {
                    if (i == 0) s = MD5Encoder.encode(signStr);
                    else s = MD5Encoder.encode(s + ("".equals(numStr) ? traceKey : numStr));
                }
                return s;
            }
            return MD5Encoder.encode(signStr);
        } else if (encodeMethod.equals(GlobalConfig.MD5)) {
            if (encodeNum != null && encodeNum > 1) {
                String s = "";
                for (int i = 0; i < encodeNum; i++) {
                    if (i == 0) s = MD5Encoder.encode(signStr).toUpperCase();
                    else s = MD5Encoder.encode(s + numStr).toUpperCase();
                }
                return s;
            }
            return MD5Encoder.encode(signStr).toUpperCase();
        } else if (encodeMethod.equals(GlobalConfig.MD5_64)) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return new BASE64Encoder().encode(md.digest(signStr.getBytes("UTF-8"))).toUpperCase();
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else if (encodeMethod.equals(GlobalConfig.md5_64)) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return new BASE64Encoder().encode(md.digest(signStr.getBytes("UTF-8")));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else if (encodeMethod.equals(GlobalConfig.HMAC)) {
            return HmacUtil.hmacSign(signStr, traceKey);
        } else if (encodeMethod.equals(GlobalConfig.HMAC_SHA1)) {
            return HmacUtil.hmacSHA1Sign(signStr, traceKey);
        } else if (encodeMethod.equals(GlobalConfig.MD5_Base64)) {
            return MD5Base64(signStr);
        } else if (encodeMethod.toUpperCase().equals(GlobalConfig.SHA) || encodeMethod.toUpperCase().equals(GlobalConfig.SHA256)) { //SHA
            if (encodeNum != null && encodeNum > 1) {
                String s = "";
                for (int i = 0; i < encodeNum; i++) {
                    if (i == 0) s = SHAUtils.SHASign(signStr, "UTF-8", encodeMethod);
                    else s = SHAUtils.SHASign(signStr + numStr, "UTF-8", encodeMethod);
                }
                return s;
            }
            return SHAUtils.SHASign(signStr, "UTF-8", encodeMethod);
        }else if (encodeMethod.equals(GlobalConfig.HMAC_SHA256)) {
            try {
                return HmacUtil.hmacSHA256Sign(signStr, traceKey);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (encodeMethod.equals(GlobalConfig.HMAC_SHA256_BASE64)) {
            try {
                return HmacUtil.hmacSHA256Base64Sign(signStr, traceKey);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (encodeMethod.equals(GlobalConfig.HMAC_MD5_UPPER)) {
            try {
                return HmacUtil.hmacMd5Sign(signStr, traceKey).toUpperCase();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (encodeMethod.equals(GlobalConfig.HMAC_MD5_LOWER)) {
            try {
                return HmacUtil.hmacMd5Sign(signStr, traceKey);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return String.valueOf(hexadecimal[iD1]) + String.valueOf(hexadecimal[iD2]);
    }

    // 转换字节数组为16进制字串
    public static String byteToString(byte[] bByte) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < bByte.length; i++) {
            sBuilder.append(byteToArrayString(bByte[i]));
        }
        return sBuilder.toString();
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static byte[] GetMD5ByteCode(byte[] bytes) {
        byte[] resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = (md.digest(bytes));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}
