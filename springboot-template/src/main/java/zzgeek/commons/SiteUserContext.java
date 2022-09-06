package zzgeek.commons;

import lombok.Data;

import java.io.Serializable;

@Data
public class SiteUserContext implements Serializable {
    private static final long serialVersionUID = 7105471039734204693L;
    private String adminAccount;
    private Long siteId;
    private String userRole;
    private String siteCode;
    private String token;
    private String adminToken;
}
