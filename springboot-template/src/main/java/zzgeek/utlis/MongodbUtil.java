package zzgeek.utlis;


import com.github.pagehelper.PageInfo;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * mongodb curd 工具类
 * 数据对象中@Document 注解所配置的collection
 */
@Component
public class MongodbUtil {
    private static MongodbUtil mongodbUtils;

    @PostConstruct
    public void init() {
        mongodbUtils = this;
        mongodbUtils.mongoTemplate = this.mongoTemplate;
    }

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 保存数据对象
     *
     * @param obj 数据对象
     */
    public static Object save(Object obj) {
        return mongodbUtils.mongoTemplate.save(obj);
    }

    /**
     * 批量保存数据对象
     */
    public static Integer saveBatch(List<?> list, String collectionName) {
        BulkOperations bulkOp = mongodbUtils.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, collectionName);
        bulkOp.insert(list);
        BulkWriteResult result = bulkOp.execute();
        return result.getInsertedCount();
    }

    /**
     * 根据数据对象中的id删除数据
     *
     * @param obj 数据对象
     */
    public static void remove(Object obj) {
        mongodbUtils.mongoTemplate.remove(obj);
    }

    /**
     * 批量删除
     *
     * @param ids 批量删除的id
     * @param obj 数据对象
     */
    public static void removeById(List<String> ids, Object obj) {
        Criteria criteria = Criteria.where("_id").in(ids);
        Query query = Query.query(criteria);
        mongodbUtils.mongoTemplate.findAllAndRemove(query, obj.getClass());
    }

    /**
     * 根据条件删除
     *
     * @param criteria
     * @param obj      数据对象
     */
    public static void removeByCriteria(Criteria criteria, Object obj) {
        Query query = Query.query(criteria);
        mongodbUtils.mongoTemplate.findAllAndRemove(query, obj.getClass());
    }

    /**
     * 指定集合 修改数据，且仅修改找到的第一条数据
     *
     * @param accordingKey   修改条件 key
     * @param accordingValue 修改条件 value
     * @param updateKeys     修改内容 key数组
     * @param updateValues   修改内容 value数组
     * @param collectionName 集合名
     */
    public static void updateOne(String accordingKey, Object accordingValue, String[] updateKeys, Object[] updateValues,
                                 String collectionName) {

        Criteria criteria = Criteria.where(accordingKey).is(accordingValue);
        Query query = Query.query(criteria);
        Update update = new Update();
        for (int i = 0; i < updateKeys.length; i++) {
            update.set(updateKeys[i], updateValues[i]);
        }
        mongodbUtils.mongoTemplate.updateFirst(query, update, collectionName);
    }

    /**
     * 指定集合 修改数据，且修改所找到的所有数据
     *
     * @param accordingKey   修改条件 key
     * @param accordingValue 修改条件 value
     * @param updateKeys     修改内容 key数组
     * @param updateValues   修改内容 value数组
     * @param collectionName 集合名
     */
    public static void updateAll(String accordingKey, Object accordingValue, String[] updateKeys, Object[] updateValues, String collectionName) {

        Criteria criteria = Criteria.where(accordingKey).is(accordingValue);
        Query query = Query.query(criteria);
        Update update = new Update();
        for (int i = 0; i < updateKeys.length; i++) {
            update.set(updateKeys[i], updateValues[i]);
        }
        mongodbUtils.mongoTemplate.updateMulti(query, update, collectionName);
    }

    /**
     * 根据条件查询出符合的第一条数据
     *
     * @param obj        数据对象
     * @param findKeys   查询条件 key
     * @param findValues 查询条件 value
     * @return
     */
    public static Object findOne(Object obj, String[] findKeys, Object[] findValues) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        return mongodbUtils.mongoTemplate.findOne(query, obj.getClass());
    }

    /**
     * 根据条件查询出所有结果集
     *
     * @param obj        数据对象
     * @param findKeys   查询条件 key
     * @param findValues 查询条件 value
     * @return
     */
    public static List<? extends Object> find(Object obj, String[] findKeys, Object[] findValues) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (findValues[i] != null) {
                if (i == 0) {
                    criteria = Criteria.where(findKeys[i]).is(findValues[i]);
                } else {
                    criteria.and(findKeys[i]).is(findValues[i]);
                }
            }
        }
        Query query = Query.query(criteria);
        return mongodbUtils.mongoTemplate.find(query, obj.getClass());
    }

    /**
     * 根据条件查询出所有结果集
     *
     * @param obj      数据对象
     * @param criteria Criteria
     * @return
     */
    public static List<? extends Object> find(Object obj, Criteria criteria) {
        Query query = Query.query(criteria);
        return mongodbUtils.mongoTemplate.find(query, obj.getClass());
    }

    public static List<? extends Object> findQuery(Object obj, Query query) {
        return mongodbUtils.mongoTemplate.find(query, obj.getClass());
    }

    /**
     * 查询出所有结果集
     *
     * @param obj 数据对象
     * @return
     */
    public static List<? extends Object> findAll(Object obj) {
        return mongodbUtils.mongoTemplate.findAll(obj.getClass());
    }

    /**
     * 查询出两表关联的所有结果集
     *
     * @param obj 数据对象
     * @return
     */
    public static List<? extends Object> findAllByAggregation(Aggregation aggregation, String table, Object obj) {
        return mongodbUtils.mongoTemplate.aggregate(aggregation, table, obj.getClass()).getMappedResults();
    }

    public static PageInfo<? extends Object> findByPage(Object obj, String collectionName,
                                                        int pageNum, int pageSize, Query query) {
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        query.with(pageable);
        // 排序
        //query.with(new Sort(Direction.ASC, CONSTS.DEVICE_SERIAL_FIELD, CONSTS.DOMAINID_FIELD));
        // 查询总数
        int count = (int) mongodbUtils.mongoTemplate.count(query, collectionName);
        List<? extends Object> items = mongodbUtils.mongoTemplate.find(query, obj.getClass(), collectionName);
        PageImpl<Object> page = (PageImpl<Object>) PageableExecutionUtils.getPage(items, pageable, () -> 0);
        PageInfo<Object> pageInfo = new PageInfo<>(page.getContent());
        pageInfo.setTotal(count);
        return pageInfo;
    }

    /**
     * 分页查询两表关联结果集
     *
     * @param aggregation 聚合条件
     * @param criteria    criteria
     * @param table       主表表名
     * @param obj         映射对象
     * @param pageNum     页码
     * @param pageSize    页面大小
     * @return
     */
    public static PageInfo<? extends Object> findByPageAggregation(Aggregation aggregation, Criteria criteria, String table, Object obj, int pageNum, int pageSize) {
        //两表关联的所有结果集
        List<? extends Object> list = findAllByAggregation(aggregation, table, obj);
        Query query = new Query();
        query.addCriteria(criteria);
        // 查询总数
        int count = (int) mongodbUtils.mongoTemplate.count(query, table);
        //封装PageInfo
        PageInfo<? extends Object> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(count);
        pageInfo.setPages((int) Math.ceil((double) pageInfo.getTotal() / (double) pageInfo.getPageSize()));
        return pageInfo;
    }

}


