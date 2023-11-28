package com.piesat.statistics.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.piesat.statistics.bean.UserInfo;
import com.piesat.statistics.pojo.vo.UserDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@InterceptorIgnore(tenantLine = "1")
public interface UserMapper {

    @Select({"SELECT * FROM API_USER_INFO"})
    List<UserInfo> getAllUserInfo();


    @Select({"SELECT * FROM API_USER_INFO"})
    List<UserInfo> getPageUserInfo(
            @Param("pageNum") int pageNum,
            @Param("pageSize") int pageSize);


    @Select({"SELECT * FROM API_USER_INFO t where t.USER_ID = #{id}"})
    UserInfo getUserInfoById(String id);

    @Select({"SELECT a.*,b.* FROM API_USER_INFO a left join API_DEPARTMENT_INFO_NEW b " +
            "on a.DEPARTMENT=b.DEPARTMENT_ID " +
            "WHERE a.USER_ID=#{userid} "})
    List<Map<String, Object>> getUserById(String userid);

    @Select({"SELECT count(1) FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP' "})
    Integer getTotalCount();

    @Select({"SELECT t.DEPARTMENT DEPARTMENT, count(USER_ID ) TOTAL FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'  GROUP BY t.DEPARTMENT"})
    List<Map<String, Object>> getTotalCountGroupByDepartment();

    @Select({"SELECT t.user_grade type, count(1) total FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP' GROUP BY t.user_grade"})
    List<Map<String, Object>> getTotalCountGroupByType();

    @Select({"SELECT t.DEPARTMENT DEPARTMENT, t.user_grade type, count(1) total FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'  GROUP BY t.DEPARTMENT, t.user_grade"})
    List<Map<String, Object>> getTotalCountGroupByDeparmentAndType();

    @Select({"SELECT count(1) FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'  AND t.CREATE_TIME >= #{startTime} and t.CREATE_TIME<= #{endTime}"})
    Integer getNewCount(String startTime, String endTime);

    @Select({"SELECT count(1) FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'N' AND user_grade!='OTHER_GROUP'  AND t.VERIFY_TIME >= #{startTime} and t.VERIFY_TIME<= #{endTime}"})
    Integer getDisableCount(String startTime, String endTime);

    @Select({"SELECT t.user_grade type, count(1) total FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'  AND t.CREATE_TIME >= #{startTime} and t.CREATE_TIME<= #{endTime} GROUP BY t.user_grade"})
    List<Map<String, Object>> getNewCountGroupByType(String startTime, String endTime);

    @Select({"SELECT t.DEPARTMENT DEPARTMENT, count(1) total FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'  AND t.CREATE_TIME >= #{startTime} and t.CREATE_TIME<= #{endTime} GROUP BY t.DEPARTMENT"})
    List<Map<String, Object>> getNewCountGroupByDepartment(String startTime, String endTime);

    @Select({"SELECT * FROM (SELECT t.DEPARTMENT DEPARTMENT,max(t2.DEPARTMENT_NAME) as DEPARTMENT_NAME ,max(t2.SERIAL_NO) as DEPARTMENT_NO FROM API_USER_INFO t,API_DEPARTMENT_INFO_NEW t2  " +
            "WHERE t.DEPARTMENT=t2.DEPARTMENT_ID " +
            "AND t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'   " +
            "AND t.CREATE_TIME >= #{startTime} and t.CREATE_TIME<= #{endTime}  " +
            "GROUP BY t.DEPARTMENT " +
            "order by sum(1) desc " +
            " ) where rownum<=7 ORDER BY DEPARTMENT_NO ASC"})
    List<Map<String, Object>> getHeaderNewCountGroupByDepartment(String startTime, String endTime);

    @Select({"SELECT t.user_grade type, count(1) total FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'  AND t.CREATE_TIME<= #{endTime} GROUP BY t.user_grade"})
    List<Map<String, Object>> getCountGroupByType(String endTime);

    @Select({"SELECT t.DEPARTMENT DEPARTMENT, count(1) total FROM API_USER_INFO t WHERE t.VERIFY_STATUS = 'Y' AND user_grade!='OTHER_GROUP'  AND t.CREATE_TIME<= #{endTime} GROUP BY t.DEPARTMENT"})
    List<Map<String, Object>> getCountGroupByDepartment(String endTime);

