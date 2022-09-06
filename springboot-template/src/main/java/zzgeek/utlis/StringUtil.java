package zzgeek.utlis;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {

    private static final int PWD_SIZE = 16;

    /**
     * 截取字符串指定的内容
     *
     * @param str   原始字符串
     * @param begin 从指定的开始标签位置,为空时从头开始
     * @param end   到指定的结束标签位置,为空时到最后
     * @return 截取的值
     */
    public static String getElementValue(String str, String begin, String end) throws StringIndexOutOfBoundsException {
        try {
            if (begin == null || "".equals(begin)) return str.substring(0, str.indexOf(end));
            if (end == null || "".equals(end)) return str.substring(str.indexOf(begin) + begin.length(), str.length());
            return str.substring(str.indexOf(begin) + begin.length(), str.indexOf(end));
        } catch (StringIndexOutOfBoundsException e) {
            throw e;
        }
    }

    public static Object formatValue(Object value, String format) {
        if (format != null && !"".equals(format.trim())) {
            if (format.contains("%."))//格式化金额 小数位
                return String.format(format, value);
            if (format.contains("*1")) {//单位为分的商户  去掉小数点.00
                BigDecimal amount = (BigDecimal) value;
                amount = amount.multiply(new BigDecimal(format.substring(1, format.length())));
                String[] str = String.valueOf(amount).split("\\.");
                return str[0];
            }
            if (format.toUpperCase().contains("YY")) //处理时间格式
                return new SimpleDateFormat(format).format((value == null || "".equals(value)) ? new Date() : value);
            if (format.toUpperCase().contains("TIMES")) //处理时间格式 时间戳精确到秒
                return getSecondTimestamp(new Date());
            if (format.toUpperCase().contains("MD5")) //加密字段,目前只支持md5
                return MD5Encoder.getSignEncode(format, 1, null, String.valueOf(value), null);
            if (format.equals("urlencode") || format.equals("URLEncode")) { //URL Encoder字段
                try {
                    if (format.equals("urlencode")) {
                        return URLEncoder.encode(String.valueOf(value), "UTF-8").toLowerCase();
                    }
                    return URLEncoder.encode(String.valueOf(value), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return value;
                }
            }
            if(format.toUpperCase().contains("NUMBER")){
                BigDecimal numberValue = new BigDecimal(value.toString());
                String[] arr=format.split("-");
                if(arr.length>1){
                    numberValue=new BigDecimal(String.format("%."+arr[1]+"f", value));
                }
                return numberValue;
            }
            if(format.toUpperCase().contains("UPPER")){
                return value.toString().toUpperCase();
            }
            if(format.toUpperCase().contains("LOWER")){
                return value.toString().toLowerCase();
            }
        }
        return value;
    }

    /**
     * 获取精确到秒的时间戳
     *
     * @param date 时间
     */
    public static String getSecondTimestamp(Date date) {
        if (null == date) {
            return "";
        }
        return String.valueOf(date.getTime() / 1000);
    }


    public static String formatString(String str, Object... args) {
        Pattern pattern = Pattern.compile("\\$\\d+");
        String temp = str;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String reg = matcher.group(0);
            Integer idx = Integer.valueOf(reg.substring(1));
            if (args != null && args.length > idx) {
                Object val = args[idx];
                temp = temp.replace(reg, val == null ? "NA" : val.toString());
            }
        }
        return temp;
    }

    public static String formatString(String str, Map<String, Object> map) {
        Pattern pattern = Pattern.compile("\\$\\{(\\w+)}");
        String temp = str;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String reg = matcher.group(0);
            String key = matcher.group(1);
            Object val = map.get(key);
            temp = temp.replace(reg, val == null ? "" : val.toString());
        }
        return temp;
    }

    public static String getMd5Sign(Map<String, Object> params, String md5key) throws UnsupportedEncodingException {
        List<String> keyList = new ArrayList<>(params.keySet());
        Collections.sort(keyList); //按ascii排序
        String md5Str = getMd5Str(params, keyList); //签名格式:key1=value1key2=value2....md5key);  //value1和key2之间没有其他字符
        return MD5Encoder.encode(md5Str + md5key, "utf-8");
    }

    private static String getMd5Str(Map<String, Object> params, List<String> keyList) {
        StringBuilder md5Str = new StringBuilder();
        for (String key : keyList) {
            if (params.get(key) != null) {
                String value = params.get(key).toString();
                if (!value.equals(""))
                    md5Str.append(key).append("=").append(value);
            }
        }
        return md5Str.toString();
    }

    /**
     * 排序加密 格式如：termIp=121&tranCode=1101&key=XdSFS12
     *
     * @param map 加密参数
     * @param key 交易密钥
     */
    public static String getSign(Map<String, Object> map, String key, String encodeMethod) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!entry.getValue().equals("")) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString().substring(0, sb.toString().length() - 1);//去掉最后一个&字符
        if (null != key)
            result = result + "&key=" + key;
        if (null != encodeMethod)
            result = MD5Encoder.getSignEncode(encodeMethod, null, null, result, key);
        return result;
    }

    /**
     * Map拼接字符串  格式: a=1&b=2&c=3
     *
     * @param parm
     * @return
     */
    public static String createRequest(Map<String, String> parm) {
        StringBuilder rtn = new StringBuilder();
        for (String s : parm.keySet()) {
            try {
                rtn.append(s).append("=").append(URLEncoder.encode(parm.get(s), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return rtn.toString();
    }

    /**
     * Map拼接字符串  格式: a=1&b=2&c=3
     *
     * @param params
     * @return
     */
    public static String createParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> sortParams = new TreeMap<>(params);
        for (Map.Entry<String, String> entry : sortParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().trim();
            if (!StringUtils.isEmpty(value)) {
                sb.append("&").append(key).append("=").append(value);
            }
        }
        String signValue = sb.toString().replaceFirst("&", "");
        return signValue;
    }

    public static String getSignFormat(Map<String, Object> map) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!entry.getValue().equals("")) {
                list.add(entry.getKey() + entry.getValue());
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        return sb.toString();
    }

    /**
     * 将普通字符串转换成十六进制字符串
     * str2Hex(123456)返回结果为313233343536
     *
     * @param args 普通字符串
     * @return 十六进制字符串 @
     */
    public static String str2Hex(String args) {
        if (StringUtils.isEmpty(args)) return args;
        return new String(Hex.encodeHex(args.getBytes()));
    }

    /**
     * 将十六进制字符串转换成普通字符串
     * hex2Str(313233343536)返回结果为123456
     *
     * @param args [0] : 十六进制字符串
     * @return 转换后的字符串 @
     */
    public static String hex2Str(String args) {
        if (StringUtils.isEmpty(args)) return args;
        try {
            return new String(Hex.decodeHex(args.toCharArray()));
        } catch (DecoderException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 将VO字段转成数据库字段
     *
     * @param property
     * @return
     */
    public static String propertyToField(String property) {
        if (null == property) {
            return "";
        }
        char[] chars = property.toCharArray();
        StringBuilder sbd = new StringBuilder();
        for (char c : chars) {
            if (CharUtils.isAsciiAlphaUpper(c)) {
                sbd.append("_").append(StringUtils.lowerCase(CharUtils.toString(c)));
            } else {
                sbd.append(c);
            }
        }
        return sbd.toString();
    }

    public static byte[] pwdHandler(String password) throws UnsupportedEncodingException {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(PWD_SIZE);
        sb.append(password);
        while (sb.length() < PWD_SIZE) {
            sb.append("0");
        }
        if (sb.length() > PWD_SIZE) {
            sb.setLength(PWD_SIZE);
        }
        data = sb.toString().getBytes("UTF-8");
        return data;
    }

    /**
     * 生成指定长度的字符串，由0-9，a-z,A-Z组成
     * @param length
     * @return
     */
    public static String getNonce(int length) {
        String key = "";
        String strPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        int max = strPool.length();
        for (int i = 0; i < length; i++) {
            key += strPool.charAt((int) (Math.random() * max));
        }
        return key;
    }
}
