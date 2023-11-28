package com.piesat.statistics.util;



/**
 * Created by Administrator on 2019/5/14.
 */
public class ResultResponse {

    public ResultEntity success(){
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setStatus(0);
        return resultEntity;
    }

    public ResultEntity error(ResponseCodeEnum responseCode){
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setStatus(responseCode.getCode());
        resultEntity.setMsg(responseCode.getMessage());
        return resultEntity;
    }
}