    @Select({"SELECT " +
            "   count(DISTINCT t2.USER_ID) " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y'  " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} "})
    Integer getTotalActiveCount(String startTime, String endTime);

    @Select({"SELECT " +
            "   t2.user_grade TYPE, " +
            "   count(DISTINCT t2.USER_ID) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP' " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t2.user_grade"})
    List<Map<String, Object>> getTotalActiveCountByType(String startTime, String endTime);

    @Select({"SELECT " +
            "   t2.DEPARTMENT DEPARTMENT, " +
            "   count(DISTINCT t2.USER_ID) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t2.DEPARTMENT"})
    List<Map<String, Object>> getActiveCountByDepartment(String startTime, String endTime);

    @Select({"SELECT " +
            "   sum(t1.QUERY_TIMES ) " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1 " +
            " WHERE " +
            "   t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime}"})
    Integer getAccessCount(String startTime, String endTime);

    @Select({"SELECT " +
            "   t2.user_grade TYPE, " +
            "   sum(t1.QUERY_TIMES ) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t2.user_grade "})
    List<Map<String, Object>> getAccessCountByType(String startTime, String endTime);

    @Select({"SELECT " +
            "   t2.DEPARTMENT DEPARTMENT, " +
            "   sum( t1.QUERY_TIMES ) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2" +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t2.DEPARTMENT"})
    List<Map<String, Object>> getAccessCountByDepartment(String startTime, String endTime);

    @Select({"select * from (SELECT " +
            "   t2.DEPARTMENT DEPARTMENT,max(t3.DEPARTMENT_NAME) as DEPARTMENT_NAME, max(t3.SERIAL_NO) as DEPARTMENT_NO " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2," +
            "   API_DEPARTMENT_INFO_NEW t3" +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.DEPARTMENT=t3.DEPARTMENT_ID" +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t2.DEPARTMENT" +
            " order by sum( t1.QUERY_TIMES) desc " +
            " ) where rownum<=7 order by DEPARTMENT_NO asc"})
    List<Map<String, Object>> getHeaderAccessCountDepartment(String startTime, String endTime);

    @Select({"SELECT " +
            "   t2.DEPARTMENT DEPARTMENT,max(t3.DEPARTMENT_NAME) as DEPARTMENT_NAME" +
            " FROM " +
            "   API_PERFORMANCE_HOR t1, " +
            "   API_USER_INFO t2," +
            "   API_DEPARTMENT_INFO_NEW t3" +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.DEPARTMENT=t3.DEPARTMENT_ID" +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t2.DEPARTMENT" +
            " order by sum( t1.QUERY_TIMES) desc limit 7"})
    List<Map<String, Object>> getHeaderAccessHorCountDepartment(String startTime, String endTime);

    @Select({"SELECT " +
            "   t1.D_DATETIME DATATIME, " +
            "   t2.user_grade TYPE, " +
            "   sum( t1.QUERY_TIMES ) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t1.D_DATETIME, t2.user_grade"})
    List<Map<String, Object>> getAccessCountByDayAndType(String startTime, String endTime);

    @Select({"SELECT " +
            "   t1.D_DATETIME DATATIME, " +
            "   t2.user_grade TYPE, " +
            "   sum( t1.QUERY_TIMES ) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_HOR t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t1.D_DATETIME, t2.user_grade"})
    List<Map<String, Object>> getAccessCountByHorAndType(String startTime, String endTime);

    @Select({"SELECT " +
            "   t1.D_DATETIME DATATIME, " +
            "   t2.DEPARTMENT , " +
            "   sum( t1.QUERY_TIMES ) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t1.D_DATETIME, t2.DEPARTMENT"})
    List<Map<String, Object>> getAccessCountByDayAndDepartment(String startTime, String endTime);

    @Select({"SELECT " +
            "   t1.D_DATETIME DATATIME, " +
            "   t2.DEPARTMENT , " +
            "   sum( t1.QUERY_TIMES ) TOTAL " +
            " FROM " +
            "   API_PERFORMANCE_HOR t1, " +
            "   API_USER_INFO t2 " +
            " WHERE " +
            "   t1.SYS_USER_ID = t2.USER_ID " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime} " +
            "   AND t1.D_DATETIME <= #{endTime} " +
            " GROUP BY t1.D_DATETIME, t2.DEPARTMENT"})
    List<Map<String, Object>> getAccessCountByHorAndDepartment(String startTime, String endTime);

    @Select({"SELECT " +
            "   t.D_DATETIME DATATIME, " +
            "   COUNT(DISTINCT t.API_DATA_CODE ) KIND, " +
            "   SUM(t.QUERY_TIMES ) ACCESS," +
            "   SUM(t.DATA_SIZE) VOLUME " +
            " FROM " +
            "   API_PERFORMANCE_DAY t " +
            " WHERE " +
            "   t.SYS_USER_ID = #{userId} " +
            "   AND t.D_DATETIME >= #{startTime} " +
            "   AND t.D_DATETIME <= #{endTime} " +
            " GROUP BY " +
            "   t.D_DATETIME " +
            " ORDER BY t.D_DATETIME"})
    List<Map<String, Object>> getSingleUserStatisticsByDay(String userId, String startTime, String endTime);

    @Select({"SELECT " +
            "   t.D_DATETIME DATATIME, " +
            "   COUNT(DISTINCT t.API_DATA_CODE ) KIND, " +
            "   SUM(t.QUERY_TIMES ) ACCESS," +
            "   SUM(t.DATA_SIZE) VOLUME " +
            " FROM " +
            "   API_PERFORMANCE_HOR t " +
            " WHERE " +
            "   t.SYS_USER_ID = #{userId} " +
            "   AND t.D_DATETIME >= #{startTime} " +
            "   AND t.D_DATETIME <= #{endTime} " +
            " GROUP BY " +
            "   t.D_DATETIME " +
            " ORDER BY t.D_DATETIME"})
    List<Map<String, Object>> getSingleUserStatisticsByHor(String userId, String startTime, String endTime);

    @Select({"SELECT " +
            "   * " +
            " FROM " +
            "   API_USER_INFO t1, " +
            "   API_DEPARTMENT_INFO_NEW t2 " +
            " WHERE " +
            "   t1.DEPARTMENT = t2.DEPARTMENT_ID " +
            "   AND t1.user_grade!='OTHER_GROUP'  " +
            "   AND t1.VERIFY_STATUS = 'Y'   " +
            "   AND t2.IS_COUNTRY = #{country}"})
    List<Map<String, Object>> getUserListByCountry(int country);

    @Select({"SELECT     " +
            "     t2.DATA_CLASS_ID DATACLASSID,     " +
            "     SUM(t1.DATA_SIZE) TOTAL     " +
            " FROM     " +
            "     API_PERFORMANCE_DAY t1,     " +
            "     API_DATA_DEFINE t2     " +
            " WHERE     " +
            "     t1.API_DATA_CODE = t2.DATA_CODE     " +
            "     AND t1.D_DATETIME >= #{startTime}     " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            "     AND t1.SYS_USER_ID = #{userId}     " +
            " GROUP BY     " +
            "     t2.DATA_CLASS_ID"})
    List<Map<String, Object>> getVolumeByUserGroupByDataClassID(String userId, String startTime, String endTime);

    @Select({"SELECT     " +
            "     t2.DATA_CLASS_ID DATACLASSID,     " +
            "     SUM(t1.DATA_SIZE) TOTAL     " +
            " FROM     " +
            "     API_PERFORMANCE_HOR t1,     " +
            "     API_DATA_DEFINE t2     " +
            " WHERE     " +
            "     t1.API_DATA_CODE = t2.DATA_CODE     " +
            "     AND t1.D_DATETIME >= #{startTime}     " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            "     AND t1.SYS_USER_ID = #{userId}     " +
            " GROUP BY     " +
            "     t2.DATA_CLASS_ID"})
    List<Map<String, Object>> getVolumeByUserGroupByDataClassIDUseHorTable(String userId, String startTime, String endTime);

    @Select({"SELECT     " +
            "     t1.API_DATA_CODE DATACODE,    " +
            "     t2.DATA_CLASS_ID DATACLASSID,   " +
            "     max(t1.D_DATETIME) MAXTIME, " +
            "     min(t1.D_DATETIME) MINTIME, " +
            "     sum(t1.QUERY_TIMES ) ACCESS,     " +
            "     SUM(t1.DATA_SIZE) VOLUME     " +
            " FROM     " +
            "     API_PERFORMANCE_HOR t1, " +
            "     API_DATA_DEFINE t2     " +
            " WHERE     " +
            "     t1.API_DATA_CODE = t2.DATA_CODE" +
            "     AND t1.D_DATETIME >= #{startTime}    " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            "     AND t1.SYS_USER_ID = #{userId}     " +
            " GROUP BY     " +
            "     t1.API_DATA_CODE, " +
            "     t2.DATA_CLASS_ID"})
    List<Map<String, Object>> getDetailByUser(String userId, String startTime, String endTime);

    @Select({"SELECT     " +
            "     t1.D_DATETIME DATATIME,      " +
            "     t1.SYS_USER_ID USERID,     " +
            "     count(DISTINCT t1.API_DATA_CODE ) DATACOUNT,     " +
            "     sum(t1.DATA_SIZE ) VOLUME,     " +
            "     sum(t1.QUERY_TIMES ) CALLNUM     " +
            " FROM     " +
            "     API_PERFORMANCE_DAY t1     " +
            " WHERE     " +
            "     t1.D_DATETIME >= #{startTime}     " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            " GROUP BY t1.D_DATETIME, t1.SYS_USER_ID "})
    List<Map<String, Object>> getTotalUserAndDataAndVolume(String startTime, String endTime);

    @Select({"SELECT     " +
            "     t1.D_DATETIME DATATIME,      " +
            "     t1.SYS_USER_ID USERID,     " +
            "     count(DISTINCT t1.API_DATA_CODE ) DATACOUNT,     " +
            "     sum(t1.DATA_SIZE ) VOLUME," +
            "     sum(t1.QUERY_TIMES) CALLNUM    " +
            " FROM     " +
            "     API_PERFORMANCE_HOR t1     " +
            " WHERE     " +
            "     t1.D_DATETIME >= #{startTime}     " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            " GROUP BY t1.D_DATETIME, t1.SYS_USER_ID "})
    List<Map<String, Object>> getTotalUserAndDataAndVolumeHor(String startTime, String endTime);

    @Select({"SELECT     " +
            "     t1.D_DATETIME DATATIME,      " +
            "     t1.SYS_USER_ID USERID,     " +
            "     count(DISTINCT t1.API_DATA_CODE ) DATACOUNT,     " +
            "     sum(t1.DATA_SIZE ) VOLUME," +
            "     sum(t1.QUERY_TIMES) CALLNUM " +
            " FROM     " +
            "     API_PERFORMANCE_HOR t1     " +
            " WHERE     " +
            "     t1.D_DATETIME >= #{startTime}     " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            " GROUP BY t1.D_DATETIME, t1.SYS_USER_ID "})
    List<Map<String, Object>> getTotalUserAndDataAndVolumeByHor(String startTime, String endTime);

    @Select({"SELECT     " +
            "     t1.D_DATETIME    " +
            " FROM     " +
            "     API_PERFORMANCE_HOR t1     " +
            " WHERE     " +
            "     t1.D_DATETIME >= #{startTime}    " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            "     AND t1.SYS_USER_ID = #{userId}   " +
            "     AND t1.API_DATA_CODE = #{dataCode} " +
            " ORDER BY     " +
            "     t1.D_DATETIME desc" +
            " LIMIT 10000"})
    List<String> accessTimes(String userId, String dataCode, String startTime, String endTime);

    @Select("<script> " +
            "select DEPARTMENT,DEPARTMENT_NAME,USERNUMBER from " +
            "( select DEPARTMENT,count(*) as usernumber from API_USER_INFO " +
            "WHERE VERIFY_STATUS ='Y' AND CREATE_TIME &gt;= #{startTime} AND CREATE_TIME &lt;= #{endTime} " +
            " and user_grade !='OTHER_GROUP' "+
            "<when test='usertype !=null'>  and user_grade = #{usertype}  </when>" +
            "GROUP BY DEPARTMENT " +
            ") a, API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "<when test='isnation !=null'>  and b.is_country = #{isnation}  </when>" +
            "order by usernumber desc " +
            "</script>")
    List<Map<String, Object>> userByType(String usertype,String isnation, String startTime, String endTime);

    @Select("<script> " +
            "select DEPARTMENT,DEPARTMENT_NAME,USERNUMBER from " +
            "( select DEPARTMENT,count(*) as usernumber from API_USER_INFO " +
            "WHERE VERIFY_STATUS ='Y' AND CREATE_TIME &gt;= #{startTime} AND CREATE_TIME &lt;= #{endTime} " +
            " and user_grade !='OTHER_GROUP' "+
            "<when test='usertype !=null'>  and user_grade = #{usertype}  </when>" +
            "AND USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME &gt;= #{startTime1} AND D_DATETIME &lt;= #{endTime1} ) " +
            "GROUP BY DEPARTMENT " +
            ") a, API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "<when test='isnation !=null'>  and b.is_country = #{isnation}  </when>" +
            "order by usernumber desc " +
            "</script>")
    List<Map<String, Object>> userByTypeByActive(String usertype,String isnation, String startTime, String endTime, String startTime1, String endTime1);

    @Select("<script> " +
            "select DEPARTMENT,DEPARTMENT_NAME,USERNUMBER from " +
            "( select DEPARTMENT,count(*) as usernumber from API_USER_INFO " +
            "WHERE VERIFY_STATUS ='Y' AND CREATE_TIME &gt;= #{startTime} AND CREATE_TIME &lt;= #{endTime} " +
            " and user_grade !='OTHER_GROUP' "+
            "<when test='usertype !=null'>  and user_grade = #{usertype}  </when>" +
            "AND USER_ID NOT IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME &gt;= #{startTime1} AND D_DATETIME &lt;= #{endTime1} ) " +
            "GROUP BY DEPARTMENT " +
            ") a, API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "<when test='isnation !=null'>  and b.is_country = #{isnation}  </when>" +
            "order by usernumber desc " +
            "</script>")
    List<Map<String, Object>> userByTypeByNotActive(String usertype,String isnation, String startTime, String endTime, String startTime1, String endTime1);

    @Select({"<script> select count(*) as totalnum from API_USER_INFO a, API_DEPARTMENT_INFO_NEW b " +
            "WHERE VERIFY_STATUS ='Y' AND CREATE_TIME &gt;= #{startTime} AND CREATE_TIME &lt;= #{endTime}  " +
            " and user_grade !='OTHER_GROUP' "+
            "AND a.DEPARTMENT=b.DEPARTMENT_ID " +
            "<when test='usertype !=null'>  and user_grade = #{usertype}  </when>" +
            "<when test='isnation !=null'>  and b.is_country = #{isnation}  </when>" +
            "</script>"})
    Integer userCountType(String usertype,String isnation,String startTime, String endTime);

    @Select({"<script> select count(*) as totalnum from API_USER_INFO a, API_DEPARTMENT_INFO_NEW b " +
            "WHERE VERIFY_STATUS ='Y' " +
            " and user_grade !='OTHER_GROUP' "+
            "AND USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME &gt;= #{startTime1} AND D_DATETIME &lt;= #{endTime1} ) " +
            "AND a.DEPARTMENT=b.DEPARTMENT_ID " +
            "<when test='isnation !=null'>  and b.is_country = #{isnation}  </when>" +
            "</script>"})
    Integer userCountTypeByActive(String usertype,String isnation,String startTime, String endTime, String startTime1, String endTime1);


    @Select({"<script> select count(*) as totalnum from API_USER_INFO a, API_DEPARTMENT_INFO_NEW b " +
            "WHERE VERIFY_STATUS ='Y' AND CREATE_TIME &gt;= #{startTime} AND CREATE_TIME &lt;= #{endTime} " +
            " and user_grade !='OTHER_GROUP' "+
            "AND USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME &gt;= #{startTime1} AND D_DATETIME &lt;= #{endTime1} ) " +
            "AND a.DEPARTMENT=b.DEPARTMENT_ID " +
            "<when test='isnation !=null'>  and b.is_country = #{isnation}  </when>" +
            "</script>"})
    Integer userCountTypeByNotActive(String usertype,String isnation,String startTime, String endTime, String startTime1, String endTime1);

    @Select("<script> " +
            "select DATA_CODE,DATA_NAME,callnum,download,b.DATA_CLASS_ID,c.DATA_CLASS_NAME from " +
            "(" +
            "select API_DATA_CODE,SUM(QUERY_TIMES) AS callnum, SUM(DATA_SIZE) AS download from API_PERFORMANCE_DAY " +
            "where D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime} " +
            "AND SYS_USER_ID=#{userid} " +
            "group by API_DATA_CODE " +
            ")a,API_DATA_DEFINE b,API_DATA_CLASS_DEFINE c   " +
            "where a.API_DATA_CODE=b.DATA_CODE AND b.DATA_CLASS_ID=c.DATA_CLASS_ID " +
            "order by callnum desc,download desc " +
            "</script>")
    List<Map<String, Object>> userCategoryDetail(String userid,String startTime, String endTime);

    @Select("<script> " +
            "select DATA_CODE,DATA_NAME,D_DATETIME,callnum,download from " +
            "(" +
            "select API_DATA_CODE,D_DATETIME,SUM(QUERY_TIMES) AS callnum, SUM(DATA_SIZE) AS download from API_PERFORMANCE_DAY " +
            "where D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime} " +
            "AND SYS_USER_ID=#{userid} " +
            "group by API_DATA_CODE,D_DATETIME " +
            ")a,API_DATA_DEFINE b " +
            "where a.API_DATA_CODE=b.DATA_CODE " +
            "order by API_DATA_CODE asc,D_DATETIME asc,callnum desc,download desc" +
            "</script>")
    List<Map<String, Object>> userAccessDetail(String userid,String startTime, String endTime);

    @Select("<script> " +
            "SELECT A.user_grade,A.DEPARTMENT,D.DEPARTMENT_NAME,A.SYSTEM,A.USER_NAME,A.CREATE_TIME,B.CATEGORYNUM,C.CALLNUM,C.DOWNLOAD,A.USER_ID FROM API_USER_INFO A,( " +
            "SELECT SYS_USER_ID,COUNT(*) AS CATEGORYNUM FROM( " +
            "SELECT SYS_USER_ID,API_DATA_CODE FROM API_PERFORMANCE_DAY " +
            "WHERE D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime} GROUP BY SYS_USER_ID,API_DATA_CODE " +
            ") GROUP BY SYS_USER_ID " +
            ") B,( " +
            "SELECT SYS_USER_ID,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY " +
            "WHERE D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime} GROUP BY SYS_USER_ID " +
            ") C,API_DEPARTMENT_INFO_NEW D WHERE A.USER_ID=B.SYS_USER_ID AND A.USER_ID=C.SYS_USER_ID AND A.DEPARTMENT=D.DEPARTMENT_ID " +
            "AND A.VERIFY_STATUS='Y' " +
            "   AND A.user_grade!='OTHER_GROUP'  " +
            "<when test='usertype !=null'>  and user_grade = #{usertype}  </when>" +
            "<when test='department !=null'>  and DEPARTMENT = #{department}  </when>" +
            "ORDER BY user_grade ASC,A.DEPARTMENT ASC,CATEGORYNUM DESC,CALLNUM DESC " +
            "</script>")
    List<UserDetail> userByTypeAndDep(String usertype, String department, String startTime, String endTime);


    @Select({"SELECT user_grade, count(1) as NUM FROM API_USER_INFO  WHERE VERIFY_STATUS = 'Y' " +
            " and user_grade !='OTHER_GROUP' "+
            "AND CREATE_TIME>= #{startTime} AND CREATE_TIME<= #{endTime}  GROUP BY user_grade"})
    List<Map<String, Object>> userTypeCount(String startTime, String endTime);

    @Select({"SELECT user_grade, count(1) as NUM FROM API_USER_INFO  WHERE VERIFY_STATUS = 'Y' " +
            " and user_grade !='OTHER_GROUP' "+
            "AND CREATE_TIME>= #{startTime} AND CREATE_TIME<= #{endTime}  GROUP BY user_grade"})
    List<Map<String, Object>> newUserToalGroupByUsertype(String startTime, String endTime);

    @Select({"SELECT user_grade, count(1) as NUM FROM API_USER_INFO  WHERE VERIFY_STATUS = 'Y' " +
            "and user_grade !='OTHER_GROUP' " +
            "AND USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} ) " +
            "GROUP BY user_grade " +
            "order by NUM desc"})
    List<Map<String, Object>> activeUserToalGroupByUsertype(String startTime, String endTime);

    @Select({"SELECT user_grade, count(1) as NUM FROM API_USER_INFO  " +
            "WHERE VERIFY_STATUS = 'Y' AND CREATE_TIME >= #{startTime} AND CREATE_TIME <= #{endTime}  " +
            "AND USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime1} AND D_DATETIME <= #{endTime1} ) " +
            " GROUP BY user_grade"})
    List<Map<String, Object>> userTypeCountActive(String startTime, String endTime, String startTime1, String endTime1);

    @Select({"SELECT user_grade, count(1) as NUM FROM API_USER_INFO  " +
            "WHERE VERIFY_STATUS = 'Y' AND CREATE_TIME >= #{startTime} AND CREATE_TIME <= #{endTime}  " +
            "AND USER_ID NOT IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime1} AND D_DATETIME <= #{endTime1} ) " +
            " GROUP BY user_grade"})
    List<Map<String, Object>> userTypeCountNoActive(String startTime, String endTime, String startTime1, String endTime1);


    @Select({"select DEPARTMENT,max(DEPARTMENT_NAME) as DEPARTMENT_NAME, count(*) as usernumber from API_USER_INFO a,API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "and a.VERIFY_STATUS ='Y'  " +
            "and a.user_grade !='OTHER_GROUP' " +
            "and a.user_grade =#{usertype} " +
            "and USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} ) " +
            "and b.is_country=#{isnation} " +
            "group by a.DEPARTMENT " +
            "order by usernumber desc"})
    List<Map<String, Object>> activeuserTotalByUsertypeGroupByDepartment(String usertype,String isnation,String startTime, String endTime);

    @Select({"<script> SELECT user_grade, count(1) as NUM FROM API_USER_INFO  WHERE VERIFY_STATUS = 'Y' " +
            "and user_grade !='OTHER_GROUP' " +
            "AND USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime} ) " +
            "<when test='tokenDepartment !=null'> and DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY user_grade " +
            "order by NUM desc </script>"})
    List<Map<String, Object>> activeUserTotal(String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT user_grade, count(1) as NUM FROM API_USER_INFO  WHERE VERIFY_STATUS = 'Y' " +
            "and user_grade !='OTHER_GROUP' " +
            "AND CREATE_TIME &gt;= #{startTime} AND CREATE_TIME &lt;= #{endTime} " +
            "<when test='tokenDepartment !=null'> and DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY user_grade " +
            "order by NUM desc </script>"})
    List<Map<String, Object>> newUserTotal(String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT user_grade, count(1) as NUM FROM API_USER_INFO  WHERE VERIFY_STATUS = 'Y' " +
            "and user_grade !='OTHER_GROUP' " +
            "<when test='tokenDepartment !=null'> and DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY user_grade " +
            "order by NUM desc </script>"})
    List<Map<String, Object>> basicuserTotal(String tokenDepartment);

    @Select("select DEPARTMENT,max(DEPARTMENT_NAME) as DEPARTMENT_NAME, count(*) as usernumber from API_USER_INFO a,API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "and a.VERIFY_STATUS ='Y' " +
            "and a.user_grade !='OTHER_GROUP' " +
            "and a.user_grade =#{usertype} " +
            "and USER_ID IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} ) " +
            "and b.is_country=#{is_country} " +
            "group by a.DEPARTMENT " +
            "order by usernumber desc ")
    List<Map<String, Object>> activeUserDepList(String usertype,String is_country, String startTime, String endTime);

    @Select({"select DEPARTMENT,max(DEPARTMENT_NAME) as DEPARTMENT_NAME, count(*) as usernumber from API_USER_INFO a,API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "and a.VERIFY_STATUS ='Y'  " +
            "and a.user_grade !='OTHER_GROUP' " +
            "and a.user_grade = #{usertype} " +
            "and USER_ID NOT IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} ) " +
            "and b.is_country=#{is_country} " +
            "group by a.DEPARTMENT " +
            "order by usernumber desc "})
    List<Map<String, Object>> notActiveUserDepList(String usertype,String is_country, String startTime, String endTime);


    @Select("select DEPARTMENT,max(DEPARTMENT_NAME) as DEPARTMENT_NAME, count(*) as usernumber from API_USER_INFO a,API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "and a.VERIFY_STATUS ='Y'  " +
            "and a.user_grade !='OTHER_GROUP' " +
            "and a.user_grade = #{usertype} " +
            "AND CREATE_TIME >= #{startTime} AND CREATE_TIME <= #{endTime}   " +
            "and b.is_country = #{is_country} " +
            "group by a.DEPARTMENT " +
            "order by usernumber desc ")
    List<Map<String, Object>> newUserDepList(String usertype,String is_country, String startTime, String endTime);

    @Select({"select DEPARTMENT,max(DEPARTMENT_NAME) as DEPARTMENT_NAME, count(*) as usernumber from API_USER_INFO a,API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "and a.VERIFY_STATUS ='Y'  " +
            "and a.user_grade != 'OTHER_GROUP' " +
            "and a.user_grade =#{usertype}  " +
            "group by a.DEPARTMENT " +
            "order by usernumber desc "})
    List<Map<String, Object>> basicUserDepList(String usertype);


    @Select({"SELECT b.system,c.DEPARTMENT_NAME,b.DEPARTMENT,b.USER_NAME,b.USER_ID,a.CALLNUM,a.DOWNLOAD FROM ( " +
            "SELECT SYS_USER_ID,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS download FROM " +
            "API_PERFORMANCE_DAY " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} GROUP BY SYS_USER_ID " +
            ") a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c " +
            "where a.SYS_USER_ID=b.USER_ID and b.DEPARTMENT=c.DEPARTMENT_ID AND b.user_grade = #{usertype} " +
            "order by callnum desc,download desc "})
    List<Map<String, Object>> accessCountByUserType(String usertype, String startTime, String endTime);

    @Select({"SELECT D_DATETIME,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS download FROM " +
            "API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.user_grade = #{usertype} " +
            "AND D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} GROUP BY D_DATETIME " +
            "order by D_DATETIME ASC "})
    List<Map<String, Object>> accessCountByUserTypeGroupBy(String usertype, String startTime, String endTime);


    @Select({"SELECT max(b.user_grade) as user_grade,max(b.DEPARTMENT) as DEPARTMENT,max(c.DEPARTMENT_NAME) as DEPARTMENT_NAME, max(b.SYSTEM) as SYSTEM, max(b.USER_NAME) as USER_NAME,max(b.CREATE_TIME) as CREATE_TIME,USER_ID,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS download,count(distinct API_DATA_CODE) as CATEGORYNUM  " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c  " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID  " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "and b.user_grade = #{usertype}  " +
            "and b.DEPARTMENT = #{department}  " +
            "and c.is_country= #{is_country} " +
            "GROUP BY USER_ID  " +
            "ORDER BY CALLNUM DESC  " +
            " "})
    List<UserDetail> activeUserList(String usertype,String department, String is_country, String startTime, String endTime);

    @Select({"select a.user_grade,a.DEPARTMENT,b.DEPARTMENT_NAME,SYSTEM,USER_NAME,CREATE_TIME,USER_ID,0 AS CALLNUM, 0 AS download,0 as CATEGORYNUM from API_USER_INFO a,API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "and a.VERIFY_STATUS ='Y'  " +
            "and a.user_grade !='OTHER_GROUP' " +
            "and a.user_grade = #{usertype} " +
            "and a.DEPARTMENT = #{department}  " +
            "and USER_ID NOT IN (SELECT DISTINCT SYS_USER_ID FROM API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} ) " +
            "and b.is_country= #{is_country} " +
            "order by USER_ID asc "})
    List<UserDetail> notActiveUserList(String usertype,String department, String is_country, String startTime, String endTime);


    @Select({"SELECT max(b.user_grade) as user_grade,max(b.DEPARTMENT) as DEPARTMENT,max(c.DEPARTMENT_NAME) as DEPARTMENT_NAME, max(b.SYSTEM) as SYSTEM, max(b.USER_NAME) as USER_NAME,max(b.CREATE_TIME) as CREATE_TIME, " +
            "USER_ID,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS download,count(distinct API_DATA_CODE) as CATEGORYNUM  " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c  " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID  " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{recordStartTime} AND a.D_DATETIME <= #{recordEndTime}  " +
            "AND b.CREATE_TIME >= #{createStartTime} AND b.CREATE_TIME <= #{createEndTime}   " +
            "and b.user_grade = #{usertype}  " +
            "and b.DEPARTMENT = #{department}  " +
            "and c.is_country= #{is_country} " +
            "GROUP BY USER_ID  " +
            "ORDER BY CALLNUM DESC  " +
            " "})
    List<UserDetail> newUserList(String usertype,String department, String is_country,
                                 String recordStartTime, String recordEndTime, String createStartTime, String createEndTime);


    @Select({"SELECT max(b.user_grade) as user_grade,max(b.DEPARTMENT) as DEPARTMENT,max(c.DEPARTMENT_NAME) as DEPARTMENT_NAME, max(b.SYSTEM) as SYSTEM, max(b.USER_NAME) as USER_NAME,max(b.CREATE_TIME) as CREATE_TIME,USER_ID,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS download,count(distinct API_DATA_CODE) as CATEGORYNUM  " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c  " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID  " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "and b.user_grade = #{usertype}  " +
            "and b.DEPARTMENT = #{department}  " +
            "GROUP BY USER_ID  " +
            "ORDER BY CALLNUM DESC  "})
    List<UserDetail> basicUserList(String usertype,String department, String startTime, String endTime);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,MAX(DEPARTMENT_NAME) AS DEPARTMENT_NAME,max(c.SERIAL_NO) AS DEPARTMENT_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "GROUP BY b.DEPARTMENT " +
            "order by DEPARTMENT_NO asc </script>")
    List<Map<String, Object>> userDepList(String usertype,String is_country, String startTime, String endTime, String tokenDepartment);

    @Select("SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.user_grade " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "and b.department = #{department}  " +
            "GROUP BY b.user_grade order by b.user_grade desc")
    List<Map<String, Object>> depUserList(String department, String startTime, String endTime);

    @Select("<script> SELECT t1.*, " +
            "       CASE " +
            "           WHEN callnum ISNULL THEN 0 " +
            "           ELSE callnum " +
            "       END AS callnum, " +
            "       CASE " +
            "           WHEN download ISNULL THEN 0 " +
            "           ELSE download " +
            "       END AS download, " +
            "       CASE " +
            "           WHEN categorynum ISNULL THEN 0 " +
            "           ELSE categorynum " +
            "       END AS categorynum " +
            "FROM " +
            "  (SELECT user_grade, " +
            "          COUNT(1) AS USERNUM " +
            "   FROM API_USER_INFO " +
            "   WHERE VERIFY_STATUS = 'Y' " +
            "     AND user_grade != 'OTHER_GROUP' " +
            "     AND department = #{department} " +
            "   GROUP BY user_grade) t1 " +
            "LEFT JOIN " +
            "  (SELECT SUM(QUERY_TIMES) AS CALLNUM, " +
            "          SUM(DATA_SIZE) AS DOWNLOAD, " +
            "          count(DISTINCT API_DATA_CODE) AS CATEGORYNUM, " +
            "          b.user_grade " +
            "   FROM API_PERFORMANCE_DAY a, " +
            "        API_USER_INFO b, " +
            "        API_DEPARTMENT_INFO_NEW c, " +
            "        API_DATA_DEFINE d, " +
            "        API_DATA_CLASS_DEFINE e " +
            "   WHERE a.SYS_USER_ID=b.USER_ID " +
            "     AND b.DEPARTMENT=c.DEPARTMENT_ID " +
            "     AND a.API_DATA_CODE=d.DATA_CODE " +
            "     AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "     AND b.VERIFY_STATUS='Y' " +
            "     AND b.user_grade !='OTHER_GROUP' " +
            "     AND a.D_DATETIME &gt;= #{startTime} " +
            "     AND a.D_DATETIME &lt;= #{endTime} " +
            "     AND b.DEPARTMENT = #{department} " +
            "   GROUP BY b.user_grade) t2 ON t1.user_grade = t2.user_grade " +
            "ORDER BY user_grade DESC </script>")
    List<Map<String, Object>> basicDepUserList(String department, String startTime, String endTime);

    @Select("<script> " +
            "SELECT t1.* " +
            ",case when callnum isnull then 0 else callnum end as callnum  " +
            ",case when download isnull then 0 else download end as download  " +
            ",case when categorynum isnull then 0 else categorynum end as categorynum  " +
            "FROM  " +
            "    (SELECT m.*, " +
            "         n.DEPARTMENT_NAME, " +
            "         n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM  " +
            "        (SELECT DEPARTMENT, " +
            "         COUNT(1) AS USERNUM " +
            "        FROM API_USER_INFO " +
            "        WHERE VERIFY_STATUS = 'Y' " +
            "                AND user_grade != 'OTHER_GROUP' " +
            "                AND CREATE_TIME &gt;= #{usreCreateStartTime} " +
            "                AND CREATE_TIME &lt;= #{userCreateEndTime} " +
            "                AND user_grade = #{usertype} " +
            "<when test='tokenDepartment !=null'> and DEPARTMENT = #{tokenDepartment} </when>"+
            "        GROUP BY  DEPARTMENT ) m, API_DEPARTMENT_INFO_NEW n " +
            "        WHERE m.DEPARTMENT=n.DEPARTMENT_ID " +
            "                AND n.is_country= #{is_country} ) t1 " +
            "    LEFT JOIN  " +
            "    (SELECT SUM(QUERY_TIMES) AS CALLNUM, " +
            "         SUM(DATA_SIZE) AS DOWNLOAD, " +
            "         count(distinct API_DATA_CODE) AS CATEGORYNUM , " +
            "         b.DEPARTMENT " +
            "    FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID=b.USER_ID " +
            "            AND b.DEPARTMENT=c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE=d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "            AND b.VERIFY_STATUS='Y' " +
            "            AND b.user_grade !='OTHER_GROUP' " +
            "            AND a.D_DATETIME &gt;= #{startTime} " +
            "            AND a.D_DATETIME &lt;= #{endTime} " +
            "            AND b.CREATE_TIME &gt;= #{usreCreateStartTime} " +
            "            AND b.CREATE_TIME &lt;= #{userCreateEndTime} " +
            "            AND b.user_grade = #{usertype} " +
            "            AND c.is_country= #{is_country} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "    GROUP BY  b.DEPARTMENT ) t2 " +
            "    ON t1.DEPARTMENT = t2.DEPARTMENT " +
            "ORDER BY  DEPARTMENT_NO asc" +
            " </script>")
    List<Map<String, Object>> newUserDepList2(String usertype,String is_country, String startTime, String endTime, String usreCreateStartTime, String userCreateEndTime, String tokenDepartment);

