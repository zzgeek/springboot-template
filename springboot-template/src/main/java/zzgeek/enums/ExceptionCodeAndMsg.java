package zzgeek.enums;

public enum ExceptionCodeAndMsg {
    SUCCESS("000","返回成功"),

    UNKNOW_ERROR("001","系统异常"),
    MYSQL_ERROR("OO2","mysql异常"),
    MONGO_ERROR("003","mongo异常"),
    PAREME_PARSE_ERROR("004","参数解析失败"),

    INVALID_ADMINS_TOKEN("101","令牌不合法"),
    NON_ADMINS_TOKEN("102","管理员token不存在"),
    NON_AUTHORITY("103","不具备请求权限"),
    WRONG_ADMINS("104","用户名密码错误"),
    NON_SITES("105","站点信息不存在"),
    DISABLE_SITES("106","该站点已禁用"),
    NON_SITEIP("107","站点未配置ip白名单"),

    ;

    private String code;
    private String msg;

    ExceptionCodeAndMsg(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
