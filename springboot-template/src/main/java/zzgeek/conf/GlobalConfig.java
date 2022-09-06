package zzgeek.conf;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;


public class GlobalConfig {
    public static final String JWTCONSTANT_ADMINS_SECRET_KEY = "checkSign";
    public static final String JWTCONSTANT_USER_SECRET_KEY = "checkSign";
    public static final Long JWTCONSTANT_ADMINS_EFFECTIVE_TIME = 600000L;
    public static final Long JWTCONSTANT_USER_EFFECTIVE_TIME = 1200000L;

    public static final String CHECK_SIGN = "checkSign";
    public static final String SIGN = "sign";
    public static final String MESSAGE = "message";
    public static final String DATE_URL = "dataUrl";
    public static final String BASE64 = "BASE64";
    public static final String URL_DECODE = "URLDecoder";
    public static final String JSON_OBJECT = "JsonObject";
    public static final String JSON_ARRAY = "JsonArray";
    public static final String RESULT = "result";
    public static final String TRACE_KEY = "traceKey";
    public static final String HTML = "HTML";

    //加密方式
    public static final String MD5 = "MD5";
    public static final String md5 = "md5";
    public static final String MD5_64 = "MD5_64";
    public static final String md5_64 = "md5_64";
    public static final String HMAC = "HMAC";
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String MD5_Base64 = "MD5_Base64";
    public static final String RSA ="RSA";
    public static final String sha ="sha";
    public static final String SHA ="SHA";
    public static final String sha256 ="sha-256";
    public static final String SHA256 ="SHA-256";

    public static final String QP_456 = "456qp";
    public static final String INTER_SITE = "intqa";
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String HMAC_SHA256_BASE64 ="SHA256_B64";
    public static final String HMAC_MD5_UPPER ="HMAC_MD5";
    public static final String HMAC_MD5_LOWER ="hmac_md5";

    private static Properties config = null;

    static {
        try {
            reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void reloadConfig() throws IOException {
        config = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("global.properties");
        config.load(in);
        in.close();
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }

    public static JSONObject getGroupConfig(String prefix) {
        if (prefix == null || "".equals(prefix.trim())) return null;
        Enumeration it = config.propertyNames();
        JSONObject map = new JSONObject();
        while (it.hasMoreElements()) {
            Object key = it.nextElement();
            if (key != null && key.toString().startsWith(prefix)) {
                String name = key.toString().replace(prefix + ".", "");
                map.put(name, config.get(key));
            }
        }
        return map;
    }

}
