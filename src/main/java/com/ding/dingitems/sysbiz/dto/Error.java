package com.ding.dingitems.sysbiz.dto;


import com.ding.dingitems.sysbiz.enmu.JsonRpcModel;
import org.apache.commons.lang3.StringUtils;

/**
 * API错误对象
 *
 * @Author hjw
 **/
public class Error {

    /**
     * 错误类型
     */
    private Integer code = JsonRpcModel.SERVER_ERROR.getCode();

    /**
     * 错误原因
     */
    private String message = JsonRpcModel.SERVER_ERROR.getMessage();

    /**
     * 错误详情（错误的额外信息）
     */
    private Object data;

    public Error() {
    }

    public Error(Integer code, String message) {
        new Error(code, message, null);
    }

    public Error(Integer code, String message, Object data) {
        if (code != null) {
            this.code = code;
        }
        if (StringUtils.isNotBlank(message)) {
            this.message = message;
        }
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
