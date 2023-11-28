package com.piesat.statistics.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class UserDetail implements Serializable {

    private String GROUP_ID;
    private String DEPARTMENT;
    private String DEPARTMENT_NAME;
    private String SYSTEM;
    private String USER_NAME;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CREATE_TIME;
    private Integer CATEGORYNUM;
    private Integer CALLNUM;
    private String DOWNLOAD;
    private String USER_ID;

    public String getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(String GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getSYSTEM() {
        return SYSTEM;
    }

    public void setSYSTEM(String SYSTEM) {
        this.SYSTEM = SYSTEM;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public Date getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(Date CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public Integer getCATEGORYNUM() {
        return CATEGORYNUM;
    }

    public void setCATEGORYNUM(Integer CATEGORYNUM) {
        this.CATEGORYNUM = CATEGORYNUM;
    }

    public Integer getCALLNUM() {
        return CALLNUM;
    }

    public void setCALLNUM(Integer CALLNUM) {
        this.CALLNUM = CALLNUM;
    }

    public String getDOWNLOAD() {
        return DOWNLOAD;
    }

    public void setDOWNLOAD(String DOWNLOAD) {
        this.DOWNLOAD = DOWNLOAD;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getDEPARTMENT_NAME() {
        return DEPARTMENT_NAME;
    }

    public void setDEPARTMENT_NAME(String DEPARTMENT_NAME) {
        this.DEPARTMENT_NAME = DEPARTMENT_NAME;
    }
}
