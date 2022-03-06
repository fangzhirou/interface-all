package com.confirm.Util;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;

public class UserAgentUtils {

    private static Logger logger = LoggerFactory.getLogger(UserAgentUtils.class);



    /**
     * 根据http获取userAgent信息
     *
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent;
    }
    /**
     * 获取客户端ip信息*
     */
    public static String getClientIPrequest(HttpServletRequest request) {
        //String userAgent = getUserAgent(request);
        String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }

    public static String getClientIP(HttpServletRequest request){
        String IP=getClientIPrequest(request);
        return getClientIP(IP);
    }
    public static String getClientIP(String IP){
        return IP;
    }
    /**
     * 根据request获取userAgent，然后解析出osVersion
     *
     * @param request
     * @return
     */
    public static String getOsVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsVersion(userAgent);
    }

    /**
     * 根据userAgent解析出osVersion
     *
     * @param userAgent
     * @return
     */
    public static String getOsVersion(String userAgent) {
        String osVersion = "";
        if (StringUtils.isBlank(userAgent)) {
            return osVersion;
        }
        String[] strArr = userAgent.substring(userAgent.indexOf("(") + 1,
                userAgent.indexOf(")")).split(";");
        if (null == strArr || strArr.length == 0) {
            return osVersion;
        }

        osVersion = strArr[1];
        logger.info("osVersion is:{}", osVersion);
        return osVersion;
    }

    /**
     * 获取操作系统对象
     *
     * @param userAgent
     * @return
     */
    private static OperatingSystem getOperatingSystem(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem operatingSystem = agent.getOperatingSystem();
        return operatingSystem;
    }
    /**
     * 获取deviceType
     *
     * @param request
     * @return
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceType(userAgent);
    }

    /**
     * 获取deviceType
     *
     * @param userAgent
     * @return
     */
    public static String getDeviceType(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String deviceType = operatingSystem.getDeviceType().toString();
        logger.info("deviceType is:{}", deviceType);
        return deviceType;
    }

    /**
     * 获取操作系统的名字
     *
     * @param request
     * @return
     */
    public static String getOsName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsName(userAgent);
    }

    /**
     * 获取操作系统的名字(osName)
     *
     * @param userAgent
     * @return
     */
    public static String getOsName(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String osName = operatingSystem.getName();
        logger.info("osName is:{}", osName);
        return osName;
    }


    /**
     * 获取device的生产厂家(deviceManufacturer)
     *
     * @param request
     */
    public static String getDeviceManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceManufacturer(userAgent);
    }

    /**
     * 获取device的生产厂家
     *
     * @param userAgent
     */
    public static String getDeviceManufacturer(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String deviceManufacturer = operatingSystem.getManufacturer().toString();
        logger.info("deviceManufacturer is:{}", deviceManufacturer);
        return deviceManufacturer;
    }
}


