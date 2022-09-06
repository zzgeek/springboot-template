package zzgeek.commons.meta;

import com.alibaba.fastjson.JSONObject;
import zzgeek.enums.ExceptionCodeAndMsg;
import zzgeek.utlis.ClientUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ZZGeek
 * @date 2021年10月23日 13:22
 * @description 请求响应报文处理类
 */
public class NetViewFactory {
    /**
     *
     * @description 请求参数解析
     * @author ZZGeek
     * @date 2021/10/23 13:24
     * @params
    	 * @param request request
     * @return alibaba.fastjson.JSONObject
     */
    public static JSONObject decoderRequestData(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength == 0) return null;
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readLen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readLen == -1) {
                break;
            }
            i += readLen;
        }
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        String json = new String(buffer, charEncoding);
        //json = URLDecoder.decode(json, "UTF-8");
        if (json.startsWith("\"")) json = json.substring(1);
        if (json.endsWith("}\"")) json = json.substring(0, json.length() - 1);
        if (json.startsWith("=")) json = json.substring(1);
        return JSONObject.parseObject(json);
    }

    /**
     *
     * @description 请求报文封装
     * @author ZZGeek
     * @date 2021/10/23 14:13
     * @params
	 * @param request request
 * @return commons.meta.RequestView
     */
    public static RequestView successRequest(HttpServletRequest request) throws IOException{
        JSONObject data = decoderRequestData(request);
        RequestView result = new RequestView();
        if (data == null) {
            throw new ApiException(ExceptionCodeAndMsg.PAREME_PARSE_ERROR,"successRequest");
        }else{
            result.setCurrentRequestParams(data.getJSONObject("paramData"));
            result.setCurrentAdminIp(ClientUtil.getFirstIpAddr(request));
        }
        return result;
    }

    /**
     * 
     * @description 返回报文封装
     * @author ZZGeek
     * @date 2021/10/23 19:30 
     * @params  
    	 * @param obj obj 
     * @return commons.meta.ResponseView
     */
    public static ResponseView successResponse(Object obj){
        ResponseView responseView = new ResponseView();
        responseView.setRespCode(ExceptionCodeAndMsg.SUCCESS.getCode());
        responseView.setRespMsg(ExceptionCodeAndMsg.SUCCESS.getMsg());
        responseView.setRespData(obj);
        return responseView;
    }

    /**
     *
     * @description 空参返回报文封装
     * @author ZZGeek
     * @date 2021/10/31 23:59
     * @params  *
     * @return commons.meta.ResponseView
     */
    public static ResponseView successResponse() {
        return successResponse(null);
    }

    /**
     * 
     * @description 自定义错误返回报文封装
     * @author ZZGeek
     * @date 2021/10/23 19:54 
     * @params  
    	 * @param code code
    	 * @param msg msg 
     * @return commons.meta.ResponseView
     */
    public static ResponseView errorCustomizeResponse(String code,String msg){
        ResponseView responseView = new ResponseView();
        responseView.setRespCode(code);
        responseView.setRespMsg(msg);
        return responseView;
    }

   /**
    * 
    * @description 已定义错误返回报文封装
    * @author ZZGeek
    * @date 2021/10/23 22:15
    * @params  
	 * @param apiExceptionCodeAndMsg apiExceptionCodeAndMsg
 * @return commons.meta.ResponseView
    */
    public static ResponseView errorDeginitionResponse(ExceptionCodeAndMsg apiExceptionCodeAndMsg){
        ResponseView result = new ResponseView();
        result.setRespCode(apiExceptionCodeAndMsg.getCode());
        result.setRespMsg(apiExceptionCodeAndMsg.getMsg());
        return result;
    }
}