    @Select("<script> " +
            "SELECT t1.* " +
            ",case when callnum isnull then 0 else callnum end as callnum  " +
            ",case when download isnull then 0 else download end as download  " +
            ",case when categorynum isnull then 0 else categorynum end as categorynum  " +
            "FROM  " +
            "    (SELECT m.*, " +
            "         n.DEPARTMENT_NAME, " +
            "         n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM  " +
            "        (SELECT DEPARTMENT, " +
            "         COUNT(1) AS USERNUM " +
            "        FROM API_USER_INFO " +
            "        WHERE VERIFY_STATUS = 'Y' " +
            "                AND user_grade != 'OTHER_GROUP' " +
            "                AND user_grade = #{usertype} " +
            "<when test='tokenDepartment !=null'> and DEPARTMENT = #{tokenDepartment} </when>"+
            "        GROUP BY  DEPARTMENT ) m, API_DEPARTMENT_INFO_NEW n " +
            "        WHERE m.DEPARTMENT=n.DEPARTMENT_ID " +
            "                AND n.is_country= #{is_country} ) t1 " +
            "    LEFT JOIN  " +
            "    (SELECT SUM(QUERY_TIMES) AS CALLNUM, " +
            "         SUM(DATA_SIZE) AS DOWNLOAD, " +
            "         count(distinct API_DATA_CODE) AS CATEGORYNUM , " +
            "         b.DEPARTMENT " +
            "    FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID=b.USER_ID " +
            "            AND b.DEPARTMENT=c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE=d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "            AND b.VERIFY_STATUS='Y' " +
            "            AND b.user_grade !='OTHER_GROUP' " +
            "            AND a.D_DATETIME &gt;= #{startTime} " +
            "            AND a.D_DATETIME &lt;= #{endTime} " +
            "            AND b.user_grade = #{usertype} " +
            "            AND c.is_country= #{is_country} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "    GROUP BY  b.DEPARTMENT ) t2 " +
            "    ON t1.DEPARTMENT = t2.DEPARTMENT " +
            "ORDER BY  DEPARTMENT_NO asc" +
            " </script>")
    List<Map<String, Object>> basicUserDepList2(String usertype,String is_country, String startTime, String endTime, String tokenDepartment);

    @Select("SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "and b.DEPARTMENT= #{department} " +
            "GROUP BY D_DATETIME " +
            "order by D_DATETIME asc ")
    List<Map<String, Object>> userDepTimeDataList(String usertype,String department,String is_country, String startTime, String endTime);

    @Select("SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "and b.DEPARTMENT= #{department} " +
            "GROUP BY D_DATETIME " +
            "order by D_DATETIME asc ")
    List<Map<String, Object>> userDepTimeDataList2(String usertype,String department,String is_country, String startTime, String endTime);

    @Select("SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",d.DATA_CLASS_ID,max(e.DATA_CLASS_NAME) as DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "and b.DEPARTMENT=#{department} " +
            "GROUP BY d.DATA_CLASS_ID " +
            "order by DATA_CLASS_NO ")
    List<Map<String, Object>> userDepClassDataList(String usertype,String department,String is_country, String startTime, String endTime);

    @Select("SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "AND b.CREATE_TIME >= #{usreCreateStartTime} AND b.CREATE_TIME <= #{userCreateEndTime}  " +
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "and b.DEPARTMENT= #{department} " +
            "GROUP BY D_DATETIME " +
            "order by D_DATETIME asc ")
    List<Map<String, Object>> newUserDepTimeDataList(String usertype,String department,String is_country, String startTime, String endTime, String usreCreateStartTime, String userCreateEndTime);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND b.CREATE_TIME &gt;= #{usreCreateStartTime} AND b.CREATE_TIME &lt;= #{userCreateEndTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "GROUP BY b.DEPARTMENT,D_DATETIME " +
            "order by b.DEPARTMENT,D_DATETIME asc </script>")
    List<Map<String, Object>> newUserDepTimeDataList2(String usertype,String is_country, String startTime, String endTime, String usreCreateStartTime, String userCreateEndTime, String tokenDepartment);

    @Select("SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",d.DATA_CLASS_ID,max(e.DATA_CLASS_NAME) as DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "AND b.CREATE_TIME >= #{usreCreateStartTime} AND b.CREATE_TIME <= #{userCreateEndTime}  " +
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "and b.DEPARTMENT=#{department} " +
            "GROUP BY d.DATA_CLASS_ID " +
            "order by DATA_CLASS_NO ")
    List<Map<String, Object>> newUserDepClassDataList(String usertype,String department,String is_country, String startTime, String endTime, String usreCreateStartTime, String userCreateEndTime);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,d.DATA_CLASS_ID,max(e.DATA_CLASS_NAME) as DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND b.CREATE_TIME &gt;= #{usreCreateStartTime} AND b.CREATE_TIME &lt;= #{userCreateEndTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "GROUP BY b.DEPARTMENT,d.DATA_CLASS_ID " +
            "order by b.DEPARTMENT,download desc </script>")
    List<Map<String, Object>> newUserDepClassDataList2(String usertype,String is_country, String startTime, String endTime, String usreCreateStartTime, String userCreateEndTime, String tokenDepartment);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "GROUP BY b.DEPARTMENT,D_DATETIME " +
            "order by b.DEPARTMENT,D_DATETIME asc </script>")
    List<Map<String, Object>> userDepTimeDataList3(String usertype,String is_country, String startTime, String endTime, String tokenDepartment);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.user_grade,D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "and b.DEPARTMENT = #{department}  " +
            "GROUP BY b.user_grade,D_DATETIME " +
            "order by b.user_grade,D_DATETIME asc </script>")
    List<Map<String, Object>> depUserTimeDataList(String department, String startTime, String endTime);


    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,d.DATA_CLASS_ID,max(e.DATA_CLASS_NAME) as DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "and b.user_grade = #{usertype}  " +
            "and c.is_country= #{is_country} " +
            "GROUP BY b.DEPARTMENT,d.DATA_CLASS_ID " +
            "order by b.DEPARTMENT,download desc </script>")
    List<Map<String, Object>> userDepClassDataList3(String usertype,String is_country, String startTime, String endTime, String tokenDepartment);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.user_grade,d.DATA_CLASS_ID,max(e.DATA_CLASS_NAME) as DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "and b.DEPARTMENT = #{department}  " +
            "GROUP BY b.user_grade,d.DATA_CLASS_ID " +
            "order by b.user_grade,download desc </script>")
    List<Map<String, Object>> depUserClassDataList(String department, String startTime, String endTime);


    @Select({"<script> select sum(query_times) as callnum, sum(data_size) as download,count(distinct api_data_code) as categorynum,count(distinct user_id) as usernum " +
            ",b.user_id,max(b.user_name) as user_name,max(b.\"SYSTEM\") as \"SYSTEM\",max(b.create_time) as create_time" +
            " ,case when max(b.PHONE) isnull then '' else max(b.PHONE) end AS PHONE" +
            " ,case when max(b.EMAIL) isnull then '' else max(b.EMAIL) end AS EMAIL" +
            " ,case when max(b.SYSTEM_DES) isnull then '' else max(b.SYSTEM_DES) end AS SYSTEM_DES" +
            ",max(b.user_grade) as user_grade,max(b.DEPARTMENT) as DEPARTMENT,max(c.DEPARTMENT_NAME) as DEPARTMENT_NAME " +
            ",max(c.SERIAL_NO) as DEPARTMENT_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "AND b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='usertype !=null '>  and b.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and b.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "<when test='search !=null '>  and (b.USER_NAME like '%${search}%' or b.\"SYSTEM\" like '%${search}%') </when>" +
            "GROUP BY b.user_grade,b.DEPARTMENT,b.USER_ID </script>"})
    @InterceptorIgnore(tenantLine = "1")
    List<Map<String, Object>> userstatByUsertypeAndDepartment(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search);


    @Select({"<script> SELECT t1.*" +
            ",0 as callnum " +
            ",0 as download " +
            ",0 as categorynum " +
            ",0 as usernum " +
            "FROM  " +
            "    (SELECT m.user_id, " +
            "         m.user_name, " +
            "         m.\"SYSTEM\", " +
            "         m.create_time, " +
            "         m.user_grade , " +
            "         case when phone isnull then '' else phone end as phone, " +
            "         case when email isnull then '' else email end as email, " +
            "         case when system_des isnull then '' else system_des end as system_des, " +
            "         m.DEPARTMENT, " +
            "         n.DEPARTMENT_NAME , " +
            "         n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'Y' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            " AND m.USER_ID not in (SELECT DISTINCT sys_user_id FROM API_PERFORMANCE_DAY WHERE D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime}) "+
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and m.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='search !=null '>  and (m.USER_NAME like '%${search}%' or m.\"SYSTEM\" like '%${search}%') </when>" +
            "             ) t1 " +
            " </script>"})
    List<Map<String, Object>> userstatByUsertypeAndDepartmentNotActive(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search);

    @Select({"<script> select sum(query_times) as callnum, sum(data_size) as download,count(distinct api_data_code) as categorynum,count(distinct user_id) as usernum " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "AND b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='usertype !=null '>  and b.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and b.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "<when test='search !=null '>  and (b.USER_NAME like '%${search}%' or b.\"SYSTEM\" like '%${search}%') </when>" +
            " </script>"})
    @InterceptorIgnore(tenantLine = "1")
    List<Map<String, Object>> userstatByUsertypeAndDepartmentTotal(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search);

    @Select({"<script> SELECT t1.*" +
            ",case when callnum isnull then 0 else callnum end as callnum " +
            ",case when download isnull then 0 else download end as download " +
            ",case when categorynum isnull then 0 else categorynum end as categorynum " +
            ",case when usernum isnull then 0 else usernum end as usernum " +
            "FROM  " +
            "    (SELECT m.user_id, " +
            "         m.user_name, " +
            "         m.\"SYSTEM\", " +
            "         m.create_time, " +
            "         m.user_grade , " +
            "         case when phone isnull then '' else phone end as phone, " +
            "         case when email isnull then '' else email end as email, " +
            "         case when system_des isnull then '' else system_des end as system_des, " +
            "        m.DEPARTMENT, " +
            "         n.DEPARTMENT_NAME , " +
            "        n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'Y' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and m.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='search !=null '>  and (m.USER_NAME like '%${search}%' or m.SYSTEM like '%${search}%') </when>" +
            "            AND CREATE_TIME &gt;= #{createStartTime} " +
            "            AND CREATE_TIME &lt;= #{createEndTime} ) t1 " +
            "LEFT JOIN  " +
            "    (SELECT sum(query_times) AS callnum, " +
            "         sum(data_size) AS download , " +
            "         count(DISTINCT api_data_code) AS categorynum, " +
            "         count(DISTINCT user_id) AS usernum , " +
            "         b.user_id " +
            "    FROM API_PERFORMANCE_DAY a, API_USER_INFO b, API_DEPARTMENT_INFO_NEW c, API_DATA_DEFINE d, API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID = b.USER_ID " +
            "            AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE = d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            " AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "    GROUP BY  b.USER_ID " +
            "    ORDER BY  download DESC ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  </script>"})
    List<Map<String, Object>> newUserstatByUsertypeAndDepartment(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search, String createStartTime,String createEndTime);

    @Select({"<script> SELECT t1.*" +
            ",case when callnum isnull then 0 else callnum end as callnum " +
            ",case when download isnull then 0 else download end as download " +
            ",case when categorynum isnull then 0 else categorynum end as categorynum " +
            ",case when usernum isnull then 0 else usernum end as usernum " +
            "FROM  " +
            "    (SELECT m.user_id, " +
            "         m.user_name, " +
            "         m.\"SYSTEM\", " +
            "         m.create_time, " +
            "         m.user_grade , " +
            "         case when phone isnull then '' else phone end as phone, " +
            "         case when email isnull then '' else email end as email, " +
            "         case when system_des isnull then '' else system_des end as system_des, " +
            "        m.DEPARTMENT, " +
            "         n.DEPARTMENT_NAME , " +
            "        n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'N' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and m.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='search !=null '>  and (m.USER_NAME like '%${search}%' or m.SYSTEM like '%${search}%') </when>" +
            "            AND VERIFY_TIME &gt;= #{createStartTime} " +
            "            AND VERIFY_TIME &lt;= #{createEndTime} ) t1 " +
            "LEFT JOIN  " +
            "    (SELECT sum(query_times) AS callnum, " +
            "         sum(data_size) AS download , " +
            "         count(DISTINCT api_data_code) AS categorynum, " +
            "         count(DISTINCT user_id) AS usernum , " +
            "         b.user_id " +
            "    FROM API_PERFORMANCE_DAY a, API_USER_INFO b, API_DEPARTMENT_INFO_NEW c, API_DATA_DEFINE d, API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID = b.USER_ID " +
            "            AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE = d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            " AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "    GROUP BY  b.USER_ID " +
            "    ORDER BY  download DESC ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  </script>"})
    List<Map<String, Object>> disableUserstatByUsertypeAndDepartment(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search, String createStartTime,String createEndTime);

    @Select({"<script> SELECT " +
            " case when SUM(callnum) isnull then 0 else SUM(callnum) end as CALLNUM " +
            ",case when SUM(download) isnull then 0 else SUM(download) end as DOWNLOAD " +
            ",count(distinct API_DATA_CODE) as CATEGORYNUM " +
            ",count(distinct USER_ID) as USERNUM " +
            "FROM  " +
            "    (SELECT m.user_id " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'Y' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and m.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='search !=null '>  and (m.USER_NAME like '%${search}%' or m.SYSTEM like '%${search}%') </when>" +
            "            AND CREATE_TIME &gt;= #{createStartTime} " +
            "            AND CREATE_TIME &lt;= #{createEndTime} ) t1 " +
            "LEFT JOIN  " +
            "    (SELECT sum(query_times) AS callnum, " +
            "         sum(data_size) AS download, " +
            "         b.user_id,a.API_DATA_CODE " +
            "    FROM API_PERFORMANCE_DAY a, API_USER_INFO b, API_DEPARTMENT_INFO_NEW c, API_DATA_DEFINE d, API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID = b.USER_ID " +
            "            AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE = d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            " AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "    GROUP BY  b.USER_ID,API_DATA_CODE " +
            "     ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  </script>"})
    List<Map<String, Object>> newUserstatByUsertypeAndDepartmentTotal(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search, String createStartTime,String createEndTime);

    @Select({"<script> SELECT " +
            " case when SUM(callnum) isnull then 0 else SUM(callnum) end as CALLNUM " +
            ",case when SUM(download) isnull then 0 else SUM(download) end as DOWNLOAD " +
            ",count(distinct API_DATA_CODE) as CATEGORYNUM " +
            ",count(distinct USER_ID) as USERNUM " +
            "FROM  " +
            "    (SELECT m.user_id " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'N' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and m.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='search !=null '>  and (m.USER_NAME like '%${search}%' or m.SYSTEM like '%${search}%') </when>" +
            "            AND VERIFY_TIME &gt;= #{createStartTime} " +
            "            AND VERIFY_TIME &lt;= #{createEndTime} ) t1 " +
            "LEFT JOIN  " +
            "    (SELECT sum(query_times) AS callnum, " +
            "         sum(data_size) AS download, " +
            "         b.user_id,a.API_DATA_CODE " +
            "    FROM API_PERFORMANCE_DAY a, API_USER_INFO b, API_DEPARTMENT_INFO_NEW c, API_DATA_DEFINE d, API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID = b.USER_ID " +
            "            AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE = d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            " AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "    GROUP BY  b.USER_ID,API_DATA_CODE " +
            "     ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  </script>"})
    List<Map<String, Object>> disableUserstatByUsertypeAndDepartmentTotal(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search, String createStartTime,String createEndTime);

    @Select({"<script> SELECT t1.*" +
            ",case when callnum isnull then 0 else callnum end as callnum " +
            ",case when download isnull then 0 else download end as download " +
            ",case when categorynum isnull then 0 else categorynum end as categorynum " +
            ",case when usernum isnull then 0 else usernum end as usernum " +
            "FROM  " +
            "    (SELECT m.user_id, " +
            "         m.user_name, " +
            "         m.\"SYSTEM\", " +
            "         m.create_time, " +
            "         m.user_grade , " +
            "         case when phone isnull then '' else phone end as phone, " +
            "         case when email isnull then '' else email end as email, " +
            "         case when system_des isnull then '' else system_des end as system_des, " +
            "         m.DEPARTMENT, " +
            "         n.DEPARTMENT_NAME , " +
            "         n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'Y' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and m.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='search !=null '>  and (m.USER_NAME like '%${search}%' or m.\"SYSTEM\" like '%${search}%') </when>" +
            "             ) t1 " +
            "LEFT JOIN  " +
            "    (SELECT sum(query_times) AS callnum, " +
            "         sum(data_size) AS download , " +
            "         count(DISTINCT api_data_code) AS categorynum, " +
            "         count(DISTINCT user_id) AS usernum , " +
            "         b.user_id " +
            "    FROM API_PERFORMANCE_DAY a, API_USER_INFO b, API_DEPARTMENT_INFO_NEW c, API_DATA_DEFINE d, API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID = b.USER_ID " +
            "            AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE = d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            " AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "    GROUP BY  b.USER_ID " +
            "    ORDER BY  download DESC ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  </script>"})
    List<Map<String, Object>> totalUserstatByUsertypeAndDepartment(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search);

    @Select({"<script> SELECT " +
            "case when SUM(callnum) isnull then 0 else SUM(callnum) end as CALLNUM," +
            "case when SUM(download) isnull then 0 else SUM(download) end as DOWNLOAD," +
            "count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM  " +
            "FROM  " +
            "    (SELECT m.user_id " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'Y' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and m.DEPARTMENT = #{departemnt}  </when>" +
            "<when test='search !=null '>  and (m.USER_NAME like '%${search}%' or m.\"SYSTEM\" like '%${search}%') </when>" +
            "             ) t1 " +
            "LEFT JOIN  " +
            "    (SELECT sum(query_times) AS callnum, " +
            "         sum(data_size) AS download , " +
            "         b.user_id,a.API_DATA_CODE " +
            "    FROM API_PERFORMANCE_DAY a, API_USER_INFO b, API_DEPARTMENT_INFO_NEW c, API_DATA_DEFINE d, API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID = b.USER_ID " +
            "            AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE = d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            " AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='data_class_id !=null '>  and e.DATA_CLASS_ID = #{data_class_id}  </when>" +
            "    GROUP BY  b.USER_ID,a.API_DATA_CODE " +
            "     ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  </script>"})
    List<Map<String, Object>> totalUserstatByUsertypeAndDepartmentTotal(String usertype,String departemnt, String data_class_id ,String startTime, String endTime,String search);


    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",e.DATA_CLASS_ID,max(e.DATA_CLASS_NAME) as DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO,d.DATA_CODE,max(d.DATA_NAME) as DATA_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND b.USER_ID=#{userid} " +
            "<when test='search !=null '>  and (d.DATA_NAME like '%${search}%' or d.DATA_CODE like '%${search}%') </when>" +
            "GROUP BY DATA_CLASS_ID,d.DATA_CODE " +
            "order by DATA_CLASS_NO ASC " +
            " </script>"})
    List<Map<String, Object>> userstatByUserid(String userid,String startTime, String endTime,String search);

    @Select({"SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",d.DATA_CODE,max(d.DATA_NAME) as DATA_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "AND b.USER_ID=#{userid} " +
            "GROUP BY DATA_CLASS_ID,d.DATA_CODE " +
            "order by DOWNLOAD desc limit 5 " +
            " "})
    List<Map<String, Object>> userstatByUseridTop5(String userid,String startTime, String endTime);

    /*@Select({"SELECT D_DATETIME,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD  " +
            "FROM API_PERFORMANCE_DAY a " +
            "where SYS_USER_ID=#{userid}  " +
            "AND API_DATA_CODE=#{datacode} " +
            "AND D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime}  " +
            "group by D_DATETIME " +
            "ORDER BY D_DATETIME ASC"})
    List<Map<String, Object>> userDataTimeDetail(String userid,String datacode,String startTime, String endTime);*/

    @Select({"SELECT D_DATETIME,SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD  " +
            "FROM API_PERFORMANCE_HOR a " +
            "where SYS_USER_ID=#{userid}  " +
            "AND API_DATA_CODE=#{datacode} " +
            "AND D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime}  " +
            "group by D_DATETIME " +
            "ORDER BY D_DATETIME ASC"})
    List<Map<String, Object>> userDataTimeDetail(String userid,String datacode,String startTime, String endTime);

    @Select({"<script> select * from (SELECT t1.*" +
            ",case when tempcallnum isnull then 0 else tempcallnum end as callnum " +
            ",case when tempdownload isnull then 0 else tempdownload end as download " +
            ",case when tempcategorynum isnull then 0 else tempcategorynum end as categorynum " +
            ",case when tempusernum isnull then 0 else tempusernum end as usernum " +
            "FROM  " +
            "    (SELECT m.user_id, " +
            "         m.user_name, " +
            "         m.\"SYSTEM\", " +
            "         m.create_time, " +
            "         m.user_grade , " +
            "         case when phone isnull then '' else phone end as phone, " +
            "         case when email isnull then '' else email end as email, " +
            "         case when system_des isnull then '' else system_des end as system_des, " +
            "         m.DEPARTMENT, " +
            "         n.DEPARTMENT_NAME , " +
            "         n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "            AND m.VERIFY_STATUS = 'Y' " +
            "            AND m.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and m.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and ( m.DEPARTMENT like '%${departemnt}%' or n.DEPARTMENT_NAME like '%${departemnt}%' ) </when>" +
            "<when test='tokenDepartment !=null '> and m.DEPARTMENT = #{tokenDepartment} </when>" +
            "<when test='system !=null '>  and m.\"system\" like '%${system}%'  </when>" +
            "<when test='username !=null '>  and ( m.user_name like '%${username}%' or m.user_id like '%${username}%' ) </when>" +
            "             ) t1 " +
            "LEFT JOIN  " +
            "    (SELECT sum(query_times) AS tempcallnum, " +
            "         sum(data_size) AS tempdownload , " +
            "         count(DISTINCT api_data_code) AS tempcategorynum, " +
            "         count(DISTINCT user_id) AS tempusernum , " +
            "         b.user_id " +
            "    FROM API_PERFORMANCE_DAY a, API_USER_INFO b, API_DEPARTMENT_INFO_NEW c, API_DATA_DEFINE d, API_DATA_CLASS_DEFINE e " +
            "    WHERE a.SYS_USER_ID = b.USER_ID " +
            "            AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "            AND a.API_DATA_CODE = d.DATA_CODE " +
            "            AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            " AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='dataname !=null '>  and ( d.DATA_NAME like '%${dataname}%' or d.DATA_CODE like '%${dataname}%' ) </when>" +
            "    GROUP BY  b.USER_ID " +
            "     ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  " +
            "    WHERE 1=1 " +
            "<when test='minCategorynum !=null '>  and categorynum &gt;= #{minCategorynum}  </when>" +
            "<when test='maxCategorynum !=null '>  and categorynum &lt;= #{maxCategorynum}  </when>" +
            "<when test='minCallnum !=null '>  and callnum &gt;= #{minCallnum}  </when>" +
            "<when test='maxCallnum !=null '>  and callnum &lt;= #{maxCallnum}  </when>" +
            "<when test='minDownload !=null '>  and download &gt;= #{minDownload}  </when>" +
            "<when test='maxDownload !=null '>  and download &lt;= #{maxDownload}  </when>" +
            " ) " +
            "</script>"})
    List<Map<String, Object>> userunionsearch(String usertype,String departemnt, String tokenDepartment, String system, String username,String dataname,
                                              String startTime, String endTime,
                                              Integer minCategorynum,Integer maxCategorynum,Long minCallnum,Long maxCallnum,
                                              Long minDownload, Long maxDownload);


    @Update("update API_USER_INFO set IS_CHECK='Y', checkdesc=#{checkdesc} ,CHECKTIME=SYSDATE() where user_id=#{userid} ")
    void checkuser(String userid,String checkdesc);


    @Update("update API_USER_INFO set IS_CHECK='N', checkdesc=#{checkdesc} ,CHECKTIME=SYSDATE() where user_id=#{userid} ")
    void uncheckuser(String userid,String checkdesc);
}

