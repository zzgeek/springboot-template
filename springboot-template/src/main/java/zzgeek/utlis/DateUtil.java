package zzgeek.utlis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static final String TIME_FORMAT_MILLIS = "yyyy-MM-dd HH:mm:ss.sss";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_FORMAT_UTC = "yyyy-MM-dd HH:mm:ss.SSS 'UTC'";
    public static final String TIME_FORMAT_UTC_VR = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String TIME_FORMAT_YMD = "yyyyMMdd";
    public static final String TIME_FORMAT_AA = "MM/dd/yyyy hh:mm:ss aa";
    public static final String DATE_FORMAT = "MM月dd日 HH:mm";

    public static String dateFormat(Date time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(time);
    }

    public static Date dateParse(String time, String format) {
        SimpleDateFormat dateFormat;
        if (TIME_FORMAT_AA.equals(format)){
            dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        } else {
            dateFormat = new SimpleDateFormat(format);
        }
        try {
            return dateFormat.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }


    /**
     * 获取UTC时间字符串
     */
    public static String getUTCTimeStr(String format) {
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Date date = new Date(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(format ==null ? TIME_FORMAT_UTC : format);
        return sdf.format(date);
    }


    /**
     * 获取北京时间
     */
    public static String getBeiJingTimeStr(String format) {
        // 1、取得本地时间：
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 获取unix时间戳，精确到秒
     */
    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     *  获取i分钟后的时间
     * @param currentDate
     * @param i
     * @return
     */
    public static Date getAfterMinuteTime(Date currentDate, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MINUTE, i);
        return calendar.getTime();
    }

    public static Date getAfterHourTime(Date currentDate, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR, i);
        return calendar.getTime();
    }

    public static void main(String[] args) throws Exception{
        String s = "12/26/2019 8:25:00 PM";
        String s2 = "2019-12-26 8:25:00 PM";
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_AA,Locale.ENGLISH);
        System.out.println(sdf.parse(s));
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa",Locale.ENGLISH);
        System.out.println(sdf2.parse(s2));

    }
}

