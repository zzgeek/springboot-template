package zzgeek.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZZGeek
 * @date 2021年10月21日 13:30
 * @description Admins
 */
@Data
public class Admin implements Serializable {

    private static final long serialVersionUID = 7532142983195875475L;
    private Long id;
    private String adminAccount;
    private String adminPassword;
    private String googleSecret;
    private String siteCode;
    private String avatar;
    private String roleCode;
}
