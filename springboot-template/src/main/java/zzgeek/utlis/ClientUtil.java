package zzgeek.utlis;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientUtil {

    /**
     * 
     * @description 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * @author ZZGeek
     * @date 2021/10/22 11:55 
     * @params  
    	 * @param request request 
     * @return lang.String
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 
     * @description 获取当前request对象。
     * @author ZZGeek
     * @date 2021/10/22 11:56 
     * @params  * 
     * @return servlet.http.HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getRequest();
    }

    /**
     * 
     * @description 获得用户真实IP
     * @author ZZGeek
     * @date 2021/10/22 11:56 
     * @params  
    	 * @param request request 
     * @return lang.String
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        /*if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }*/
        if (ip == null || ip.length() == 0 ||
                "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) ||
                    "0:0:0:0:0:0:0:1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        return ip;
    }

    /**
     * 
     * @description 获得用户真实IP
     * @author ZZGeek
     * @date 2021/10/22 11:57 
     * @params  * 
     * @return lang.String
     */
    public static String getIpAddr() {
        return getIpAddr(getCurrentRequest());
    }

    public static String getFirstIpAddr() {
        String ip = getIpAddr(getCurrentRequest());
        return ip.split(",")[0].trim();
    }

    public static String getFirstIpAddr(HttpServletRequest request) {
        String ip = getIpAddr(request);
        return ip.split(",")[0].trim();
    }

    /**
     * 
     * @description 判断IP是否属于IP段内
     * @author ZZGeek
     * @date 2021/10/22 11:57 
     * @params  
    	 * @param ipStart ipStart
    	 * @param ipEnd ipEnd
    	 * @param ipToCheck ipToCheck 
     * @return boolean
     */
    public static boolean checkIpBelongRange(String ipStart, String ipEnd, String ipToCheck) {
        try {
            long ipLo = ipTolong(InetAddress.getByName(ipStart));
            long ipHi = ipTolong(InetAddress.getByName(ipEnd));
            long ipcheck = ipTolong(InetAddress.getByName(ipToCheck));

            return (ipcheck >= ipLo && ipcheck <= ipHi);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @description 判断IP是否属于IP段内
     * @author ZZGeek
     * @date 2021/10/22 11:58 
     * @params  
    	 * @param ipStart ipStart
    	 * @param ipEnd ipEnd
    	 * @param ipToCheck ipToCheck 
     * @return boolean
     */
    public static boolean checkIpBelongRange(long ipStart, long ipEnd, String ipToCheck) {
        try {
            long ipcheck = ipTolong(InetAddress.getByName(ipToCheck));

            return (ipcheck >= ipStart && ipcheck <= ipEnd);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @description IP转Long
     * @author ZZGeek
     * @date 2021/10/22 11:58 
     * @params  
    	 * @param ipToCheck ipToCheck 
     * @return long
     */
    public static long ipTolong(String ipToCheck) {
        try {
            return ipTolong(InetAddress.getByName(ipToCheck));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 
     * @description IP转Long
     * @author ZZGeek
     * @date 2021/10/22 11:58 
     * @params  
    	 * @param ip ip 
     * @return long
     */
    public static long ipTolong(InetAddress ip) {
        long result = 0;
        byte[] ipAdds = ip.getAddress();
        for (byte b : ipAdds) {
            result <<= 8;
            result |= b & 0xff;
        }
        return result;
    }

    /**
     * 
     * @description 这里只区分安卓和IOS，有些通道：苹果用户可以充值，安卓无法充值。APP可以苹果手机显示么？安卓不能显示该通道
     * @author ZZGeek
     * @date 2021/10/22 11:59 
     * @params  
    	 * @param request request 
     * @return lang.String
     */
    public static String getOsInfo(HttpServletRequest request) {
        String os = null;
        if (request.getHeader("User-Agent") != null) {
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            if (userAgent.contains("android") || userAgent.contains("linux")) {
                os = "android";
            } else if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad") || userAgent.contains("ios")) {
                os = "iphone";
            } else {
                os = null;
            }
        }
        return os;
    }

    /**
     * 
     * @description getSourceWay 
     * @author ZZGeek
     * @date 2021/10/22 11:59 
     * @params  
    	 * @param request request 
     * @return lang.String
     */
    public static String getSourceWay(HttpServletRequest request) {
        String os = null;
        if (request.getHeader("User-Agent") != null) {
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            if (userAgent.contains("android") || userAgent.contains("linux")) {
                os = "ANDROID";
            } else if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad") || userAgent.contains("ios")) {
                os = "IOS";
            } else {
                os = null;
            }
        }
        return os;
    }

}
