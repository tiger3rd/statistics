package com.piesat.statistics.service;

import com.piesat.statistics.bean.MonitorUserInfo;
import com.piesat.statistics.mapper.MonitorUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MonitorUserService {
    @Resource
    MonitorUserMapper monitorUserMapper;

    public List<MonitorUserInfo> getAllUserInfo() {
        List<MonitorUserInfo> allUserInfo = monitorUserMapper.getAllUserInfo();
        return allUserInfo;
    }

    public MonitorUserInfo getUserInfoByUsername(String username){
        return monitorUserMapper.getUserInfoByUsername(username);
    }

    public void updateUserPwd(String username, String pwd){
        monitorUserMapper.updateUserPwd(username, pwd);
    }
}
