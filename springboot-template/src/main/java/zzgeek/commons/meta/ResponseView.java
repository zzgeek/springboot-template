package zzgeek.commons.meta;

import lombok.Data;

/**
 * @author ZZGeek
 * @date 2021年10月23日 15:25
 * @description ResponseView
 */
@Data
public class ResponseView {
    private String respCode;
    private String respMsg;
    private Object respData;
}
