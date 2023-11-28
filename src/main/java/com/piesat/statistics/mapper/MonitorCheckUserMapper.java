package com.piesat.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piesat.statistics.bean.MonitorCheckUserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface MonitorCheckUserMapper extends BaseMapper<MonitorCheckUserInfo> {

    @Insert({"insert into MONITOR_USER_CHECK_INFO (user_id,checkStatus,type,year,week_Of_Year,createTime,createBy) VALUES (#{user_id},#{checkStatus},#{type},#{year},#{weekOfYear},#{createTime},#{createBy}) "})
    void saveUser(MonitorCheckUserInfo user);

    @Select({"select * from MONITOR_USER_CHECK_INFO where id=#{id}"})
    MonitorCheckUserInfo getUserById (Integer id);

    @Update({"update MONITOR_USER_CHECK_INFO set checkStatus=#{checkStatus},checkTime=#{checkTime},checkDayLimit=#{checkDayLimit},checkdesc=#{checkdesc}, " +
            "targetEmail=#{targetEmail},emailContent=#{emailContent},emailPushStatus=#{emailPushStatus},updateTime=#{updateTime},updateBy=#{updateBy} " +
            "where id=#{id}"})
    void updateUserCheck(MonitorCheckUserInfo user);

    @Update({"update MONITOR_USER_CHECK_INFO set feedbackStatus=#{feedbackStatus},measureStatus=#{measureStatus}," +
            " feedbackdesc=#{feedbackdesc}, feedbackfilepath=#{feedbackfilepath}, updateTime=#{updateTime},updateBy=#{updateBy} " +
            "where id=#{id}"})
    void updateUserFeedback(MonitorCheckUserInfo user);

    @Select({" <script>" +
            "SELECT a.*,b.USER_NAME,b.\"SYSTEM\",b.VERIFY_STATUS,b.EMAIL,b.GROUP_ID,b.DEPARTMENT,c.DEPARTMENT_NAME from MONITOR_USER_CHECK_INFO a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c " +
            " WHERE a.USER_ID=B.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID " +
            " AND a.year=#{year} AND a.week_of_year=#{week_of_year} " +
            "<when test='deaprtment !=null '>  and b.department = #{deaprtment}  </when>" +
            "<when test='type !=null '> and (a.type=#{type} or a.type like '${type},%' or a.type like '%,${type}' or a.type like '%,${type},%') </when>" +
            "<when test='checkStatus !=null '>  and a.checkStatus = #{checkStatus}  </when>" +
            "</script>"})
    List<Map<String, Object>> getList(String deaprtment, String checkStatus, String type,int year, int week_of_year);

    @Select({" <script>" +
            "SELECT a.*,b.USER_NAME,b.SYSTEM,b.EMAIL,b.DEPARTMENT,c.DEPARTMENT_NAME from MONITOR_USER_CHECK_INFO a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c " +
            " WHERE a.USER_ID=B.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.USER_ID=#{user_id} " +
            "</script>"})
    List<Map<String, Object>> getListByUserId(String user_id);

    @Select({" <script>" +
            "SELECT b.DEPARTMENT, count(1) as problemNum FROM MONITOR_USER_CHECK_INFO a,API_USER_INFO b " +
            "where a.USER_ID=b.USER_ID " +
            " AND a.year=#{year} AND a.week_of_year=#{week_of_year} " +
            "<when test='type !=null '> and (a.type=#{type} or a.type like '${type},%' or a.type like '%,${type}' or a.type like '%,${type},%') </when>" +
            "<when test='checkStatus !=null '>  and a.checkStatus = #{checkStatus}  </when>" +
            "group by b.DEPARTMENT " +
            "</script>"})
    List<Map<String, Object>> getProblemUserGroupByDepartment(String type, String checkStatus,int year, int week_of_year);

}
