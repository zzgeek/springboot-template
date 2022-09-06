package zzgeek.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzgeek.commons.meta.ServiceException;
import zzgeek.conf.GlobalConfig;
import zzgeek.dao.read.ReadAdminDao;
import zzgeek.dao.read.ReadSiteDao;
import zzgeek.enums.ExceptionCodeAndMsg;
import zzgeek.model.Admin;
import zzgeek.model.Site;
import zzgeek.service.AdminService;
import zzgeek.utlis.JwtUtil;
import zzgeek.vo.AdminsLoginVo;

import java.sql.SQLException;

/**
 * @author ZZGeek
 * @date 2021年10月23日 15:48
 * @description AdminsServiceImpl
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ReadAdminDao readAdminsDao;
    @Autowired
    private ReadSiteDao readSitesDao;

    @Override
    public AdminsLoginVo loginAdminByAccount(String adminAccount, String adminPassword, String ipAddress) throws SQLException {
        AdminsLoginVo adminsLoginVo = new AdminsLoginVo();
        Admin admin = readAdminsDao.selectAdminsForLogin(adminAccount,adminPassword);
        if(admin == null){
            throw new ServiceException(ExceptionCodeAndMsg.WRONG_ADMINS,this.getClass().getName());
        }
        Site sites = readSitesDao.selectSiteBySiteCode(admin.getSiteCode());

        if(sites == null){
            throw new ServiceException(ExceptionCodeAndMsg.NON_SITES,this.getClass().getName());
        }else if(!sites.getSiteStatus()){
            throw new ServiceException(ExceptionCodeAndMsg.DISABLE_SITES,this.getClass().getName());
        }else {
            String ipAddressAll = sites.getIpAddress();
            if(ipAddressAll == null) {
                throw new ServiceException(ExceptionCodeAndMsg.NON_SITEIP, this.getClass().getName());
            }else if (ipAddressAll.equals("*")) {
                String adminToken = JwtUtil.encodeJwt(adminAccount,admin.getRoleCode(), GlobalConfig.JWTCONSTANT_ADMINS_SECRET_KEY,GlobalConfig.JWTCONSTANT_ADMINS_EFFECTIVE_TIME);
                adminsLoginVo.setAdminAccount(adminAccount);
                adminsLoginVo.setAdminToken(adminToken);
                adminsLoginVo.setSiteCode(admin.getSiteCode());
                adminsLoginVo.setAvatar(admin.getAvatar());
                adminsLoginVo.setRoleCode(admin.getRoleCode());
                return adminsLoginVo;
            }else{
                String[] ipAddressStr = ipAddressAll.split(",");
                for (String str : ipAddressStr) {
                    if (str.equals(ipAddress)){
                        String adminToken = JwtUtil.encodeJwt(adminAccount,admin.getRoleCode(), GlobalConfig.JWTCONSTANT_ADMINS_SECRET_KEY,GlobalConfig.JWTCONSTANT_ADMINS_EFFECTIVE_TIME);
                        adminsLoginVo.setAdminAccount(adminAccount);
                        adminsLoginVo.setAdminToken(adminToken);
                        adminsLoginVo.setSiteCode(admin.getSiteCode());
                        adminsLoginVo.setAvatar(admin.getAvatar());
                        adminsLoginVo.setRoleCode(admin.getRoleCode());
                        return adminsLoginVo;
                    }
                }
            }
        }
        return null;
    }
}
