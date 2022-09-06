package zzgeek.commons.meta;

import lombok.Data;
import zzgeek.enums.ExceptionCodeAndMsg;

/**
 * @author ZZGeek
 * @date 2021年10月31日 23:29
 * @description ServiceException
 */
@Data
public class ServiceException extends RuntimeException{
    private String service;
    private ExceptionCodeAndMsg exceptionCodeAndMsg;

    public ServiceException(ExceptionCodeAndMsg exceptionCodeAndMsg, String service){
        super();
        this.exceptionCodeAndMsg = exceptionCodeAndMsg;
        this.service = service;
    }
}
