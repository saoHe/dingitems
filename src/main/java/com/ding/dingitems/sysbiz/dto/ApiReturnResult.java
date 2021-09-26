package com.ding.dingitems.sysbiz.dto;


import com.ding.dingitems.sysbiz.enmu.JsonRpcModel;

/**
 * Api系统级统一响应
 *
 * @Author hjw
 */

public class ApiReturnResult {

    /**
     * JsonRpc的版本号
     */
    private String jsonrpc = "2.0";

    /**
     * 响应出错，返回错误对象
     */
    private com.ding.dingitems.sysbiz.dto.Error error;

    /**
     * 响应成功，返回值
     */
    private Object result;

    /**
     * 调用标识符
     */
    private String id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public com.ding.dingitems.sysbiz.dto.Error getError() {
        return error;
    }

    public Object getResult() {
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApiReturnResult() {
    }

    public ApiReturnResult(com.ding.dingitems.sysbiz.dto.Error error) {
        this.error = error;
    }

    public ApiReturnResult(Object result) {
        this.result = result;
    }


    public static com.ding.dingitems.sysbiz.dto.ApiReturnResult succ(Object result) {
        return new com.ding.dingitems.sysbiz.dto.ApiReturnResult(result);
    }


    public static com.ding.dingitems.sysbiz.dto.ApiReturnResult fail(Integer code, String errMsg, Object errData) {
        return new com.ding.dingitems.sysbiz.dto.ApiReturnResult(new com.ding.dingitems.sysbiz.dto.Error(code, errMsg, errData));
    }

    public static com.ding.dingitems.sysbiz.dto.ApiReturnResult fail(String errMsg, Object errData) {
        return fail(null, errMsg, errData);
    }

    public static com.ding.dingitems.sysbiz.dto.ApiReturnResult fail(Integer code, String errMsg) {
        return fail(code, errMsg, null);
    }

    public static com.ding.dingitems.sysbiz.dto.ApiReturnResult fail(Integer code, Object errData) {
        return fail(code, null, errData);
    }

    public static com.ding.dingitems.sysbiz.dto.ApiReturnResult fail(String errMsg) {
        return fail(JsonRpcModel.SERVER_ERROR.getCode(), errMsg, null);
    }

    public static com.ding.dingitems.sysbiz.dto.ApiReturnResult fail() {
        return fail(null, null, null);
    }
}
