package zzgeek.dao.read;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zzgeek.model.Admin;

import java.sql.SQLException;

/**
 * @author ZZGeek
 * @date 2021年10月23日 15:31
 * @description ReadAdminsDao
 */
@Mapper
public interface ReadAdminDao {
    Admin selectAdminsForLogin(@Param("adminAccount") String adminAccount, @Param("adminPassword") String adminPassword) throws SQLException;
}
