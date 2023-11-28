package com.piesat.statistics.controller;


import com.piesat.statistics.bean.MonitorUserInfo;
import com.piesat.statistics.bean.SysSession;
import com.piesat.statistics.service.MonitorUserService;
import com.piesat.statistics.util.DateUtils;
import com.piesat.statistics.util.ResponseCodeEnum;
import com.piesat.statistics.util.ResultResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 2021/9/2.
 */

@RestController
public class LoginController {
    //@Autowired
    //private UserProperties userProperties;

    @Autowired
    private MonitorUserService monitorUserService;

    public static ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object ajaxLogin(@RequestBody Map<String, String> param) {

        String username = "";
        if (param.keySet().contains("username")) {
            username = param.get("username").trim();
        }
        String password = "";
        if (param.keySet().contains("password")) {
            password = param.get("password").trim();
        }
        if (StringUtils.isEmpty(username)) {
            return new ResultResponse().error(ResponseCodeEnum.ACCOUNT_IS_NULL);
        }
        if (StringUtils.isEmpty(password)) {
            return new ResultResponse().error(ResponseCodeEnum.PASSWORD_IS_NULL);
        }
        Subject subject = SecurityUtils.getSubject();
        String department = null;
        String department_name = null;

        MonitorUserInfo monitorUserInfo = monitorUserService.getUserInfoByUsername(username);
        //判断用户名密码
        if(monitorUserInfo!=null){
            if(!monitorUserInfo.getPwd().equals(password)){
                return new ResultResponse().error(ResponseCodeEnum.PASSWORD_ERROR);
            }else{
                department = monitorUserInfo.getDepartment();
                department_name = monitorUserInfo.getDepartment_name();
            }
        }else{
            return new ResultResponse().error(ResponseCodeEnum.USER_NOT_EXIST);
        }

        String sessionId = (String) subject.getSession().getId();
        //concurrentHashMap
        SysSession sysSession = new SysSession();
        Date now = new Date();
        sysSession.setId(sessionId);
        sysSession.setUsername(username);
        sysSession.setDepartment(department);
        sysSession.setCreateTime(now);
        sysSession.setExpireTime(DateUtils.getNHourAfterDate(now, 12));
        concurrentHashMap.put(sessionId,sysSession);

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        Map userMap = new TreeMap();
        userMap.put("token",sessionId);
        userMap.put("username",username);
        userMap.put("department",department);
        userMap.put("department_name",department_name);
        ret.put("data", userMap);
        return ret;
    }

    @RequestMapping(value = "/logout")
    public Object ajaxLogout(HttpServletRequest request) {
        String token = request.getHeader("token");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        concurrentHashMap.remove(token);
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        return ret;
    }

    @RequestMapping(value = "/changePwd",method = RequestMethod.POST)
    public Object changePwd(HttpServletRequest request,@RequestBody Map<String, String> param) {
        String oldpwd = "";
        if (param.keySet().contains("oldpwd")) {
            oldpwd = param.get("oldpwd").trim();
        }
        String newpwd = "";
        if (param.keySet().contains("newpwd")) {
            newpwd = param.get("newpwd").trim();
        }
        String token = request.getHeader("token");
        //1. 根据accessToken，查询用户信息
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String username = sysSession.getUsername();
        MonitorUserInfo monitorUserInfo = monitorUserService.getUserInfoByUsername(username);
        //判断用户名密码
        if(monitorUserInfo.getPwd().equals(oldpwd)){
            try {
                monitorUserService.updateUserPwd(username, newpwd);
            }catch (Exception e){
                e.printStackTrace();
                return new ResultResponse().error(ResponseCodeEnum.OPERATE_FAIL);
            }
            Map<String, Object> ret = new HashMap<>(3);
            ret.put("status", 0);
            ret.put("msg", "change pwd success");
            return ret;
        }else{
            return new ResultResponse().error(ResponseCodeEnum.PASSWORD_ERROR);
        }
    }
}
