package com.piesat.statistics.util;

/**
 * Created by Administrator on 2019/5/14.
 */
public enum ResponseCodeEnum {
    SUCCESS(0, ""),
    ACCOUNT_IS_NULL(2000, "用户名为空"),
    PASSWORD_IS_NULL(2001, "密码为空"),
    USER_NOT_EXIST(2002, "用户不存在"),
    NOT_LOGIN_2003(401, "未登录"),
    NOT_LOGIN_2004(401, "未登录"),
    NO_PERMISSION(2005, "无权限"),
    PARAM_IS_NULL(2006, "参数为空"),
    SEARCH_USER_NOT_EXIST(2007, "查询的用户不存在"),
    PASSWORD_NOT_SAME(2008, "两次密码输入不同"),
    USER_EXIST(2009, "用户已存在"),
    NOT_OWN_OPREATION(2010, "不是用户本人操作"),
    PASSWORD_ERROR(2011, "密码错误"),
    LAT_LON_ERROR(2012, "经纬度不在海区范围内"),
    NC_FILE_IS_NULL(2013, "文件不存在"),
    PARAM_ERROR(2014, "参数错误"),
    VARIABLE_MISSING(2015, "变量定义不存在"),
    OPERATE_FAIL(9999, "操作失败");

    ResponseCodeEnum(Integer code, String message) {
        this.status = code;
        this.msg = message;
    }

    private Integer status;
    private String msg;

    public Integer getCode() {
        return status;
    }

    public void setCode(Integer code) {
        this.status = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }
}
