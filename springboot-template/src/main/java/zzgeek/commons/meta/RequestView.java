package zzgeek.commons.meta;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author ZZGeek
 * @date 2021年10月23日 14:02
 * @description RequestView
 */
@Data
public class RequestView {
    private JSONObject currentRequestParams;
    private String currentAdminIp;
}
