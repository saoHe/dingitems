package com.ding.dingitems.util.redis.config;


/**
 * 公共静态常量
 */
public enum RedisComData {

    // 缓存中api集合keys
    APIS_KEY(104, "apikeys"),

    // 缓存库0
    DATE_BASE_0(0, ""),
    // 缓存库1
    DATE_BASE_1(1, ""),
    // 缓存库2
    DATE_BASE_2(2, ""),
    // 缓存库0
    DATE_BASE_3(3, ""),
    // 缓存库0
    DATE_BASE_4(4, ""),
    ;

    private Integer code;
    private String message;

    RedisComData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过code 取枚举
     *
     * @return
     */
    public static RedisComData getEnumByCode(Integer code) {
        for (RedisComData enums : RedisComData.values()) {
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
        for (RedisComData enums : RedisComData.values()) {
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

