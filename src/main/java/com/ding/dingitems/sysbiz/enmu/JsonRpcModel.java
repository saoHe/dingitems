package com.ding.dingitems.sysbiz.enmu;


/**
 * JSONRPC 返回错误定义
 */
public enum JsonRpcModel {

    //JSONRPC 公共定义
    PARSE_ERROR(-32700, "Parse error:语法解析错误"),
    INVALID_REQUEST(-32600, "Invalid Request:无效请求"),
    METHOD_NOT_FOUND(-32601, "Method not found:找不到方法"),
    INVALID_PARAMS(-32602, "Invalid Params:无效的方法参数"),
    INTERNAL_ERROR(-32603, "Internal Error:JSON-RPC内部错误"),

    // 32000-32099 自定义服务器错误
    SERVER_ERROR(-32000, "服务内部通用错误"),
    LOGIN_ERROR(-32001, "登录失败"),
    PERMISSIONS_ERROR(-32002, "权限问题"),
    PASS_ERROR(-32003, "登录失败"),

    ;

    private Integer code;
    private String message;

    JsonRpcModel(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过code 取枚举
     *
     * @return
     */
    public static JsonRpcModel getEnumByCode(Integer code) {
        for (JsonRpcModel enums : JsonRpcModel.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }

    /**
     * 通过code 取值
     *
     * @return
     */
    public static String getMsgByCode(Integer code) {
        for (JsonRpcModel enums : JsonRpcModel.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMessage();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }
}

