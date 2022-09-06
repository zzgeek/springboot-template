package zzgeek.utlis;
import com.qiniu.util.Auth;

/**
 * @author ZZGeek
 * @date 2022年09月01日 20:17
 * @description QiniuUtil
 */
public class QiniuUtil {

    static String getQiniuToken (String accessKey,String secretKey,String bucket) {
        String upToken = "";
        Auth auth = Auth.create(accessKey,secretKey);
        upToken = auth.uploadToken(bucket);
        return upToken;
    }

    public static void main(String[] args) {
        String accessKey = "a5vhAGQErwrixYoGL6IQPDpnok1OZo17rniNKx7t";
        String secretKey = "42tXmNfzEEW29kh_eVActbg7d29mdB8D2USv8b3o";
        String bucket = "zzgeek";
        String upToken = getQiniuToken(accessKey,secretKey,bucket);
        System.out.println(upToken);

    }


}


