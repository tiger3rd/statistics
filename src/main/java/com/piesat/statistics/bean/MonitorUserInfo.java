package com.piesat.statistics.bean;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
/**
 * 天擎监管平台-登录账户信息表
 */
public class MonitorUserInfo {
    private String username;
    private String pwd;
    private String department;
    private String department_name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}
