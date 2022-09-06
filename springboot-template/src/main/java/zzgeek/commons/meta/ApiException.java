package zzgeek.commons.meta;

import zzgeek.enums.ExceptionCodeAndMsg;
import lombok.Data;

@Data
public class ApiException extends RuntimeException{
    private ExceptionCodeAndMsg exceptionCodeAndMsg;
    private String controller;

    public ApiException(ExceptionCodeAndMsg exceptionCodeAndMsg,String controller) {
        super();
        this.exceptionCodeAndMsg = exceptionCodeAndMsg;
        this.controller = controller;
    }

}
