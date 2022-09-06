package zzgeek.dao.read;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zzgeek.model.Site;

/**
 * @author ZZGeek
 * @date 2021年10月23日 22:14
 * @description ReadSitesDao
 */
@Mapper
public interface ReadSiteDao {
    Site selectSiteBySiteCode (@Param("siteCode") String siteCode);
}
