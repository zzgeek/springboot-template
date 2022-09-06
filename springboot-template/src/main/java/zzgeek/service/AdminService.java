package zzgeek.service;

import zzgeek.vo.AdminsLoginVo;

import java.sql.SQLException;

/**
 * @author ZZGeek
 * @date 2021年10月23日 15:43
 * @description AdminsService
 */
public interface AdminService {
    AdminsLoginVo loginAdminByAccount (String adminAccount, String adminPassword, String ipAddress) throws SQLException;
}
