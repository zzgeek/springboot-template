package zzgeek.vo;

import lombok.Data;

/**
 * @author ZZGeek
 * @date 2021年10月23日 22:27
 * @description AdminsLoginVo
 */
@Data
public class AdminsLoginVo {
    private String adminAccount;
    private String siteCode;
    private String roleCode;
    private String adminToken;
    private String avatar;
}
