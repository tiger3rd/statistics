package com.piesat.statistics.mapper;

import com.piesat.statistics.bean.DataBean;
import com.piesat.statistics.bean.DataClassBean;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataMapper {

    @Select({"SELECT * FROM API_DATA_CLASS_DEFINE"})
    List<DataClassBean> getAllDataClass();

    @Select({"SELECT * FROM API_DATA_DEFINE t WHERE t.IS_PUBLISHED = 'True'"})
    List<DataBean> getAllPublishedData();

    @Select({"SELECT * FROM API_DATA_DEFINE t"})
    List<DataBean> getAllData();

    @Select({"<script> SELECT d.DATA_CODE,d.DATA_NAME FROM API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e WHERE d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "AND d.IS_PUBLISHED = 'True' " +
            "<when test='search !=null '>  and (d.DATA_NAME like '%${search}%' or d.DATA_CODE like '%${search}%') </when>" +
            "</script>"})
    List<Map<String, Object>> getDataDefine(String search);

    @Select({"<script> SELECT m.DATA_CODE,n.DATA_NAME FROM API_TOP_DATA m,API_DATA_DEFINE n " +
            " WHERE m.DATA_CODE = n.DATA_CODE " +
            " <when test='department !=null '> and m.DEPARTMENT=#{department} </when> " +
            "ORDER BY m.SERIAL_NO ASC " +
            "</script>"})
    List<Map<String, Object>> getTopData(String department);


    @Delete((" delete  from API_TOP_DATA WHERE DEPARTMENT=#{department}" ))
    void deleteTopData(String department);

    @Insert({" insert into API_TOP_DATA (DATA_CODE,SERIAL_NO,DEPARTMENT) values (#{DATA_CODE},#{SERIAL_NO},#{department})"})
    Integer saveTopData(String DATA_CODE,int SERIAL_NO,String department);


    @Select({"SELECT  " +
            "   DISTINCT t1.API_DATA_CODE  " +
            " FROM  " +
            "   API_PERFORMANCE_DAY t1  " +
            " WHERE  " +
            "   t1.API_DATA_CODE is not null  " +
            "   and t1.D_DATETIME >= #{startTime}   " +
            "   AND t1.D_DATETIME <= #{endTime}"})
    List<String> getAccessData(String startTime, String endTime);

    @Select({"SELECT  " +
            "   t2.DEPARTMENT DEPARTMENT,  " +
            "   count(DISTINCT t1.API_DATA_CODE ) TOTAL  " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1,  " +
            "   API_USER_INFO t2  " +
            " WHERE  " +
            "   t1.SYS_USER_ID = t2.USER_ID  " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime}  " +
            "   AND t1.D_DATETIME <= #{endTime}  " +
            "GROUP BY t2.DEPARTMENT "})
    List<Map<String, Object>> getAccessByDepartment(String startTime, String endTime);

    @Select({"SELECT  " +
            "   sum(t1.DATA_SIZE )  " +
            " FROM  " +
            "   API_PERFORMANCE_DAY t1  " +
            " WHERE  " +
            "   t1.D_DATETIME >= #{startTime}  " +
            "   AND t1.D_DATETIME <= #{endTime}"})
    Long getTotalVolume(String startTime, String endTime);

    @Select({"SELECT     " +
            "    sum(t1.DATA_SIZE )     " +
            " FROM     " +
            "    API_PERFORMANCE_DAY t1,     " +
            "    API_USER_INFO t2     " +
            " WHERE     " +
            "    t1.SYS_USER_ID = t2.USER_ID     " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "    AND t2.user_grade = #{groupId}     " +
            "    AND t1.D_DATETIME >= #{startTime}     " +
            "    AND t1.D_DATETIME <= #{endTime}"})
    Long getTotalVolumeByGroupId(String groupId, String startTime, String endTime);

    @Select({"SELECT     " +
            "    t2.user_grade TYPE,     " +
            "    sum(t1.DATA_SIZE ) TOTAL     " +
            " FROM     " +
            "    API_PERFORMANCE_DAY t1,     " +
            "    API_USER_INFO t2     " +
            " WHERE     " +
            "    t1.SYS_USER_ID = t2.USER_ID     " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "    AND t1.D_DATETIME >= #{startTime}     " +
            "    AND t1.D_DATETIME <= #{endTime}    " +
            " GROUP BY t2.user_grade"})
    List<Map<String, Object>> getVolumeByGroupId(String startTime, String endTime);

    @Select({"SELECT     " +
            "    t1.D_DATETIME DATATIME,     " +
            "    t2.user_grade TYPE,     " +
            "    SUM(t1.DATA_SIZE) TOTAL     " +
            " FROM     " +
            "    API_PERFORMANCE_DAY t1,     " +
            "    API_USER_INFO t2     " +
            " WHERE     " +
            "    t1.SYS_USER_ID = t2.USER_ID     " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "    AND t1.D_DATETIME >= #{startTime}     " +
            "    AND t1.D_DATETIME <= #{endTime}     " +
            " GROUP BY     " +
            "    t1.D_DATETIME,     " +
            "    t2.user_grade "})
    List<Map<String, Object>> getVolumeByDatetimeAndUserType(String startTime, String endTime);

    @Select({"SELECT     " +
            "    t1.D_DATETIME DATATIME,     " +
            "    t2.user_grade TYPE,     " +
            "    SUM(t1.DATA_SIZE) TOTAL     " +
            " FROM     " +
            "    API_PERFORMANCE_HOR t1,     " +
            "    API_USER_INFO t2     " +
            " WHERE     " +
            "    t1.SYS_USER_ID = t2.USER_ID     " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "    AND t1.D_DATETIME >= #{startTime}     " +
            "    AND t1.D_DATETIME <= #{endTime}     " +
            " GROUP BY     " +
            "    t1.D_DATETIME,     " +
            "    t2.user_grade "})
    List<Map<String, Object>> getDrillVolumeByDatetimeAndUserType(String startTime, String endTime);

    @Select({"SELECT  " +
            "   t2.DEPARTMENT DEPARTMENT,  " +
            "   sum(t1.DATA_SIZE ) TOTAL  " +
            " FROM " +
            "   API_PERFORMANCE_DAY t1,  " +
            "   API_USER_INFO t2  " +
            " WHERE  " +
            "   t1.SYS_USER_ID = t2.USER_ID  " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t1.D_DATETIME >= #{startTime}  " +
            "   AND t1.D_DATETIME <= #{endTime}  " +
            "GROUP BY t2.DEPARTMENT "})
    List<Map<String, Object>> getVolumeByDepartment(String startTime, String endTime);

    @Select({"SELECT " +
            "   t2.USER_ID USERID,  " +
            "   SUM(t1.DATA_SIZE) TOTAL  " +
            " FROM  " +
            "   API_PERFORMANCE_DAY t1,  " +
            "   API_USER_INFO t2  " +
            " WHERE  " +
            "   t1.SYS_USER_ID = t2.USER_ID  " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "   AND t2.user_grade = #{userType} " +
            "   AND t1.D_DATETIME >= #{startTime}  " +
            "   AND t1.D_DATETIME <= #{endTime}  " +
            " GROUP BY t2.USER_ID  " +
            " ORDER BY TOTAL DESC  " +
            " LIMIT #{limit}"})
    List<Map<String, Object>> getVolumeByUserType(String startTime, String endTime, String userType, int limit);


    @Select({"SELECT " +
            "    t3.DATA_CLASS_ID DATACLASSID, " +
            "    sum(t.DATA_SIZE ) TOTAL " +
            " FROM " +
            "    API_PERFORMANCE_DAY t, " +
            "    API_USER_INFO t1, " +
            "    API_DEPARTMENT_INFO_NEW t2, " +
            "    API_DATA_DEFINE t3 " +
            " WHERE " +
            "    t.SYS_USER_ID = t1.USER_ID  " +
            "   AND t1.VERIFY_STATUS='Y' " +
            "   AND t1.user_grade!='OTHER_GROUP'  " +
            "    AND t.API_DATA_CODE = t3.DATA_CODE  " +
            "    AND t1.DEPARTMENT = t2.DEPARTMENT_ID " +
            "    AND t2.IS_COUNTRY = #{country} " +
            "    AND t.D_DATETIME >= #{startTime} " +
            "    AND t.D_DATETIME <= #{endTime} " +
            " GROUP BY t3.DATA_CLASS_ID"})
    List<Map<String, Object>> getVolumeByDataClassId(int country, String startTime, String endTime);

    @Select({"SELECT      " +
            "    t2.user_grade TYPE,     " +
            "    SUM(t1.DATA_SIZE) TOTAL     " +
            " FROM      " +
            "    API_PERFORMANCE_DAY t1,     " +
            "    API_USER_INFO t2,     " +
            "    API_DEPARTMENT_INFO_NEW t3     " +
            " WHERE     " +
            "    t1.SYS_USER_ID = t2.USER_ID      " +
            "   AND t2.VERIFY_STATUS='Y' " +
            "   AND t2.user_grade!='OTHER_GROUP'  " +
            "    AND t2.DEPARTMENT = t3.DEPARTMENT_ID      " +
            "    AND t3.IS_COUNTRY = #{country}     " +
            "    AND t1.D_DATETIME >= #{startTime}     " +
            "    AND t1.D_DATETIME <= #{endTime}     " +
            " GROUP  BY      " +
            "    t2.user_grade"})
    List<Map<String, Object>> getVolumeFilterCountryByGroupId(int country, String startTime, String endTime);

    @Select({"SELECT  " +
            "     t.API_DATA_CODE DATACODE,   " +
            "     t1.DEPARTMENT DEPARTMENT,  " +
            "     sum(t.DATA_SIZE ) TOTAL  " +
            " FROM  " +
            "     API_PERFORMANCE_DAY t,  " +
            "     API_USER_INFO t1,  " +
            "     API_DEPARTMENT_INFO_NEW t2  " +
            " WHERE  " +
            "     t.SYS_USER_ID = t1.USER_ID   " +
            "   AND t1.VERIFY_STATUS='Y' " +
            "   AND t1.user_grade!='OTHER_GROUP'  " +
            "     AND t1.DEPARTMENT = t2.DEPARTMENT_ID  " +
            "     AND t2.IS_COUNTRY = #{country}  " +
            "     AND t.D_DATETIME >= #{startTime}  " +
            "     AND t.D_DATETIME <= #{endTime}  " +
            " GROUP BY t.API_DATA_CODE, t1.DEPARTMENT "})
    List<Map<String, Object>> getVolumeByDataIdAndDepartment(int country, String startTime, String endTime);

    @Select({"SELECT  " +
            "     t1.user_grade GROUPID,  " +
            "     t.API_DATA_CODE DATACODE,   " +
            "     t1.DEPARTMENT DEPARTMENT,  " +
            "     sum(t.DATA_SIZE ) TOTAL  " +
            " FROM  " +
            "     API_PERFORMANCE_DAY t,  " +
            "     API_USER_INFO t1,  " +
            "     API_DEPARTMENT_INFO_NEW t2  " +
            " WHERE  " +
            "     t.SYS_USER_ID = t1.USER_ID   " +
            "   AND t1.VERIFY_STATUS='Y' " +
            "   AND t1.user_grade!='OTHER_GROUP'  " +
            "     AND t1.DEPARTMENT = t2.DEPARTMENT_ID  " +
            "     AND t2.IS_COUNTRY = #{country}  " +
            "     AND t.D_DATETIME >= #{startTime}  " +
            "     AND t.D_DATETIME <= #{endTime}  " +
            " GROUP BY t1.user_grade, t.API_DATA_CODE, t1.DEPARTMENT "})
    List<Map<String, Object>> getVolumeByUserTypeGroupByDataIdAndDepartment(int country, String startTime, String endTime);

    @Select({"<script> " +
            "select t1.DATA_CLASS_ID,t1.DATA_CLASS_NAME,t1.SERIAL_NO as DATA_CLASS_NO, " +
            "case WHEN CLSSNUM ISNULL THEN 0  ELSE CLSSNUM END AS CLSSNUM " +
            " from API_DATA_CLASS_DEFINE t1 " +
            "left join  " +
            "(" +
            "SELECT count(distinct API_DATA_CODE) as CLSSNUM " +
            ",e.DATA_CLASS_ID,max(e.DATA_CLASS_NAME) as DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY e.DATA_CLASS_ID  " +
            ") t2 on t1.DATA_CLASS_ID=t2.DATA_CLASS_ID " +
            "WHERE t1.DATA_CLASS_ID !='SCEX' " +
            "ORDER BY DATA_CLASS_NO ASC" +
            " </script>"})
    List<Map<String, Object>> dataClassCount(String startTime, String endTime, String tokenDepartment);

    /*@Select({"<script> " +
            "SELECT  count(DISTINCT DATA_CODE) AS CLSSNUM, " +
            "       e.DATA_CLASS_ID, " +
            "       max(e.DATA_CLASS_NAME) AS DATA_CLASS_NAME, " +
            "       max(e.SERIAL_NO) AS DATA_CLASS_NO " +
            "FROM API_DATA_CLASS_DEFINE e LEFT JOIN API_DATA_DEFINE d " +
            "ON d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "WHERE e.DATA_CLASS_ID!='SCEX' " +
            "GROUP BY e.DATA_CLASS_ID " +
            "ORDER BY DATA_CLASS_NO ASC " +
            " " +
            " </script>"})
    List<Map<String, Object>> dataClassCount(String startTime, String endTime, String tokenDepartment);*/

    @Select({"<script>" +
            "SELECT count(distinct API_DATA_CODE) as total " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP' " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "</script>"})
    Integer dataCalssTotalCount(String startTime, String endTime, String tokenDepartment);


    @Select({
            "SELECT * FROM ( " +
            "SELECT b.DATA_NAME,SUM(QUERY_TIMES) AS callnum, " +
            " SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY a,api_data_define b WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime}  " +
            "AND b.DATA_CLASS_ID = #{data_class_id} AND a.API_DATA_CODE = b.data_code GROUP BY b.DATA_NAME ) ORDER BY callnum DESC  LIMIT  #{limit}"
    })
    List<Map<String, Object>> dataDetailListByLimit(String data_class_id,String startTime, String endTime,Integer limit);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",d.DATA_CODE,MAX(d.DATA_NAME) as DATA_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP' " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "AND d.DATA_CLASS_ID=#{data_class_id} " +
            "<when test='search !=null '>  and (d.DATA_NAME like '%${search}%' or d.DATA_CODE like '%${search}%') </when>" +
            "GROUP BY d.DATA_CODE </script>"})
    List<Map<String, Object>> dataDetailList(String data_class_id,String startTime, String endTime,String search, String tokenDepartment);

    @Select({"<script> SELECT CASE WHEN SUM(QUERY_TIMES) isnull THEN 0 ELSE SUM(QUERY_TIMES) END AS CALLNUM, " +
            "CASE WHEN SUM(DATA_SIZE) isnull THEN 0 ELSE SUM(DATA_SIZE) END AS DOWNLOAD, " +
            "count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP' " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "AND d.DATA_CLASS_ID=#{data_class_id} " +
            "<when test='search !=null '>  and (d.DATA_NAME like '%${search}%' or d.DATA_CODE like '%${search}%') </when>" +
            " </script>"})
    List<Map<String, Object>> dataClassTotal(String data_class_id,String startTime, String endTime,String search, String tokenDepartment);

    @Select({"<script> SELECT b.DEPARTMENT,REPLACE(c.DEPARTMENT_NAME,'气象局','') as DEPARTMENT_NAME, c.IS_COUNTRY ,count(1) departNum ,max(c.SERIAL_NO) as DEPARTMENT_NO " +
            " FROM ( " +
            "SELECT DISTINCT sys_user_id FROM API_PERFORMANCE_DAY a WHERE D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime}) a, " +
            "API_USER_INFO b , API_DEPARTMENT_INFO_NEW c  " +
            "WHERE a.SYS_USER_ID = b.user_id AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND c.IS_COUNTRY=#{is_country} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY b.DEPARTMENT,c.DEPARTMENT_name,c.IS_COUNTRY " +
            "order by DEPARTMENT_NO asc </script>"})
    List<Map<String, Object>> dataCategoryCountGroupByDepartment(String is_country, String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT b.DEPARTMENT,REPLACE(c.DEPARTMENT_NAME,'气象局','') as DEPARTMENT_NAME, c.IS_COUNTRY ,count(1) departNum ,max(c.SERIAL_NO) as DEPARTMENT_NO " +
            " FROM " +
            "API_USER_INFO b , API_DEPARTMENT_INFO_NEW c  " +
            "WHERE b.DEPARTMENT = c.DEPARTMENT_ID " +
            "  AND b.USER_ID NOT IN (SELECT DISTINCT sys_user_id FROM API_PERFORMANCE_DAY WHERE D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime}) " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND c.IS_COUNTRY=#{is_country} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY b.DEPARTMENT,c.DEPARTMENT_name,c.IS_COUNTRY " +
            "order by DEPARTMENT_NO asc </script>"})
    List<Map<String, Object>> dataCategoryCountGroupByDepartmentNotActive(String is_country, String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT b.DEPARTMENT,REPLACE(c.DEPARTMENT_NAME,'气象局','') as DEPARTMENT_NAME, c.IS_COUNTRY ,count(1) departNum ,max(c.SERIAL_NO) as DEPARTMENT_NO " +
            " FROM "+
            "API_USER_INFO b , API_DEPARTMENT_INFO_NEW c  " +
            "WHERE b.DEPARTMENT = c.DEPARTMENT_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND c.IS_COUNTRY=#{is_country} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY b.DEPARTMENT,c.DEPARTMENT_name,c.IS_COUNTRY " +
            "order by DEPARTMENT_NO asc </script>"})
    List<Map<String, Object>> basicDataCategoryCountGroupByDepartment(String is_country, String startTime, String endTime, String tokenDepartment);


    @Select({"<script> SELECT count(1) total FROM ( " +
            "SELECT DISTINCT sys_user_id FROM API_PERFORMANCE_DAY a WHERE D_DATETIME &gt;= #{startTime} AND D_DATETIME &lt;= #{endTime}) a, " +
            "API_USER_INFO b , API_DEPARTMENT_INFO_NEW c  " +
            "WHERE a.SYS_USER_ID = b.user_id AND b.DEPARTMENT = c.DEPARTMENT_ID" +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            " AND c.IS_COUNTRY=#{is_country} </script>"})
    Integer dataCategoryTotalCountGroupByDepartment(String is_country, String startTime, String endTime, String tokenDepartment);

    @Select({"SELECT user_grade,COUNT(DISTINCT a.API_DATA_CODE) as num FROM  API_PERFORMANCE_DAY a,API_USER_INFO b  " +
            "WHERE a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND  a.SYS_USER_ID = b.user_id AND b.DEPARTMENT  = #{department}  " +
            "GROUP BY b.user_grade"})
    List<Map<String, Object>> dataCategoryCountByDepartment(String department, String startTime, String endTime);

    @Select({"SELECT COUNT(DISTINCT a.API_DATA_CODE) FROM  API_PERFORMANCE_DAY a,API_USER_INFO b  " +
            "WHERE a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND  a.SYS_USER_ID = b.user_id AND b.DEPARTMENT  = #{department} "})
    Integer dataCategoryTotalCountByDepartment(String department, String startTime, String endTime);

    @Select({"SELECT b.user_grade,count(1) AS groupNum FROM ( " +
            "SELECT DISTINCT sys_user_id FROM  API_PERFORMANCE_DAY WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime}  " +
            ") a, API_USER_INFO b " +
            "WHERE a.SYS_USER_ID = b.user_id " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "GROUP BY b.user_grade"})
    List<Map<String, Object>> dataCategoryCountGroupByUserType(String startTime, String endTime);

    @Select({"SELECT b.DEPARTMENT,c.DEPARTMENT_NAME,COUNT(DISTINCT a.API_DATA_CODE) as num FROM  API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c  " +
            "WHERE a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND  a.SYS_USER_ID = b.user_id AND b.user_grade  = #{usertype} AND b.DEPARTMENT  = c.DEPARTMENT_ID  " +
            "GROUP BY b.DEPARTMENT,c.DEPARTMENT_NAME "})
    List<Map<String, Object>> dataCategoryCountByUserType(String usertype,String startTime, String endTime);

    @Select({"SELECT COUNT(DISTINCT a.API_DATA_CODE) as NUM FROM  API_PERFORMANCE_DAY a,API_USER_INFO b  " +
            "WHERE a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime}  " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND  a.SYS_USER_ID = b.user_id AND b.user_grade  = #{usertype} "})
    Integer dataCategoryTotalCountByUserType(String usertype,String startTime, String endTime);

    @Select({"SELECT DATA_NAME,SUM(QUERY_TIMES) AS callnum,  " +
            " SUM(DATA_SIZE) AS download,count(DISTINCT sys_user_id) usernum,MAX(c.DATA_CLASS_ID) as DATA_CLASS_ID,max(a.API_DATA_CODE) as API_DATA_CODE FROM API_PERFORMANCE_DAY a,  " +
            "API_USER_INFO b,api_data_define c  " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND b.DEPARTMENT = #{department} AND b.user_grade = #{usertype} AND a.SYS_USER_ID = b.user_id AND a.API_DATA_CODE  = c.DATA_CODE  " +
            "AND DATA_NAME LIKE '%小时%'  " +
            "GROUP BY c.DATA_NAME "})
    List<Map<String, Object>> dataCountByDepartmentAndUserType(String department, String usertype,String startTime, String endTime);

/*    @Select({"SELECT DATA_CLASS_ID, SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY a,api_data_define c " +
            "WHERE a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime} AND a.API_DATA_CODE = c.data_code " +
            "GROUP BY c.DATA_CLASS_ID ORDER BY SUM(DATA_SIZE) desc" })
    List<Map<String, Object>> dataCountGroupByDataclass(String startTime, String endTime);*/

    @Select({"<script>" +
            "SELECT t1.DATA_CLASS_NAME, " +
            "       t1.DATA_CLASS_ID, " +
            "       CASE " +
            "           WHEN tempdownload ISNULL THEN 0 " +
            "           ELSE tempdownload " +
            "       END AS download " +
            "FROM API_DATA_CLASS_DEFINE t1 " +
            "LEFT JOIN " +
            "  (SELECT DATA_CLASS_ID, " +
            "          SUM(DATA_SIZE) AS tempdownload " +
            "   FROM API_PERFORMANCE_DAY a, " +
            "        api_data_define c " +
            "   WHERE a.D_DATETIME &gt;= #{startTime} " +
            "     AND a.D_DATETIME &lt;= #{endTime} " +
            "     AND a.API_DATA_CODE = c.data_code " +
            "   GROUP BY c.DATA_CLASS_ID) t2 ON t1.DATA_CLASS_ID=t2.DATA_CLASS_ID " +
            " WHERE t1.DATA_CLASS_ID!='SCEX' " +
            "ORDER BY download DESC" +
            "</script>" })
    List<Map<String, Object>> dataCountGroupByDataclass(String startTime, String endTime);


    @Select({"SELECT SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY a " +
            "WHERE a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime} "})
    Long totalDataSize(String startTime, String endTime);

    @Select({"<script> SELECT SYS_USER_ID,max(b.system) as system, max(b.user_name) as user_name,count(DISTINCT API_DATA_CODE) datacodenumber FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND a.SYS_USER_ID  = b.USER_ID " +
            "AND b.is_check  != 'Y' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            "ORDER BY count(DISTINCT API_DATA_CODE) DESC LIMIT 5 </script>"})
    List<Map<String, Object>> accessCategoryTop5(String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT SYS_USER_ID,max(b.system) as system, max(b.user_name) as user_name,count(DISTINCT API_DATA_CODE) datacodenumber FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND a.SYS_USER_ID  = b.USER_ID " +
            "<when test='usertype !=null'> and b.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and b.DEPARTMENT = #{department} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            "ORDER BY count(DISTINCT API_DATA_CODE) DESC LIMIT 50 </script>"})
    List<Map<String, Object>> accessCategoryTop50(String usertype,String department,String startTime, String endTime);

    @Select({"<script> SELECT SYS_USER_ID,max(b.system) as system, max(b.user_name) as user_name,SUM(QUERY_TIMES) AS callnum FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND a.SYS_USER_ID  = b.USER_ID " +
            "AND b.is_check  != 'Y' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY a.SYS_USER_ID  " +
            "ORDER BY SUM(QUERY_TIMES) DESC LIMIT 5  </script>"})
    List<Map<String, Object>> accessNumTop5(String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT SYS_USER_ID,max(b.system) as system, max(b.user_name) as user_name,SUM(QUERY_TIMES) AS callnum FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND a.SYS_USER_ID  = b.USER_ID  " +
            "<when test='usertype !=null'> and b.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and b.DEPARTMENT = #{department} </when>"+
            "GROUP BY a.SYS_USER_ID  " +
            "ORDER BY SUM(QUERY_TIMES) DESC LIMIT 50  </script>"})
    List<Map<String, Object>> accessNumTop50(String usertype,String department, String startTime, String endTime);

    @Select({"<script> SELECT SYS_USER_ID,max(b.system) as system, max(b.user_name) as user_name,SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_HOR a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND b.is_check  != 'Y' " +
            "AND (SUBSTR(D_DATETIME,9,10) &gt;= '23' OR SUBSTR(D_DATETIME,9,10) &lt;= '08')  AND a.SYS_USER_ID  = b.USER_ID AND b.user_grade='G_KY' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            "ORDER BY SUM(DATA_SIZE) DESC LIMIT 5  </script>"})
    List<Map<String, Object>> downloadTop5(String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT SYS_USER_ID,max(b.system) as system, max(b.user_name) as user_name,SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_HOR a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND (SUBSTR(D_DATETIME,9,10) &gt;= '23' OR SUBSTR(D_DATETIME,9,10) &lt;= '08')  AND a.SYS_USER_ID  = b.USER_ID " +
            "<when test='usertype !=null'> and b.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and b.DEPARTMENT = #{department} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            "ORDER BY SUM(DATA_SIZE) DESC LIMIT 50  </script>"})
    List<Map<String, Object>> downloadTop50(String usertype,String department,String startTime, String endTime);

    @Select({"<script> SELECT nowdata.SYS_USER_ID,nowdata.callnum-predata.callnum AS addquerydata FROM ( " +
            "SELECT SYS_USER_ID,SUM(QUERY_TIMES) AS callnum " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime1} AND a.D_DATETIME &lt;= #{endTime1} AND a.SYS_USER_ID  = b.USER_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND b.is_check  != 'Y' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY a.SYS_USER_ID) as nowdata, " +
            "( " +
            "SELECT SYS_USER_ID,SUM(QUERY_TIMES) AS callnum " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime2} AND a.D_DATETIME &lt;= #{endTime2} AND a.SYS_USER_ID  = b.USER_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND b.is_check  != 'Y' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            ") AS predata WHERE nowdata.SYS_USER_ID  = predata.SYS_USER_ID " +
            "ORDER BY addquerydata desc LIMIT 5  </script>"})
    List<Map<String, Object>> accessNumAddTop5(String startTime1, String endTime1, String startTime2, String endTime2, String tokenDepartment);

    @Select({"<script> SELECT nowdata.SYS_USER_ID,nowdata.callnum-predata.callnum AS addquerydata FROM ( " +
            "SELECT SYS_USER_ID,SUM(QUERY_TIMES) AS callnum " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime1} AND a.D_DATETIME &lt;= #{endTime1} AND a.SYS_USER_ID  = b.USER_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "<when test='usertype !=null'> and b.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and b.DEPARTMENT = #{department} </when>"+
            "GROUP BY a.SYS_USER_ID) as nowdata, " +
            "( " +
            "SELECT SYS_USER_ID,SUM(QUERY_TIMES) AS callnum " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime2} AND a.D_DATETIME &lt;= #{endTime2} AND a.SYS_USER_ID  = b.USER_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "<when test='usertype !=null'> and b.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and b.DEPARTMENT = #{department} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            ") AS predata WHERE nowdata.SYS_USER_ID  = predata.SYS_USER_ID " +
            "ORDER BY addquerydata desc LIMIT 50  </script>"})
    List<Map<String, Object>> accessNumAddTop50(String usertype,String department,String startTime1, String endTime1, String startTime2, String endTime2);

    @Select({"<script> SELECT nowdata.SYS_USER_ID,nowdata.download-predata.download AS adddowndata FROM ( " +
            "SELECT SYS_USER_ID,SUM(DATA_SIZE) AS download " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime1} AND a.D_DATETIME &lt;= #{endTime1} AND a.SYS_USER_ID  = b.USER_ID  " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND b.is_check  != 'Y' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY a.SYS_USER_ID) as nowdata, " +
            "( " +
            "SELECT SYS_USER_ID,SUM(DATA_SIZE) AS download " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime2} AND a.D_DATETIME &lt;= #{endTime2} AND a.SYS_USER_ID  = b.USER_ID  " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND b.is_check  != 'Y' " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            ") AS predata WHERE nowdata.SYS_USER_ID  = predata.SYS_USER_ID " +
            "ORDER BY adddowndata desc LIMIT 5 </script>"})
    List<Map<String, Object>> downlaodAddTop5(String startTime1, String endTime1, String startTime2, String endTime2, String tokenDepartment);

    @Select({"<script> SELECT nowdata.SYS_USER_ID,nowdata.download-predata.download AS adddowndata FROM ( " +
            "SELECT SYS_USER_ID,SUM(DATA_SIZE) AS download " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime1} AND a.D_DATETIME &lt;= #{endTime1} AND a.SYS_USER_ID  = b.USER_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "<when test='usertype !=null'> and b.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and b.DEPARTMENT = #{department} </when>"+
            "GROUP BY a.SYS_USER_ID) as nowdata, " +
            "( " +
            "SELECT SYS_USER_ID,SUM(DATA_SIZE) AS download " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b " +
            "WHERE a.D_DATETIME &gt;= #{startTime2} AND a.D_DATETIME &lt;= #{endTime2} AND a.SYS_USER_ID  = b.USER_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "<when test='usertype !=null'> and b.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and b.DEPARTMENT = #{department} </when>"+
            "GROUP BY a.SYS_USER_ID " +
            ") AS predata WHERE nowdata.SYS_USER_ID  = predata.SYS_USER_ID " +
            "ORDER BY adddowndata desc LIMIT 50 </script>"})
    List<Map<String, Object>> downlaodAddTop50(String usertype,String department, String startTime1, String endTime1, String startTime2, String endTime2);


    @Select({"select a.SYS_USER_ID,callnum,a.download,b.USER_NAME,b.DEPARTMENT,c.DEPARTMENT_NAME from ( " +
            "select SYS_USER_ID,SUM(QUERY_TIMES) AS callnum,SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY  " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} AND API_DATA_CODE  = #{datacode} GROUP BY SYS_USER_ID " +
            ") a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c where a.SYS_USER_ID=b.USER_ID  AND b.DEPARTMENT=c.DEPARTMENT_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "order by callnum desc,download desc limit #{limit} "})
    List<Map<String, Object>> dataCountByDatacodeAndLimit(String datacode,Integer limit, String startTime, String endTime);

    @Select({"select a.SYS_USER_ID,callnum,a.download,b.USER_NAME,b.DEPARTMENT,c.DEPARTMENT_NAME from ( " +
            "select SYS_USER_ID,SUM(QUERY_TIMES) AS callnum,SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY  " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} AND API_DATA_CODE  = 'SURF_CHN_SSD_HOR' GROUP BY SYS_USER_ID " +
            ") a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c where a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "order by callnum desc,download desc "})
    List<Map<String, Object>> dataCountByDatacode(String datacode, String startTime, String endTime);


    @Select({"select a.DEPARTMENT,b.DEPARTMENT_NAME,a.user_grade from API_USER_INFO a,API_DEPARTMENT_INFO_NEW b " +
            "where a.DEPARTMENT=b.DEPARTMENT_ID " +
            "and USER_ID=#{userid} "})
    List<Map<String, Object>> departmentByuserId(String userid);

    @Select({"SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD " +
            ",a.API_DATA_CODE,max(d.DATA_NAME) as DATA_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME >= #{startTime} AND a.D_DATETIME <= #{endTime} " +
            "AND a.API_DATA_CODE in ('OCEN_CHN_BUOY','SURF_CHN_MUL_HOR','SURF_CHN_MUL_MIN','RADA_L2_FMT','RADA_L2_ELEV_ST')  " +
            "group by a.API_DATA_CODE " +
            "order by a.API_DATA_CODE desc "})
    List<Map<String, Object>> dataTop5(String startTime, String endTime);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD " +
            ",a.API_DATA_CODE,max(d.DATA_NAME) as DATA_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP' " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "AND a.API_DATA_CODE in ('OCEN_CHN_BUOY','SURF_CHN_MUL_HOR','SURF_CHN_MUL_MIN','RADA_L2_FMT','RADA_L2_ELEV_ST') " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "group by a.API_DATA_CODE " +
            "order by a.API_DATA_CODE desc </script>"})
    List<Map<String, Object>> dataTop5Indepartment(String startTime, String endTime, String tokenDepartment);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD " +
            ",a.API_DATA_CODE,max(d.DATA_NAME) as DATA_NAME,max(f.SERIAL_NO) as TOP_DATA_SERIAL_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e,API_TOP_DATA f " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND d.DATA_CODE=f.DATA_CODE " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP' " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            //"AND a.API_DATA_CODE in ('OCEN_CHN_BUOY','SURF_CHN_MUL_HOR','SURF_CHN_MUL_MIN','RADA_L2_FMT','RADA_L2_ELEV_ST') " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            " <when test='tokenDepartment !=null '> and f.DEPARTMENT=#{tokenDepartment} </when> " +
            "group by a.API_DATA_CODE " +
            "order by TOP_DATA_SERIAL_NO asc </script>"})
    List<Map<String, Object>> dataTopIndepartment(String startTime, String endTime, String tokenDepartment);

    @Select({"SELECT SUM(DOWNLOAD) as total from( " +
            "SELECT API_DATA_CODE,SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY a " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} " +
            //"AND API_DATA_CODE !='SATE_HIMAWARI_8_FLDK' " +
            "AND API_DATA_CODE in ('SURF_CHN_MUL_HOR','SURF_CHN_MUL_MIN','OCEN_CHN_BUOY','RADA_L2_FMT','RADA_L2_ELEV_ST')  " +
            //"AND API_DATA_CODE in ('SATE_HIMAWARI_8_FLDK','OCEN_CHN_BUOY','SURF_CHN_MUL_HOR','SURF_CHN_MUL_MIN','RADA_L2_FMT','RADA_L2_ELEV_ST')  " +
            "group by API_DATA_CODE ORDER BY API_DATA_CODE DESC ) "})
    Long dataTotalTop5(String startTime, String endTime);

    @Select({"SELECT SUM(DOWNLOAD) as total from( " +
            "SELECT API_DATA_CODE,SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_HOR a " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} " +
            //"AND API_DATA_CODE !='SATE_HIMAWARI_8_FLDK' " +
            "AND API_DATA_CODE in ('OCEN_CHN_BUOY','SURF_CHN_MUL_HOR','SURF_CHN_MUL_MIN','RADA_L2_FMT','RADA_L2_ELEV_ST')  " +
            //"AND API_DATA_CODE in ('SATE_HIMAWARI_8_FLDK','OCEN_CHN_BUOY','SURF_CHN_MUL_HOR','SURF_CHN_MUL_MIN','RADA_L2_FMT','RADA_L2_ELEV_ST')  " +
            "group by API_DATA_CODE ORDER BY DOWNLOAD DESC ) "})
    Long dataTotalTop5ByHor(String startTime, String endTime);

    @Select({"SELECT DATA_NAME,D_DATETIME,SUM(QUERY_TIMES) AS callnum, " +
            "SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY a,api_data_define c " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} AND a.API_DATA_CODE  = c.DATA_CODE  " +
            "AND a.API_DATA_CODE = #{datacode} " +
            "GROUP BY c.DATA_NAME,D_DATETIME " +
            "order by D_DATETIME asc" })
    List<Map<String, Object>>  dataStatByDatacode(String datacode, String startTime, String endTime);

    @Select({"SELECT DATA_NAME,D_DATETIME,SUM(QUERY_TIMES) AS callnum, " +
            "SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_HOR a,api_data_define c " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} AND a.API_DATA_CODE  = c.DATA_CODE  " +
            "AND a.API_DATA_CODE = #{datacode} " +
            "GROUP BY c.DATA_NAME,D_DATETIME " +
            "order by D_DATETIME asc" })
    List<Map<String, Object>>  dataStatByDatacodeByHor(String datacode, String startTime, String endTime);

    @Select({"SELECT b.DEPARTMENT,c.DEPARTMENT_NAME,SUM(QUERY_TIMES) AS callnum, " +
            "SUM(DATA_SIZE) AS download FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} " +
            "AND a.API_DATA_CODE = #{datacode}  " +
            "AND a.SYS_USER_ID = b.user_id  AND b.DEPARTMENT  = c.DEPARTMENT_ID " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "GROUP BY c.DEPARTMENT_NAME,b.DEPARTMENT  ORDER BY download DESC " })
    List<Map<String, Object>>  dataStatByDatacodeGroupByDepartment(String datacode, String startTime, String endTime);

    @Select({"SELECT SUM(DATA_SIZE) AS total FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c " +
            "WHERE D_DATETIME >= #{startTime} AND D_DATETIME <= #{endTime} " +
            "AND a.API_DATA_CODE = #{datacode}  " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND a.SYS_USER_ID = b.user_id  AND b.DEPARTMENT  = c.DEPARTMENT_ID " })
    Long dataStatByDatacodeTotalGroupByDepartment(String datacode, String startTime, String endTime);

    @Select({"SELECT " +
            "b.USER_NAME, " +
            "b.USER_ID , " +
            "SUM(QUERY_TIMES) AS callnum, " +
            "SUM(DATA_SIZE) AS download " +
            "FROM " +
            "API_PERFORMANCE_DAY a, " +
            "API_USER_INFO b " +
            "WHERE " +
            "D_DATETIME >= #{startTime} " +
            "AND D_DATETIME <= #{endTime} " +
            "  AND b.VERIFY_STATUS='Y' " +
            "  AND b.user_grade !='OTHER_GROUP' " +
            "AND a.API_DATA_CODE = #{datacode} " +
            "AND b.DEPARTMENT = #{department} " +
            "AND a.SYS_USER_ID = b.user_id " +
            "GROUP BY " +
            "b.USER_NAME, " +
            "b.USER_ID " +
            "ORDER BY " +
            "download DESC " })
    List<Map<String, Object>>  dataStatByDatacodeAndDepartment(String datacode,String department, String startTime, String endTime);

    @Select({"<script> select m.API_DATA_CODE,callnum,download,n.DATA_NAME,n.DATA_CLASS_ID,s.DATA_CLASS_NAME,s.SERIAL_NO as DATA_CLASS_NO from ( " +
            "SELECT " +
            "a.API_DATA_CODE, " +
            "SUM(QUERY_TIMES) AS callnum, " +
            "SUM(DATA_SIZE) AS download " +
            "FROM " +
            "API_PERFORMANCE_DAY a " +
            "WHERE " +
            "D_DATETIME &gt;= #{startTime} " +
            "AND D_DATETIME &lt;= #{endTime} " +
            "AND a.sys_user_id = #{userid} " +
            "GROUP BY " +
            "a.API_DATA_CODE " +
            " " +
            ") m,API_DATA_DEFINE n ,API_DATA_CLASS_DEFINE s " +
            "where m.API_DATA_CODE=n.DATA_CODE  AND n.DATA_CLASS_ID=s.DATA_CLASS_ID " +
            "<when test='search !=null '>  and (n.DATA_NAME like '%${search}%' or m.API_DATA_CODE like '%${search}%') </when>" +
            "<when test='orderby !=null'> order by ${orderby} </when>"+
            " </script>"})
    List<Map<String, Object>>  dataStatByUseridGroupByDatacode(String userid, String startTime, String endTime,String search,String orderby);

    @Select({"SELECT " +
            "D_DATETIME, " +
            "SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct SYS_USER_ID) as USERNUM " +
            "FROM " +
            "API_PERFORMANCE_DAY " +
            "WHERE " +
            "D_DATETIME >= #{startTime} " +
            "AND D_DATETIME <= #{endTime} " +
            "AND sys_user_id = #{userid} " +
            "GROUP BY " +
            "D_DATETIME " +
            "order by D_DATETIME asc"})
    List<Map<String, Object>>  dataCategoryNumByUserid(String userid, String startTime, String endTime);

    @Select({"SELECT " +
            "D_DATETIME, " +
            "SUM(QUERY_TIMES) AS callnum, " +
            "SUM(DATA_SIZE) AS download " +
            "FROM " +
            "API_PERFORMANCE_DAY " +
            "WHERE " +
            "D_DATETIME >= #{startTime} " +
            "AND D_DATETIME <= #{endTime} " +
            "AND sys_user_id = #{userid} " +
            "AND API_DATA_CODE= #{datacode} " +
            "GROUP BY " +
            "D_DATETIME " +
            "order by D_DATETIME asc "})
    List<Map<String, Object>>  dataStatByUseridAndDatacode(String userid,String datacode, String startTime, String endTime);

    @Select({"select system,user_name from API_USER_INFO where USER_ID=#{userid} "})
    List<Map<String, Object>> queryUserByUserId(String userid);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM " +
            ",SYS_USER_ID,MAX(b.user_grade) as user_grade,max(b.USER_NAME) as USER_NAME,max(b.system) as SYSTEM " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "and b.DEPARTMENT = #{department} " +
            "group by a.SYS_USER_ID " +
            "<when test='orderby !=null'> order by ${orderby} </when>"+
            "<when test='limit !=null'> limit #{limit} </when>"+
            "</script> "})
    List<Map<String, Object>> datastatLimitGroupBydep(String department, String startTime, String endTime,String orderby, String limit);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,MAX(DEPARTMENT_NAME) AS DEPARTMENT_NAME,max(c.SERIAL_NO) AS DEPARTMENT_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND a.API_DATA_CODE= #{datacode} " +
            "<when test='is_country !=null'>  and is_country = #{is_country}  </when>" +
            "GROUP BY b.DEPARTMENT " +
            "order by DEPARTMENT_NO asc </script>"})
    List<Map<String, Object>> topDataDepList(String datacode,String is_country, String startTime, String endTime);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct USER_ID) as USERNUM " +
            ",D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND a.API_DATA_CODE= #{datacode} " +
            "and b.DEPARTMENT=#{department} " +
            "<when test='is_country !=null'>  and is_country = #{is_country}  </when>" +
            "GROUP BY D_DATETIME " +
            "order by D_DATETIME asc " +
            "  </script>"})
    List<Map<String, Object>> dataDepTimeDataList(String department,String is_country,String datacode, String startTime, String endTime);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct USER_ID) as USERNUM " +
            ",a.SYS_USER_ID,max(b.USER_NAME) as USER_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND a.API_DATA_CODE=#{datacode} " +
            "and b.DEPARTMENT=#{department} " +
            "<when test='is_country !=null'>  and is_country = #{is_country}  </when> " +
            "GROUP BY a.SYS_USER_ID " +
            "order by DOWNLOAD desc  </script>"})
    List<Map<String, Object>> dataDepUserDataList(String department,String is_country,String datacode, String startTime, String endTime);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND a.API_DATA_CODE= #{datacode} " +
            "<when test='is_country !=null'>  and is_country = #{is_country}  </when>" +
            "GROUP BY b.DEPARTMENT,D_DATETIME " +
            "order by b.DEPARTMENT,D_DATETIME asc " +
            "  </script>"})
    List<Map<String, Object>> dataDepTimeDataList2(String is_country,String datacode, String startTime, String endTime);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct USER_ID) as USERNUM " +
            ",b.DEPARTMENT,a.SYS_USER_ID,max(b.USER_NAME) as USER_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "AND a.API_DATA_CODE=#{datacode} " +
            "<when test='is_country !=null'>  and is_country = #{is_country}  </when> " +
            "GROUP BY b.DEPARTMENT,a.SYS_USER_ID " +
            "order by b.DEPARTMENT,DOWNLOAD desc  </script>"})
    List<Map<String, Object>> dataDepUserDataList2(String is_country,String datacode, String startTime, String endTime);

    @Select({"<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",b.USER_ID,max(b.user_grade) as user_grade,max(b.DEPARTMENT) as DEPARTMENT,max(c.DEPARTMENT_NAME) as DEPARTMENT_NAME,max(c.SERIAL_NO) as DEPARTMENT_NO,max(b.USER_NAME) as USER_NAME,max(b.SYSTEM) as SYSTEM " +
            ",max(b.PHONE) as PHONE,max(b.EMAIL) as EMAIL,max(b.SYSTEM_DES) as SYSTEM_DES,max(b.CREATE_TIME) as CREATE_TIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP' " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "AND a.API_DATA_CODE = #{datacode} " +
            "<when test='search !=null '>  and (b.SYSTEM like '%${search}%' or b.USER_NAME like '%${search}%' or b.USER_ID like '%${search}%' ) </when>" +
            "GROUP BY b.USER_ID </script>"})
    List<Map<String, Object>> dataUserstatByDatacode(String datacode,String startTime, String endTime,String search, String tokenDepartment);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",d.DATA_CLASS_ID,MAX(DATA_CLASS_NAME) AS DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y' " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime} " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY d.DATA_CLASS_ID " +
            "order by DATA_CLASS_NO asc </script>")
    List<Map<String, Object>> dataClassList(String startTime, String endTime, String tokenDepartment);


    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",d.DATA_CLASS_ID,MAX(e.DATA_CLASS_NAME) AS DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO,a.D_DATETIME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY d.DATA_CLASS_ID,a.D_DATETIME " +
            "order by DATA_CLASS_NO,a.D_DATETIME asc </script>")
    List<Map<String, Object>> dataClassTimeDataList(String startTime, String endTime, String tokenDepartment);

    @Select("<script> SELECT SUM(QUERY_TIMES) AS CALLNUM, SUM(DATA_SIZE) AS DOWNLOAD,count(distinct API_DATA_CODE) as CATEGORYNUM,count(distinct USER_ID) as USERNUM " +
            ",d.DATA_CLASS_ID,MAX(e.DATA_CLASS_NAME) AS DATA_CLASS_NAME,max(e.SERIAL_NO) as DATA_CLASS_NO,d.DATA_CODE,max(d.DATA_NAME) as DATA_NAME " +
            "FROM API_PERFORMANCE_DAY a,API_USER_INFO b,API_DEPARTMENT_INFO_NEW c,API_DATA_DEFINE d,API_DATA_CLASS_DEFINE e " +
            "WHERE a.SYS_USER_ID=b.USER_ID AND b.DEPARTMENT=c.DEPARTMENT_ID AND a.API_DATA_CODE=d.DATA_CODE AND d.DATA_CLASS_ID=e.DATA_CLASS_ID " +
            "AND b.VERIFY_STATUS='Y'  " +
            "and b.user_grade !='OTHER_GROUP'  " +
            "AND a.D_DATETIME &gt;= #{startTime} AND a.D_DATETIME &lt;= #{endTime}  " +
            "<when test='tokenDepartment !=null'> and b.DEPARTMENT = #{tokenDepartment} </when>"+
            "GROUP BY d.DATA_CLASS_ID,d.DATA_CODE " +
            "order by DATA_CLASS_NO,download desc </script>")
    List<Map<String, Object>> dataClassTop5DataList(String startTime, String endTime, String tokenDepartment);

    @Select({"<script> select * from ( SELECT t1.*, " +
            "       CASE " +
            "           WHEN tempcallnum ISNULL THEN 0 " +
            "           ELSE tempcallnum " +
            "       END AS callnum, " +
            "       CASE " +
            "           WHEN tempdownload ISNULL THEN 0 " +
            "           ELSE tempdownload " +
            "       END AS download, " +
            "       CASE " +
            "           WHEN tempcategorynum ISNULL THEN 0 " +
            "           ELSE tempcategorynum " +
            "       END AS categorynum, " +
            "       CASE " +
            "           WHEN tempusernum ISNULL THEN 0 " +
            "           ELSE tempusernum " +
            "       END AS usernum " +
            "FROM " +
            "  (SELECT m.DATA_CODE, " +
            "          m.DATA_NAME, " +
            "          m.DATA_CLASS_ID, " +
            "          m.D_DATA_ID_4FILTER, " +
            "          n.DATA_CLASS_NAME, " +
            "          m.SERIAL_NO_IN_CLASS, " +
            "          n.SERIAL_NO AS DATA_CLASS_NO " +
            "   FROM API_DATA_DEFINE m, " +
            "        API_DATA_CLASS_DEFINE n " +
            "   WHERE m.DATA_CLASS_ID = n.DATA_CLASS_ID AND m.IS_PUBLISHED = 'True' " +
            "<when test='dataname !=null '>  and ( DATA_NAME like '%${dataname}%' or DATA_CODE like '%${dataname}%' ) </when>" +
            "   ) t1 " +
            "LEFT JOIN " +
            "  (SELECT sum(query_times) AS tempcallnum, " +
            "          sum(data_size) AS tempdownload, " +
            "          count(DISTINCT api_data_code) AS tempcategorynum, " +
            "          count(DISTINCT user_id) AS tempusernum, " +
            "          d.DATA_CODE " +
            "   FROM API_PERFORMANCE_DAY a, " +
            "        API_USER_INFO b, " +
            "        API_DEPARTMENT_INFO_NEW c, " +
            "        API_DATA_DEFINE d, " +
            "        API_DATA_CLASS_DEFINE e " +
            "   WHERE a.SYS_USER_ID = b.USER_ID " +
            "     AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "     AND a.API_DATA_CODE = d.DATA_CODE " +
            "     AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "     AND a.D_DATETIME &gt;= #{startTime} " +
            "     AND a.D_DATETIME &lt;= #{endTime} " +
            "            AND b.VERIFY_STATUS = 'Y' " +
            "            AND b.user_grade != 'OTHER_GROUP' " +
            "<when test='usertype !=null '>  and b.user_grade = #{usertype}  </when>" +
            "<when test='departemnt !=null '>  and ( b.DEPARTMENT like '%${departemnt}%' or c.DEPARTMENT_NAME like '%${departemnt}%' ) </when>" +
            "<when test='tokenDepartment !=null '> and b.DEPARTMENT = #{tokenDepartment} </when>" +
            "<when test='system !=null '>  and b.\"system\" like '%${system}%'  </when>" +
            "<when test='username !=null '>  and ( b.user_name like '%${username}%' or b.user_id like '%${username}%' ) </when>" +
            "   GROUP BY d.DATA_CODE) t2 ON t1.DATA_CODE = t2.DATA_CODE " +
            "    WHERE 1=1 " +
            "<when test='minUsernum !=null '>  and usernum &gt;= #{minUsernum}  </when>" +
            "<when test='maxUsernum !=null '>  and usernum &lt;= #{maxUsernum}  </when>" +
            "<when test='minCallnum !=null '>  and callnum &gt;= #{minCallnum}  </when>" +
            "<when test='maxCallnum !=null '>  and callnum &lt;= #{maxCallnum}  </when>" +
            "<when test='minDownload !=null '>  and download &gt;= #{minDownload}  </when>" +
            "<when test='maxDownload !=null '>  and download &lt;= #{maxDownload}  </when>" +
            " ) " +
            "</script>"})
    List<Map<String, Object>> dataunionsearch(String usertype,String departemnt, String tokenDepartment, String system, String username, String dataname,
                                              String startTime, String endTime,
                                              Integer minUsernum,Integer maxUsernum,Long minCallnum,Long maxCallnum,
                                              Long minDownload, Long maxDownload);


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
            "         case when is_check isnull then '' else is_check end as is_check, " +
            "         case when checkdesc isnull then '' else checkdesc end as checkdesc, " +
            "         case when checktime isnull then '' else checktime end as checktime, " +
            "         m.DEPARTMENT, " +
            "         n.DEPARTMENT_NAME , " +
            "         n.SERIAL_NO AS DEPARTMENT_NO " +
            "    FROM API_USER_INFO m, API_DEPARTMENT_INFO_NEW n " +
            "    WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "and USER_ID in <foreach collection='userids' item='userid' open='(' separator=',' close=')'>",
            "#{userid}",
            "</foreach>",
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
            "    GROUP BY  b.USER_ID " +
            "    ORDER BY  categorynum DESC ) t2 " +
            "    ON t1.USER_ID=t2.USER_ID  </script>"})
    List<Map<String, Object>> top50Userstat(Set<String> userids,String startTime, String endTime);


    @Select({"<script> " +
            "SELECT t3.*, " +
            "       t4.* " +
            "FROM " +
            "  (SELECT m.user_id, " +
            "          m.user_name, " +
            "          m.\"SYSTEM\", " +
            "          m.create_time, " +
            "          m.user_grade, " +
            "          CASE " +
            "              WHEN phone ISNULL THEN '' " +
            "              ELSE phone " +
            "          END AS phone, " +
            "          CASE " +
            "              WHEN email ISNULL THEN '' " +
            "              ELSE email " +
            "          END AS email, " +
            "          CASE " +
            "              WHEN system_des ISNULL THEN '' " +
            "              ELSE system_des " +
            "          END AS system_des, " +
            "          CASE " +
            "              WHEN is_check ISNULL THEN '' " +
            "              ELSE is_check " +
            "          END AS is_check, " +
            "          CASE " +
            "              WHEN checkdesc ISNULL THEN '' " +
            "              ELSE checkdesc " +
            "          END AS checkdesc, " +
            "          CASE " +
            "              WHEN checktime ISNULL THEN '' " +
            "              ELSE checktime " +
            "          END AS checktime, " +
            "          m.DEPARTMENT, " +
            "          n.DEPARTMENT_NAME, " +
            "          n.SERIAL_NO AS DEPARTMENT_NO " +
            "   FROM API_USER_INFO m, " +
            "        API_DEPARTMENT_INFO_NEW n " +
            "   WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "     AND m.VERIFY_STATUS = 'Y' " +
            "     AND m.user_grade != 'OTHER_GROUP'" +
            "<when test='usertype !=null'> and m.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and m.DEPARTMENT = #{department} </when>"+
            "<when test='is_check !=null'> and m.is_check = #{is_check} </when>"+
            ") t3 " +
            "LEFT JOIN " +
            "  (SELECT nowdata.USER_ID, " +
            "          nowdata.callnum, " +
            "          nowdata.download, " +
            "          nowdata.categorynum, " +
            "          nowdata.usernum, " +
            "          predata.callnum AS precallnum, " +
            "          predata.download AS predownload, " +
            "          predata.categorynum AS precategorynum, " +
            "          predata.usernum AS preusernum, " +
            "          nowdata.callnum-predata.callnum AS callnumadd, " +
            "          nowdata.download-predata.download AS downloadadd, " +
            "          nowdata.categorynum-predata.categorynum AS categorynumadd," +
            "          CASE " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum!=0 THEN 100 " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum=0 THEN 0 " +
            "              WHEN predata.callnum!=0 AND  nowdata.callnum=0 THEN -100 " +
            "              ELSE  ROUND((nowdata.callnum - predata.callnum)/predata.callnum*100,2) " +
            "          END AS callnumaddpercent, " +
            "          CASE " +
            "              WHEN predata.download=0 AND  nowdata.download!=0 THEN 100 " +
            "              WHEN predata.download=0 AND  nowdata.download=0 THEN 0 " +
            "              WHEN predata.download!=0 AND  nowdata.download=0 THEN -100 " +
            "              ELSE ROUND((nowdata.download - predata.download)/predata.download*100,2) " +
            "          END AS downloadaddpercent, " +
            "          CASE " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum!=0 THEN 100 " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum=0 THEN 0 " +
            "              WHEN predata.categorynum!=0 AND  nowdata.categorynum=0 THEN -100 " +
            "              ELSE ROUND((nowdata.categorynum - predata.categorynum)/predata.categorynum*100,2) " +
            "          END AS categorynumaddpercent " +
            "   FROM " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_DAY a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime1} " +
            "           AND a.D_DATETIME &lt;= #{endTime1} " +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS nowdata, " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_DAY a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime2} " +
            "           AND a.D_DATETIME &lt;= #{endTime2} " +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS predata " +
            "   WHERE nowdata.USER_ID = predata.USER_ID ) t4 ON t3.USER_ID = t4.USER_ID" +
            " </script>"})
    List<Map<String, Object>> top50Userstat2(String usertype,String department,String is_check,String startTime1, String endTime1, String startTime2, String endTime2);


    @Select({"<script> " +
            "SELECT t3.*, " +
            "       t4.* " +
            "FROM " +
            "  (SELECT m.user_id, " +
            "          m.user_name, " +
            "          m.\"SYSTEM\", " +
            "          m.create_time, " +
            "          m.user_grade, " +
            "          CASE " +
            "              WHEN phone ISNULL THEN '' " +
            "              ELSE phone " +
            "          END AS phone, " +
            "          CASE " +
            "              WHEN email ISNULL THEN '' " +
            "              ELSE email " +
            "          END AS email, " +
            "          CASE " +
            "              WHEN system_des ISNULL THEN '' " +
            "              ELSE system_des " +
            "          END AS system_des, " +
            "          CASE " +
            "              WHEN is_check ISNULL THEN '' " +
            "              ELSE is_check " +
            "          END AS is_check, " +
            "          CASE " +
            "              WHEN checkdesc ISNULL THEN '' " +
            "              ELSE checkdesc " +
            "          END AS checkdesc, " +
            "          CASE " +
            "              WHEN checktime ISNULL THEN '' " +
            "              ELSE checktime " +
            "          END AS checktime, " +
            "          m.DEPARTMENT, " +
            "          n.DEPARTMENT_NAME, " +
            "          n.SERIAL_NO AS DEPARTMENT_NO " +
            "   FROM API_USER_INFO m, " +
            "        API_DEPARTMENT_INFO_NEW n " +
            "   WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "     AND m.VERIFY_STATUS = 'Y' " +
            "     AND m.user_grade != 'OTHER_GROUP'" +
            "<when test='usertype !=null'> and m.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and m.DEPARTMENT = #{department} </when>"+
            "<when test='is_check !=null'> and m.is_check = #{is_check} </when>"+
            ") t3 " +
            "LEFT JOIN " +
            "  (SELECT nowdata.USER_ID, " +
            "          nowdata.callnum, " +
            "          nowdata.download, " +
            "          nowdata.categorynum, " +
            "          nowdata.usernum, " +
            "          predata.callnum AS precallnum, " +
            "          predata.download AS predownload, " +
            "          predata.categorynum AS precategorynum, " +
            "          predata.usernum AS preusernum, " +
            "          nowdata.callnum-predata.callnum AS callnumadd, " +
            "          nowdata.download-predata.download AS downloadadd, " +
            "          nowdata.categorynum-predata.categorynum AS categorynumadd," +
            "          CASE " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum!=0 THEN 100 " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum=0 THEN 0 " +
            "              WHEN predata.callnum!=0 AND  nowdata.callnum=0 THEN -100 " +
            "              ELSE  ROUND((nowdata.callnum - predata.callnum)/predata.callnum*100,2) " +
            "          END AS callnumaddpercent, " +
            "          CASE " +
            "              WHEN predata.download=0 AND  nowdata.download!=0 THEN 100 " +
            "              WHEN predata.download=0 AND  nowdata.download=0 THEN 0 " +
            "              WHEN predata.download!=0 AND  nowdata.download=0 THEN -100 " +
            "              ELSE ROUND((nowdata.download - predata.download)/predata.download*100,2) " +
            "          END AS downloadaddpercent, " +
            "          CASE " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum!=0 THEN 100 " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum=0 THEN 0 " +
            "              WHEN predata.categorynum!=0 AND  nowdata.categorynum=0 THEN -100 " +
            "              ELSE ROUND((nowdata.categorynum - predata.categorynum)/predata.categorynum*100,2) " +
            "          END AS categorynumaddpercent " +
            "   FROM " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_HOR a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime1} " +
            "           AND a.D_DATETIME &lt;= #{endTime1} " +
            " AND (SUBSTR(D_DATETIME,9,10) &gt;= '23' OR SUBSTR(D_DATETIME,9,10) &lt;= '08')" +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS nowdata, " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_HOR a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime2} " +
            "           AND a.D_DATETIME &lt;= #{endTime2} " +
            " AND (SUBSTR(D_DATETIME,9,10) &gt;= '23' OR SUBSTR(D_DATETIME,9,10) &lt;= '08')" +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS predata " +
            "   WHERE nowdata.USER_ID = predata.USER_ID ) t4 ON t3.USER_ID = t4.USER_ID" +
            " </script>"})
    List<Map<String, Object>> top50UserDownloadstat(String usertype,String department,String is_check,String startTime1, String endTime1, String startTime2, String endTime2);

    @Select({"SELECT     " +
            "     t1.SYS_USER_ID USERID,     " +
            "     count(DISTINCT t1.API_DATA_CODE ) DATACOUNT,     " +
            "     sum(t1.DATA_SIZE ) VOLUME,     " +
            "     sum(t1.QUERY_TIMES ) CALLNUM     " +
            " FROM     " +
            "     API_PERFORMANCE_DAY t1     " +
            " WHERE     " +
            "     t1.D_DATETIME >= #{startTime}     " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            " GROUP BY t1.SYS_USER_ID "})
    List<Map<String, Object>> getTotalUserAndDataAndVolume(String startTime, String endTime);

    /**
     * 夜间访问量top50平均值
     * @param startTime
     * @param endTime
     * @return
     */
    @Select({"SELECT     " +
            "     t1.SYS_USER_ID USERID,     " +
            "     count(DISTINCT t1.API_DATA_CODE ) DATACOUNT,     " +
            "     sum(t1.DATA_SIZE ) VOLUME,     " +
            "     sum(t1.QUERY_TIMES ) CALLNUM     " +
            " FROM     " +
            "     API_PERFORMANCE_HOR t1     " +
            " WHERE     " +
            "     t1.D_DATETIME >= #{startTime}     " +
            "     AND t1.D_DATETIME <= #{endTime}     " +
            " AND (SUBSTR(D_DATETIME,9,10) >= '23' OR SUBSTR(D_DATETIME,9,10) <= '08') "+
            " GROUP BY t1.SYS_USER_ID "})
    List<Map<String, Object>> getYJDownloadAvg(String startTime, String endTime);

    @Select({"<script>" +
            "SELECT nowdata.USER_ID, " +
            "       nowdata.callnum - predata.callnum AS callnumadd, " +
            "       nowdata.download - predata.download AS downloadadd, " +
            "       nowdata.categorynum - predata.categorynum AS categorynumadd, " +
            "       CASE " +
            "           WHEN predata.callnum=0 " +
            "                AND nowdata.callnum!=0 THEN 100 " +
            "           WHEN predata.callnum=0 " +
            "                AND nowdata.callnum=0 THEN 0 " +
            "           WHEN predata.callnum!=0 " +
            "                AND nowdata.callnum=0 THEN -100 " +
            "           ELSE ROUND((nowdata.callnum - predata.callnum)/predata.callnum*100, 2) " +
            "       END AS callnumaddpercent, " +
            "       CASE " +
            "           WHEN predata.download=0 " +
            "                AND nowdata.download!=0 THEN 100 " +
            "           WHEN predata.download=0 " +
            "                AND nowdata.download=0 THEN 0 " +
            "           WHEN predata.download!=0 " +
            "                AND nowdata.download=0 THEN -100 " +
            "           ELSE ROUND((nowdata.download - predata.download)/predata.download*100, 2) " +
            "       END AS downloadaddpercent, " +
            "       CASE " +
            "           WHEN predata.categorynum=0 " +
            "                AND nowdata.categorynum!=0 THEN 100 " +
            "           WHEN predata.categorynum=0 " +
            "                AND nowdata.categorynum=0 THEN 0 " +
            "           WHEN predata.categorynum!=0 " +
            "                AND nowdata.categorynum=0 THEN -100 " +
            "           ELSE ROUND((nowdata.categorynum - predata.categorynum)/predata.categorynum*100, 2) " +
            "       END AS categorynumaddpercent " +
            "FROM " +
            "  (SELECT t1.*, " +
            "          CASE " +
            "              WHEN callnum ISNULL THEN 0 " +
            "              ELSE callnum " +
            "          END AS callnum, " +
            "          CASE " +
            "              WHEN download ISNULL THEN 0 " +
            "              ELSE download " +
            "          END AS download, " +
            "          CASE " +
            "              WHEN categorynum ISNULL THEN 0 " +
            "              ELSE categorynum " +
            "          END AS categorynum, " +
            "          CASE " +
            "              WHEN usernum ISNULL THEN 0 " +
            "              ELSE usernum " +
            "          END AS usernum " +
            "   FROM " +
            "     (SELECT m.user_id " +
            "      FROM API_USER_INFO m) t1 " +
            "   LEFT JOIN " +
            "     (SELECT sum(query_times) AS callnum, " +
            "             sum(data_size) AS download, " +
            "             count(DISTINCT api_data_code) AS categorynum, " +
            "             count(DISTINCT user_id) AS usernum, " +
            "             b.user_id " +
            "      FROM API_PERFORMANCE_DAY a, " +
            "           API_USER_INFO b, " +
            "           API_DEPARTMENT_INFO_NEW c, " +
            "           API_DATA_DEFINE d, " +
            "           API_DATA_CLASS_DEFINE e " +
            "      WHERE a.SYS_USER_ID = b.USER_ID " +
            "        AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "        AND a.API_DATA_CODE = d.DATA_CODE " +
            "        AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "        AND a.D_DATETIME &gt;= #{startTime} " +
            "        AND a.D_DATETIME &lt;= #{endTime} " +
            "      GROUP BY b.USER_ID " +
            "      ) t2 ON t1.USER_ID = t2.USER_ID) AS nowdata, " +
            " " +
            "  (SELECT t1.*, " +
            "          CASE " +
            "              WHEN callnum ISNULL THEN 0 " +
            "              ELSE callnum " +
            "          END AS callnum, " +
            "          CASE " +
            "              WHEN download ISNULL THEN 0 " +
            "              ELSE download " +
            "          END AS download, " +
            "          CASE " +
            "              WHEN categorynum ISNULL THEN 0 " +
            "              ELSE categorynum " +
            "          END AS categorynum, " +
            "          CASE " +
            "              WHEN usernum ISNULL THEN 0 " +
            "              ELSE usernum " +
            "          END AS usernum " +
            "   FROM " +
            "     (SELECT m.user_id " +
            "      FROM API_USER_INFO m) t1 " +
            "   LEFT JOIN " +
            "     (SELECT sum(query_times) AS callnum, " +
            "             sum(data_size) AS download, " +
            "             count(DISTINCT api_data_code) AS categorynum, " +
            "             count(DISTINCT user_id) AS usernum, " +
            "             b.user_id " +
            "      FROM API_PERFORMANCE_DAY a, " +
            "           API_USER_INFO b, " +
            "           API_DEPARTMENT_INFO_NEW c, " +
            "           API_DATA_DEFINE d, " +
            "           API_DATA_CLASS_DEFINE e " +
            "      WHERE a.SYS_USER_ID = b.USER_ID " +
            "        AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "        AND a.API_DATA_CODE = d.DATA_CODE " +
            "        AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "        AND a.D_DATETIME &gt;= #{startTime1} " +
            "        AND a.D_DATETIME &lt;= #{endTime1} " +
            "      GROUP BY b.USER_ID " +
            "      ) t2 ON t1.USER_ID = t2.USER_ID) AS predata " +
            "WHERE nowdata.USER_ID = predata.USER_ID" +
            "</script>"})
    List<Map<String, Object>> getTotalAddPercent(String startTime, String endTime, String startTime1, String endTime1);

    @Select({"<script> " +
            "SELECT t3.*, " +
            "       t4.* " +
            "FROM " +
            "  (SELECT m.user_id, " +
            "          m.user_name, " +
            "          m.\"SYSTEM\", " +
            "          m.create_time, " +
            "          m.user_grade, " +
            "          CASE " +
            "              WHEN phone ISNULL THEN '' " +
            "              ELSE phone " +
            "          END AS phone, " +
            "          CASE " +
            "              WHEN email ISNULL THEN '' " +
            "              ELSE email " +
            "          END AS email, " +
            "          CASE " +
            "              WHEN system_des ISNULL THEN '' " +
            "              ELSE system_des " +
            "          END AS system_des, " +
            "          CASE " +
            "              WHEN is_check ISNULL THEN '' " +
            "              ELSE is_check " +
            "          END AS is_check, " +
            "          CASE " +
            "              WHEN checkdesc ISNULL THEN '' " +
            "              ELSE checkdesc " +
            "          END AS checkdesc, " +
            "          CASE " +
            "              WHEN checktime ISNULL THEN '' " +
            "              ELSE checktime " +
            "          END AS checktime, " +
            "          m.DEPARTMENT, " +
            "          n.DEPARTMENT_NAME, " +
            "          n.SERIAL_NO AS DEPARTMENT_NO " +
            "   FROM API_USER_INFO m, " +
            "        API_DEPARTMENT_INFO_NEW n " +
            "   WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "     AND m.VERIFY_STATUS = 'Y' " +
            "     AND m.user_grade != 'OTHER_GROUP'" +
            "<when test='usertype !=null'> and m.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and m.DEPARTMENT = #{department} </when>"+
            "<when test='is_check !=null'> and m.is_check = #{is_check} </when>"+
            ") t3 " +
            "LEFT JOIN " +
            "  (SELECT nowdata.USER_ID, " +
            "          nowdata.callnum, " +
            "          nowdata.download, " +
            "          nowdata.categorynum, " +
            "          nowdata.usernum, " +
            "          predata.callnum AS precallnum, " +
            "          predata.download AS predownload, " +
            "          predata.categorynum AS precategorynum, " +
            "          predata.usernum AS preusernum, " +
            "          nowdata.callnum-predata.callnum AS callnumadd, " +
            "          nowdata.download-predata.download AS downloadadd, " +
            "          nowdata.categorynum-predata.categorynum AS categorynumadd," +
            "          CASE " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum!=0 THEN 100 " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum=0 THEN 0 " +
            "              WHEN predata.callnum!=0 AND  nowdata.callnum=0 THEN -100 " +
            "              ELSE  ROUND((nowdata.callnum - predata.callnum)/predata.callnum*100,2) " +
            "          END AS callnumaddpercent, " +
            "          CASE " +
            "              WHEN predata.download=0 AND  nowdata.download!=0 THEN 100 " +
            "              WHEN predata.download=0 AND  nowdata.download=0 THEN 0 " +
            "              WHEN predata.download!=0 AND  nowdata.download=0 THEN -100 " +
            "              ELSE ROUND((nowdata.download - predata.download)/predata.download*100,2) " +
            "          END AS downloadaddpercent, " +
            "          CASE " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum!=0 THEN 100 " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum=0 THEN 0 " +
            "              WHEN predata.categorynum!=0 AND  nowdata.categorynum=0 THEN -100 " +
            "              ELSE ROUND((nowdata.categorynum - predata.categorynum)/predata.categorynum*100,2) " +
            "          END AS categorynumaddpercent " +
            "   FROM " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_DAY a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime1} " +
            "           AND a.D_DATETIME &lt;= #{endTime1} " +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS nowdata, " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_DAY a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime2} " +
            "           AND a.D_DATETIME &lt;= #{endTime2} " +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS predata " +
            "   WHERE nowdata.USER_ID = predata.USER_ID ) t4 ON t3.USER_ID = t4.USER_ID" +
            "   WHERE 1=1 " +
            "<when test='dataMap !=null'> and (" +
            " <foreach collection='dataMap' item='value' index='key' separator='or'> " +
            " ${key} &gt;= #{value} " +
            "</foreach> "+
            ") </when>"+
            " </script>"})
    List<Map<String, Object>> getProblemUser(String usertype,String department,String is_check,String startTime1, String endTime1, String startTime2, String endTime2
                                            ,Map dataMap);

    @Select({"<script> " +
            "SELECT t3.*, " +
            "       t4.* " +
            "FROM " +
            "  (SELECT m.user_id, " +
            "          m.user_name, " +
            "          m.\"SYSTEM\", " +
            "          m.create_time, " +
            "          m.user_grade, " +
            "          CASE " +
            "              WHEN phone ISNULL THEN '' " +
            "              ELSE phone " +
            "          END AS phone, " +
            "          CASE " +
            "              WHEN email ISNULL THEN '' " +
            "              ELSE email " +
            "          END AS email, " +
            "          CASE " +
            "              WHEN system_des ISNULL THEN '' " +
            "              ELSE system_des " +
            "          END AS system_des, " +
            "          CASE " +
            "              WHEN is_check ISNULL THEN '' " +
            "              ELSE is_check " +
            "          END AS is_check, " +
            "          CASE " +
            "              WHEN checkdesc ISNULL THEN '' " +
            "              ELSE checkdesc " +
            "          END AS checkdesc, " +
            "          CASE " +
            "              WHEN checktime ISNULL THEN '' " +
            "              ELSE checktime " +
            "          END AS checktime, " +
            "          m.DEPARTMENT, " +
            "          n.DEPARTMENT_NAME, " +
            "          n.SERIAL_NO AS DEPARTMENT_NO " +
            "   FROM API_USER_INFO m, " +
            "        API_DEPARTMENT_INFO_NEW n " +
            "   WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "     AND m.VERIFY_STATUS = 'Y' " +
            "     AND m.user_grade != 'OTHER_GROUP'" +
            "<when test='usertype !=null'> and m.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and m.DEPARTMENT = #{department} </when>"+
            "<when test='is_check !=null'> and m.is_check = #{is_check} </when>"+
            ") t3 " +
            "LEFT JOIN " +
            "  (SELECT nowdata.USER_ID, " +
            "          nowdata.callnum, " +
            "          nowdata.download, " +
            "          nowdata.categorynum, " +
            "          nowdata.usernum, " +
            "          predata.callnum AS precallnum, " +
            "          predata.download AS predownload, " +
            "          predata.categorynum AS precategorynum, " +
            "          predata.usernum AS preusernum, " +
            "          nowdata.callnum-predata.callnum AS callnumadd, " +
            "          nowdata.download-predata.download AS downloadadd, " +
            "          nowdata.categorynum-predata.categorynum AS categorynumadd," +
            "          CASE " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum!=0 THEN 100 " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum=0 THEN 0 " +
            "              WHEN predata.callnum!=0 AND  nowdata.callnum=0 THEN -100 " +
            "              ELSE  ROUND((nowdata.callnum - predata.callnum)/predata.callnum*100,2) " +
            "          END AS callnumaddpercent, " +
            "          CASE " +
            "              WHEN predata.download=0 AND  nowdata.download!=0 THEN 100 " +
            "              WHEN predata.download=0 AND  nowdata.download=0 THEN 0 " +
            "              WHEN predata.download!=0 AND  nowdata.download=0 THEN -100 " +
            "              ELSE ROUND((nowdata.download - predata.download)/predata.download*100,2) " +
            "          END AS downloadaddpercent, " +
            "          CASE " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum!=0 THEN 100 " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum=0 THEN 0 " +
            "              WHEN predata.categorynum!=0 AND  nowdata.categorynum=0 THEN -100 " +
            "              ELSE ROUND((nowdata.categorynum - predata.categorynum)/predata.categorynum*100,2) " +
            "          END AS categorynumaddpercent " +
            "   FROM " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_HOR a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime1} " +
            "           AND a.D_DATETIME &lt;= #{endTime1} " +
            " AND (SUBSTR(D_DATETIME,9,10) &gt;= '23' OR SUBSTR(D_DATETIME,9,10) &lt;= '08') "+
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS nowdata, " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_HOR a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime2} " +
            "           AND a.D_DATETIME &lt;= #{endTime2} " +
            " AND (SUBSTR(D_DATETIME,9,10) &gt;= '23' OR SUBSTR(D_DATETIME,9,10) &lt;= '08') "+
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS predata " +
            "   WHERE nowdata.USER_ID = predata.USER_ID ) t4 ON t3.USER_ID = t4.USER_ID" +
            "   WHERE 1=1 " +
            "<when test='dataMap !=null'> and (" +
            " <foreach collection='dataMap' item='value' index='key' separator='or'> " +
            " ${key} &gt;= #{value} " +
            "</foreach> "+
            ") </when>"+
            " </script>"})
    List<Map<String, Object>> getYJDownloadProblemUser(String usertype,String department,String is_check,String startTime1, String endTime1, String startTime2, String endTime2
            ,Map dataMap);

    @Select({"<script> " +
            "SELECT DEPARTMENT, count(1) as problemNum from ( " +
            "SELECT t3.*, " +
            "       t4.* " +
            "FROM " +
            "  (SELECT m.user_id, " +
            "          m.user_name, " +
            "          m.\"SYSTEM\", " +
            "          m.create_time, " +
            "          m.user_grade, " +
            "          CASE " +
            "              WHEN phone ISNULL THEN '' " +
            "              ELSE phone " +
            "          END AS phone, " +
            "          CASE " +
            "              WHEN email ISNULL THEN '' " +
            "              ELSE email " +
            "          END AS email, " +
            "          CASE " +
            "              WHEN system_des ISNULL THEN '' " +
            "              ELSE system_des " +
            "          END AS system_des, " +
            "          CASE " +
            "              WHEN is_check ISNULL THEN '' " +
            "              ELSE is_check " +
            "          END AS is_check, " +
            "          CASE " +
            "              WHEN checkdesc ISNULL THEN '' " +
            "              ELSE checkdesc " +
            "          END AS checkdesc, " +
            "          CASE " +
            "              WHEN checktime ISNULL THEN '' " +
            "              ELSE checktime " +
            "          END AS checktime, " +
            "          m.DEPARTMENT, " +
            "          n.DEPARTMENT_NAME, " +
            "          n.SERIAL_NO AS DEPARTMENT_NO " +
            "   FROM API_USER_INFO m, " +
            "        API_DEPARTMENT_INFO_NEW n " +
            "   WHERE m.DEPARTMENT = n.DEPARTMENT_ID " +
            "     AND m.VERIFY_STATUS = 'Y' " +
            "     AND m.user_grade != 'OTHER_GROUP'" +
            "<when test='usertype !=null'> and m.user_grade = #{usertype} </when>"+
            "<when test='department !=null'> and m.DEPARTMENT = #{department} </when>"+
            "<when test='is_check !=null'> and m.is_check = #{is_check} </when>"+
            ") t3 " +
            "LEFT JOIN " +
            "  (SELECT nowdata.USER_ID, " +
            "          nowdata.callnum, " +
            "          nowdata.download, " +
            "          nowdata.categorynum, " +
            "          nowdata.usernum, " +
            "          predata.callnum AS precallnum, " +
            "          predata.download AS predownload, " +
            "          predata.categorynum AS precategorynum, " +
            "          predata.usernum AS preusernum, " +
            "          nowdata.callnum-predata.callnum AS callnumadd, " +
            "          nowdata.download-predata.download AS downloadadd, " +
            "          nowdata.categorynum-predata.categorynum AS categorynumadd," +
            "          CASE " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum!=0 THEN 100 " +
            "              WHEN predata.callnum=0 AND  nowdata.callnum=0 THEN 0 " +
            "              WHEN predata.callnum!=0 AND  nowdata.callnum=0 THEN -100 " +
            "              ELSE  ROUND((nowdata.callnum - predata.callnum)/predata.callnum*100,2) " +
            "          END AS callnumaddpercent, " +
            "          CASE " +
            "              WHEN predata.download=0 AND  nowdata.download!=0 THEN 100 " +
            "              WHEN predata.download=0 AND  nowdata.download=0 THEN 0 " +
            "              WHEN predata.download!=0 AND  nowdata.download=0 THEN -100 " +
            "              ELSE ROUND((nowdata.download - predata.download)/predata.download*100,2) " +
            "          END AS downloadaddpercent, " +
            "          CASE " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum!=0 THEN 100 " +
            "              WHEN predata.categorynum=0 AND  nowdata.categorynum=0 THEN 0 " +
            "              WHEN predata.categorynum!=0 AND  nowdata.categorynum=0 THEN -100 " +
            "              ELSE ROUND((nowdata.categorynum - predata.categorynum)/predata.categorynum*100,2) " +
            "          END AS categorynumaddpercent " +
            "   FROM " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_DAY a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime1} " +
            "           AND a.D_DATETIME &lt;= #{endTime1} " +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS nowdata, " +
            "     (SELECT t1.*, " +
            "             CASE " +
            "                 WHEN callnum ISNULL THEN 0 " +
            "                 ELSE callnum " +
            "             END AS callnum, " +
            "             CASE " +
            "                 WHEN download ISNULL THEN 0 " +
            "                 ELSE download " +
            "             END AS download, " +
            "             CASE " +
            "                 WHEN categorynum ISNULL THEN 0 " +
            "                 ELSE categorynum " +
            "             END AS categorynum, " +
            "             CASE " +
            "                 WHEN usernum ISNULL THEN 0 " +
            "                 ELSE usernum " +
            "             END AS usernum " +
            "      FROM " +
            "        (SELECT m.user_id " +
            "         FROM API_USER_INFO m) t1 " +
            "      LEFT JOIN " +
            "        (SELECT sum(query_times) AS callnum, " +
            "                sum(data_size) AS download, " +
            "                count(DISTINCT api_data_code) AS categorynum, " +
            "                count(DISTINCT user_id) AS usernum, " +
            "                b.user_id " +
            "         FROM API_PERFORMANCE_DAY a, " +
            "              API_USER_INFO b, " +
            "              API_DEPARTMENT_INFO_NEW c, " +
            "              API_DATA_DEFINE d, " +
            "              API_DATA_CLASS_DEFINE e " +
            "         WHERE a.SYS_USER_ID = b.USER_ID " +
            "           AND b.DEPARTMENT = c.DEPARTMENT_ID " +
            "           AND a.API_DATA_CODE = d.DATA_CODE " +
            "           AND d.DATA_CLASS_ID = e.DATA_CLASS_ID " +
            "           AND a.D_DATETIME &gt;= #{startTime2} " +
            "           AND a.D_DATETIME &lt;= #{endTime2} " +
            "         GROUP BY b.USER_ID " +
            "         ) t2 ON t1.USER_ID = t2.USER_ID) AS predata " +
            "   WHERE nowdata.USER_ID = predata.USER_ID ) t4 ON t3.USER_ID = t4.USER_ID" +
            "   WHERE 1=1 " +
            "<when test='dataMap !=null'> and (" +
            " <foreach collection='dataMap' item='value' index='key' separator='or'> " +
            " ${key} &gt;= #{value} " +
            "</foreach> "+
            ") </when>"+
            " ) t5 GROUP BY t5.DEPARTMENT"+
            " </script>"})
    List<Map<String, Object>> getProblemUserGroupByDepartment(String usertype,String department,String is_check,String startTime1, String endTime1, String startTime2, String endTime2
            ,Map dataMap);

    @Select({"select DEPARTMENT_ID,max(DEPARTMENT_NAME) as DEPARTMENT_NAME,max(SERIAL_NO) as DEPARTMENT_NO from API_DEPARTMENT_INFO_NEW a, API_USER_INFO b " +
            " WHERE a.DEPARTMENT_ID=b.DEPARTMENT AND is_country=0 " +
            " group by DEPARTMENT_ID " +
            " order by DEPARTMENT_NO ASC "})
    List<Map<String, Object>> getCountryDepartment();
}
