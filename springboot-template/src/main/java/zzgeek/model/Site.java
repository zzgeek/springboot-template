package zzgeek.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZZGeek
 * @date 2021年10月21日 15:24
 * @description Sites
 */
@Data
public class Site implements Serializable {

    private static final long serialVersionUID = 281731891104356492L;
    private Long id;
    private String siteName;
    private String ipAddress;
    private String blackList;
    private Boolean siteStatus;
    private String siteCode;
}
