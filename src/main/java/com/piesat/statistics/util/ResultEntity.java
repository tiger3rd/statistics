package com.piesat.statistics.util;

/**
 * Created by Administrator on 2019/5/14.
 */
public class ResultEntity {
    private Integer status;
    private String msg;
    private Object result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
