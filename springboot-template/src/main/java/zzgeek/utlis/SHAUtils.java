package zzgeek.utlis;


import zzgeek.conf.GlobalConfig;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHAUtils {
    private static final String SING_ALGORITHMS_SHA1 = "SHA-1";
    private static final String SING_ALGORITHMS_SHA256 = "SHA-256";
    public static final String SING_ALGORITHMS_SHA512 = "SHA-512";


    /**
     * SHA 加密算法
     *
     * @param content
     * @param inputCharset
     * @return
     */
    public static String SHASign(String content, String inputCharset, String shaType) {
        try {
            //sha 算法
            MessageDigest digest = MessageDigest.getInstance(shaType.toUpperCase());
            digest.update(content.getBytes(inputCharset));
            byte messageDigest[] = digest.digest();
            StringBuilder sb = new StringBuilder();
            //转十六进制
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    sb.append(0);
                }
                sb.append(shaHex);
            }
            if (shaType.contains(GlobalConfig.SHA)) {
                return sb.toString().toUpperCase();
            } else {
                return sb.toString();
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            //e.printStackTrace();
        }
        return null;
    }
}
