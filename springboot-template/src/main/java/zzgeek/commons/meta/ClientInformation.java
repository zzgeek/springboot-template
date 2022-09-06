package zzgeek.commons.meta;

import lombok.Data;

/**
 * 客户端信息
 */
@Data
public class ClientInformation {

    /**
     * @return 当前客户端IP地址
     */
    private String currentIpAddress;

    /**
     * @return 当前客户端目标URI
     */
    private String currentTargetURI;

    /**
     * @return 当前客户端请求方法
     */
    private String currentTargetMethod;


}
