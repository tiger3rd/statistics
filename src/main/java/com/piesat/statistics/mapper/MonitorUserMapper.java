package com.piesat.statistics.mapper;

import com.piesat.statistics.bean.MonitorUserInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MonitorUserMapper {

    @Select({"SELECT * FROM MONITOR_USER_INFO"})
    List<MonitorUserInfo> getAllUserInfo();

    @Select({"SELECT * FROM MONITOR_USER_INFO WHERE USERNAME = #{username} "})
    MonitorUserInfo getUserInfoByUsername(String username);

    @Update({"update MONITOR_USER_INFO SET PWD = #{pwd} WHERE USERNAME = #{username} "})
    void updateUserPwd(String username, String pwd);
}
