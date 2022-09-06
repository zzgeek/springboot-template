package zzgeek.conf;

import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import zzgeek.commons.meta.ApiException;
import zzgeek.commons.meta.NetViewFactory;
import zzgeek.commons.meta.ResponseView;
import zzgeek.commons.meta.ServiceException;
import zzgeek.enums.ExceptionCodeAndMsg;

import java.sql.SQLException;

/**
 * @author ZZGeek
 * @date 2021年10月30日 13:53
 * @description 全局异常处理类
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MongoException.class)
    public ResponseView handleMongoException (final MongoException mongoException) {
        log.info("mongoException: {}",mongoException.getMessage());
        return NetViewFactory.errorDeginitionResponse(ExceptionCodeAndMsg.MONGO_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseView handleMysqlException (final SQLException sqlException) {
        log.info("mysqlException: {}",sqlException.getMessage());
        return NetViewFactory.errorDeginitionResponse(ExceptionCodeAndMsg.MYSQL_ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseView handleServiceException (final ServiceException serviceException) {
        log.info("serviceException at " + serviceException.getService() + ": {}",serviceException.getMessage());
        return NetViewFactory.errorDeginitionResponse(serviceException.getExceptionCodeAndMsg());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseView handleApiException (final ApiException apiException){
        log.info("apiException at " + apiException.getController() + ": {}",apiException.getMessage());
        return NetViewFactory.errorDeginitionResponse(apiException.getExceptionCodeAndMsg());
    }
}
