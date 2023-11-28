package com.piesat.statistics.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.piesat.statistics.service.UserService;
import com.piesat.statistics.bean.SysSession;
import com.piesat.statistics.bean.UserInfo;
import com.piesat.statistics.service.UnitService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    UnitService unitService;

    @GetMapping("/total")
    public Map<String, Object> getTotalCount() {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            Map<String, Object> unitMap = unitService.getDepartmentMap();

            Map<String, Map<String, Object>> unitData =userService.getTotalCountGroupByDeparmentAndType();

            Integer total = userService.getTotalCount();
            Map<String, Object> data = userService.getTotalCountGroupByType();
            data.put("TOTAL", total);

            ret.put("total", data);
            ret.put("unit", unitMap);
            ret.put("unitData", unitData);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/new")
    public Map<String, Object> getNew(String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
            if(startTime.contains("-")){
                if(!startTime.contains(":")){
                    startTime = sdf3.format(sdf3.parse(startTime))+" 00:00:00";
                }
                if(!endTime.contains(":")){
                    endTime = sdf3.format(sdf3.parse(endTime))+" 23:59:59";
                }
            }else{
                startTime = sdf3.format(sdf2.parse(startTime.substring(0,8)))+" 00:00:00";
                endTime = sdf3.format(sdf2.parse(endTime.substring(0,8)))+" 23:59:59";
            }


            Map<String, Object> unitMap = unitService.getDepartmentMap();

            Integer newCount = userService.getNewCount(startTime, endTime);
            Integer disableCount = userService.getDisableCount(startTime, endTime);

            Map<String, Object> newCountByType = userService.getNewCountGroupByType(startTime, endTime);
            Map<String, Object> newCountByDepartment = userService.getNewCountGroupByDepartment(startTime, endTime);
            List headerNewCountGroupByDepartment = userService.getHeaderNewCountGroupByDepartment(startTime, endTime);
            ret.put("headerDepartment", headerNewCountGroupByDepartment);

            List<Map<String, Object>> countListByType = userService.getTotalCountGroupByType(startTime, endTime);
            List<Map<String, Object>> countListByDepartment = userService.getCountGroupByDepartment(startTime, endTime);

            ret.put("newCount", newCount);
            ret.put("disableCount", disableCount);
            ret.put("newCountByType", newCountByType);
            ret.put("newCountByDepartment", newCountByDepartment);
            ret.put("countListByType", countListByType);
            ret.put("countListByDepartment", countListByDepartment);
            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/active")
    public Map<String, Object> getActive(String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            Map<String, Object> unitMap = unitService.getDepartmentMap();

            Integer total = userService.getTotalCount();
            Integer activeTotal = userService.getTotalActiveCount(startTime, endTime);
            Map<String, Object> typeActiveCountMap = userService.getTotalActiveCountByType(startTime, endTime);

            Map<String, Integer> departmentCount = userService.getTotalCountGroupByDepartment();
            Map<String, Integer> activeDepartmentCount = userService.getActiveCountByDepartmentAndType(startTime, endTime);

            Map<String, Map<String, Integer>> departmentCountDetail = new HashMap<>();

            for (String department : departmentCount.keySet()) {
                if (departmentCountDetail.get(department) == null) {
                    departmentCountDetail.put(department, new HashMap<>());
                }

                Map<String, Integer> obj = departmentCountDetail.get(department);

                int t = departmentCount.get(department);
                int a = activeDepartmentCount.get(department) != null ? activeDepartmentCount.get(department) : 0;

                obj.put("total", t);
                obj.put("active", a);
                obj.put("silence", t - a < 0 ? 0 : t - a);
            }

            ret.put("total", total);
            ret.put("activeTotal", activeTotal);
            ret.put("typeActiveCountMap", typeActiveCountMap);
            ret.put("departmentCountDetail", departmentCountDetail);
            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/access")
    public Map<String, Object> getAccess(String startTime, String endTime, String startTime2, String endTime2) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            startTime2 = sdf.format(sdf2.parse(startTime2));
            endTime2 = sdf.format(sdf2.parse(endTime2));

            Map<String, Object> unitMap = unitService.getDepartmentMap();

            Integer total = userService.getAccessCount(startTime, endTime);
            Integer total2 = userService.getAccessCount(startTime2, endTime2);

            Map HeadMap = new TreeMap();

            Map<String, Object> accessCountByType = userService.getAccessCountByType(startTime, endTime);
            Map<String, Object> accessCountByDepartment = userService.getAccessCountByDepartment(startTime, endTime);

            List headerAccessCountDepartment = userService.getHeaderAccessCountDepartment(startTime, endTime);
            ret.put("headerDepartment", headerAccessCountDepartment);

            Map<String, Map<String, Object>> accessCountByDayAndType = userService.getAccessCountByDayAndType(startTime+"00", endTime);
            Map<String, Map<String, Object>> accessCountByDayAndDepartment = userService.getAccessCountByDayAndDepartment(startTime, endTime);

            ret.put("total", total);
            ret.put("total2", total2);

            ret.put("accessCountByType", accessCountByType);
            ret.put("accessCountByDepartment", accessCountByDepartment);
            ret.put("accessCountByDayAndType", accessCountByDayAndType);
            ret.put("accessCountByDayAndDepartment", accessCountByDayAndDepartment);

            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/drillAccess")
    public Map<String, Object> getDrillAccess(String time) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            String[] timeRange = getTimeRange(time);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
           /* startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            startTime2 = sdf.format(sdf2.parse(startTime2));
            endTime2 = sdf.format(sdf2.parse(endTime2));*/

            Map<String, Object> unitMap = unitService.getDepartmentMap();

           /* Integer total = userService.getAccessCount(startTime, endTime);
            Integer total2 = userService.getAccessCount(startTime2, endTime2);

            Map<String, Object> accessCountByType = userService.getAccessCountByType(startTime, endTime);
            Map<String, Object> accessCountByDepartment = userService.getAccessCountByDepartment(startTime, endTime);*/
            List headerAccessCountDepartment = userService.getHeaderAccessHorCountDepartment(timeRange[0], timeRange[1]);
            ret.put("headerDepartment", headerAccessCountDepartment);

            Map<String, Map<String, Object>> accessCountByDayAndType = userService.getAccessCountByHorAndType(timeRange[0], timeRange[1]);
            Map<String, Map<String, Object>> accessCountByDayAndDepartment = userService.getAccessCountByHorAndDepartment(timeRange[0], timeRange[1]);

            //ret.put("total", total);
            //ret.put("total2", total2);

            //ret.put("accessCountByType", accessCountByType);
            //ret.put("accessCountByDepartment", accessCountByDepartment);
            ret.put("accessCountByDayAndType", accessCountByDayAndType);
            ret.put("accessCountByDayAndDepartment", accessCountByDayAndDepartment);

            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/single")
    public Map<String, Object> getAccess(String userId, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            Map<String, Object> unitMap = unitService.getDepartmentMap();

            UserInfo ui = userService.getUserInfoById(userId);
            List<Map<String, Object>> statistics = userService.getSingleUserStatisticsByDay(userId, startTime, endTime);
            Map<String, Map<String, Long>> avgMap = userService.getTotalUserAndDataAndVolume(startTime, endTime);

            for (Map<String, Object> entity : statistics) {
                String datatime = entity.get("DATATIME").toString();

                if (avgMap.get(datatime) != null) {
                    Map<String, Long> subObj = avgMap.get(datatime);

                    entity.put("dataAvg", subObj.get("dataAvg"));
                    entity.put("volumeAvg", subObj.get("volumeAvg"));
                    entity.put("callnumAvg", subObj.get("callnumAvg"));
                }
            }

            ret.put("userInfo", ui);
            ret.put("kindAndVolume", statistics);

            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/drillSingle")
    public Map<String, Object> getDrillAccess(String userId, String time) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {


            Map<String, Object> unitMap = unitService.getDepartmentMap();

            UserInfo ui = userService.getUserInfoById(userId);

            String[] timeRange = getTimeRange(time);

            List<Map<String, Object>> statistics = userService.getSingleUserStatisticsByHor(userId, timeRange[0], timeRange[1]);
            Map<String, Map<String, Long>> avgMap = userService.getTotalUserAndDataAndVolumeByHor(timeRange[0], timeRange[1]);

            for (Map<String, Object> entity : statistics) {
                String datatime = entity.get("DATATIME").toString();

                if (avgMap.get(datatime) != null) {
                    Map<String, Long> subObj = avgMap.get(datatime);

                    entity.put("dataAvg", subObj.get("dataAvg"));
                    entity.put("volumeAvg", subObj.get("volumeAvg"));
                    entity.put("callnumAvg", subObj.get("callnumAvg"));
                }
            }

            ret.put("userInfo", ui);
            ret.put("kindAndVolume", statistics);

            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }


    @GetMapping("/userListByCountry")
    public Map<String, Object> getUserListByCountry(int country) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", userService.getUserListByCountry(country));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/detailByUser")
    public Map<String, Object> detailByUser(String userId, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            UserInfo ui = userService.getUserInfoById(userId);
            List<Map<String, Object>> statistics = userService.getSingleUserStatisticsByDay(userId, startTime, endTime);
            List<Map<String, Object>> volumeByByDataClassID = userService.getVolumeByUserGroupByDataClassIDUseHorTable(userId, startTime, endTime);
            List<Map<String, Object>> detail = userService.getDetailByUser(userId, startTime, endTime);

            Map<String, Object> unitMap = unitService.getDepartmentMap();

            ret.put("userInfo", ui);
            ret.put("kindAndVolume", statistics);
            ret.put("volumeByByDataClassID", volumeByByDataClassID);
            ret.put("detail", detail);

            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/getUserDetail")
    public Map<String, Object> getUserDetail(String userid) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            List list = userService.getUserById(userid);
            if(list!=null&&list.size()>0){
                ret.put("data", list.get(0));
            }else{
                ret.put("data", new HashMap<>());
            }
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/accessTimes")
    public Map<String, Object> accessTimes(String userId, String dataCode, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", userService.accessTimes(userId, dataCode, startTime, endTime));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }
    @GetMapping("/userByType")
    public Map<String, Object> userByType(String usertype,String isnation, String isactive, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        System.out.println("isnation:"+isnation);
        System.out.println("isactive:"+isactive);
        if(isactive==null||"".equalsIgnoreCase(isactive)){
            String startTime1 = startTime +" 00:00:00";
            String endTime1 = endTime +" 00:00:00";
            ret.put("status", 0);
            ret.put("msg", "success");
            try {
                ret.put("data", userService.userByType(usertype, isnation, startTime1, endTime1));
            } catch (Exception e) {
                logger.error("", e);
                ret.put("status", 1);
                ret.put("msg", e.toString());
            }
            return ret;
        }else{
            if("true".equalsIgnoreCase(isactive)){
                System.out.println("isnation:"+isnation);
                String startTime1 = startTime +" 00:00:00";
                String endTime1 = endTime +" 00:00:00";
                ret.put("status", 0);
                ret.put("msg", "success");
                try {
                    ret.put("data", userService.userByTypeByIsactive(true, usertype, isnation, startTime1, endTime1));
                } catch (Exception e) {
                    logger.error("", e);

                    ret.put("status", 1);
                    ret.put("msg", e.toString());
                }
            }else if ("false".equalsIgnoreCase(isactive)){
                System.out.println("isnation:"+isnation);
                String startTime1 = startTime +" 00:00:00";
                String endTime1 = endTime +" 00:00:00";
                ret.put("status", 0);
                ret.put("msg", "success");
                try {
                    ret.put("data", userService.userByTypeByIsactive(false, usertype, isnation, startTime1, endTime1));
                } catch (Exception e) {
                    logger.error("", e);

                    ret.put("status", 1);
                    ret.put("msg", e.toString());
                }
                return ret;
            }else{
                ret.put("status", 1);
                ret.put("msg", "value of 'isactive' is not right!");
                return ret;
            }
        }
        return ret;
    }

    @GetMapping("/userByTypeAndDep")
    public Map<String, Object> userByTypeAndDep(HttpServletRequest request, String usertype,String department, String startTime, String endTime) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }else{
            //只能查看本单位下的数据
            department = tokenDepartment;
        }
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", userService.userByTypeAndDep(usertype, department, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/userCategoryDetail")
    public Map<String, Object> userCategoryDetail(String userid, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", userService.userCategoryDetail(userid, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/userAccessDetail")
    public Map<String, Object> userAccessRecord(String userid, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", userService.userAccessDetail(userid, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/userCount")
    public Map<String, Object> userCount(HttpServletRequest request, String isactive, String startTime, String endTime, String type) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.userTypeCount(startTime, endTime,type,tokenDepartment));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 新增账户 二级页面 各账户类型下的数量
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/newUserToalGroupByUsertype")
    public Map<String, Object> newUserToalGroupByUsertype(String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.newUserToalGroupByUsertype(startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 活跃账户 二级页面 各账户类型下的数量
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/activeUserToalGroupByUsertype")
    public Map<String, Object> activeUserToalGroupByUsertype(String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.activeUserToalGroupByUsertype(startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/activeuserTotalByUsertypeGroupByDepartment")
    public Map<String, Object> activeuserTotalByUsertypeGroupByDepartment(String usertype,String isnation, String isactive, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        System.out.println("isnation:"+isnation);
        System.out.println("isactive:"+isactive);
        String startTime1 = startTime +" 00:00:00";
        String endTime1 = endTime +" 00:00:00";
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.activeuserTotalByUsertypeGroupByDepartment(usertype, isnation, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 	2.3. TAB2二级基本情况头部
     * @return
     */

    /**
     * 	2.4. TAB2二级活跃度柱子
     * @param usertype  账户类型
     * @param is_country    国家级/省级
     * @param isactive  沉默用户/非沉默用户
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/activeUserDepList")
    public Map<String, Object> activeUserDepList(String usertype,String is_country,String isactive, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.activeUserDepList(usertype,is_country,isactive,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 	2.5. TAB1二级变化情况柱子
     * @param usertype
     * @param is_country
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/newUserDepList")
    public Map<String, Object> newUserDepList(String usertype,String is_country, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.newUserDepList(usertype,is_country,startTime, endTime));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 2.6. TAB1二级基本情况柱子
     * @param usertype
     * @return
     */
    @GetMapping("/basicUserDepList")
    public Map<String, Object> basicUserDepList(String usertype) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.basicUserDepList(usertype));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 	2.14. TAB1-栏目4-表格
     * @param usertype
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/accessCountByUserType")
    public Map<String, Object> accessCountByUserType(String usertype, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.accessCountByUserType(usertype, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 	2.14. TAB1-栏目4-折线
     * @param usertype
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/accessCountByUserTypeGroupBy")
    public Map<String, Object> accessCountByUserTypeGroupBy(String usertype, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.accessCountByUserTypeGroupBy(usertype, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/activeUserList")
    public Map<String, Object> activeUserList(HttpServletRequest request, String usertype,String department, String is_country,String isactive, String startTime, String endTime) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }else{
            //只能查看本单位下的数据
            department = tokenDepartment;
        }

        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.activeUserList(usertype,department,is_country,isactive,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/newUserList")
    public Map<String, Object> newUserList(HttpServletRequest request, String usertype,String department, String is_country,String isactive, String startTime, String endTime) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }else{
            //只能查看本单位下的数据
            department = tokenDepartment;
        }
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.newUserList(usertype,department,is_country,isactive,startTime, endTime));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/basicUserList")
    public Map<String, Object> basicUserList(HttpServletRequest request, String usertype,String department, String is_country,String isactive, String startTime, String endTime) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }else{
            //只能查看本单位下的数据
            department = tokenDepartment;
        }
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.basicUserList(usertype,department,is_country,isactive,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }


    /**
     * 	2.4. TAB2二级活跃度柱子
     * @param usertype  账户类型
     * @param is_country    国家级/省级
     * @param isactive  沉默用户/非沉默用户
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/userDepList")
    public Map<String, Object> userDepList(HttpServletRequest request,String usertype,String is_country,String isactive, String startTime, String endTime,String type) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }


        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.userDepList(usertype,is_country,isactive,startTime1, endTime1,type, tokenDepartment));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }


    @GetMapping("/depUserList")
    public Map<String, Object> depUserList(HttpServletRequest request, String department, String startTime, String endTime, String type) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }else{
            //只能查看本单位下的数据
            department = tokenDepartment;
        }
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", userService.depUserList(department,startTime1, endTime1, type));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }


    @GetMapping("/userstatByUsertypeAndDepartment")
    public Map<String, Object> userstatByUsertypeAndDepartment(HttpServletRequest request, String usertype,String department, String data_class_id,
                                                               String startTime, String endTime,String searchtxt,String type,String verify_status,
                                                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                               @RequestParam(value = "orderName", required = false, defaultValue = "download") String orderName,
                                                               @RequestParam(value = "orderType", required = false, defaultValue = "desc") String orderType) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }else{
            //只能查看本单位下的数据
            department = tokenDepartment;
        }
        if("4".equals(type)){
            orderName = "user_id";
        }
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize, orderName + " " + orderType);

            if("".equalsIgnoreCase(data_class_id)){
                data_class_id = null;
            }
            List<Map<String, Object>> maps = userService.userstatByUsertypeAndDepartment("".equals(usertype)?null:usertype, "".equals(department)?null:department, data_class_id, startTime1, endTime1,"".equals(searchtxt)?null:searchtxt,type, verify_status);

            long startRow = objects.getStartRow();
            if(maps!=null&&maps.size()>0){
                for(int i = 0;i<maps.size();i++){
                    maps.get(i).put("ROWNO",startRow+1+i);
                }
            }

            ret.put("data", maps);
            Object o = userService.userstatByUsertypeAndDepartmentTotal("".equals(usertype)?null:usertype, "".equals(department)?null:department, data_class_id, startTime1, endTime1,"".equals(searchtxt)?null:searchtxt,type, verify_status).get(0);;
            ret.put("stat", o);
            Map pageMap = new TreeMap();
            pageMap.put("total",objects.getTotal());
            pageMap.put("pageNum",objects.getPageNum());
            pageMap.put("pageSize",objects.getPageSize());
            pageMap.put("pages",objects.getPages());
            pageMap.put("startRow",objects.getStartRow());
            pageMap.put("endRow",objects.getEndRow());
            pageMap.put("orderBy",objects.getOrderBy());
            ret.put("pageinfo",pageMap);
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/userstatByUserid")
    public Map<String, Object> userstatByUserid(String userid, String startTime, String endTime,String searchtxt,
                                                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                               @RequestParam(value = "orderName", required = false, defaultValue = "download") String orderName,
                                                               @RequestParam(value = "orderType", required = false, defaultValue = "desc") String orderType) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize, "DATA_CLASS_NO ASC," + orderName + " " + orderType);
            List<Map<String, Object>> maps = userService.userstatByUserid(userid, startTime1, endTime1,"".equals(searchtxt)?null:searchtxt);
            long startRow = objects.getStartRow();
            if(maps!=null&&maps.size()>0){
                for(int i = 0;i<maps.size();i++){
                    maps.get(i).put("ROWNO",startRow+1+i);
                }
            }
            ret.put("data", maps);
            Map pageMap = new TreeMap();
            pageMap.put("total",objects.getTotal());
            pageMap.put("pageNum",objects.getPageNum());
            pageMap.put("pageSize",objects.getPageSize());
            pageMap.put("pages",objects.getPages());
            pageMap.put("startRow",objects.getStartRow());
            pageMap.put("endRow",objects.getEndRow());
            pageMap.put("orderBy",objects.getOrderBy());
            ret.put("pageinfo",pageMap);
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/userstatByUseridTop5")
    public Map<String, Object> userstatByUseridTop5(String userid, String startTime, String endTime,String searchtxt,
                                                    @RequestParam(value = "orderName", required = false, defaultValue = "download") String orderName,
                                                    @RequestParam(value = "orderType", required = false, defaultValue = "desc") String orderType) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            Page<Object> objects = PageHelper.startPage(1, 5, orderName + " " + orderType);
            List<Map<String, Object>> maps = userService.userstatByUserid(userid, startTime1, endTime1, "".equals(searchtxt)?null:searchtxt);
            long startRow = objects.getStartRow();
            if(maps!=null&&maps.size()>0){
                for(int i = 0;i<maps.size();i++){
                    maps.get(i).put("ROWNO",startRow+1+i);
                }
            }
            ret.put("data", maps);
            Map pageMap = new TreeMap();
            pageMap.put("total",objects.getTotal());
            pageMap.put("pageNum",objects.getPageNum());
            pageMap.put("pageSize",objects.getPageSize());
            pageMap.put("pages",objects.getPages());
            pageMap.put("startRow",objects.getStartRow());
            pageMap.put("endRow",objects.getEndRow());
            pageMap.put("orderBy",objects.getOrderBy());
            ret.put("pageinfo",pageMap);
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/userDataTimeDetail")
    public Map<String, Object> userDataTimeDetail(String userid, String datacode, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            List<Map<String, Object>> maps = userService.userDataTimeDetail(userid, datacode, startTime1, endTime1);
            ret.put("data", maps);
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    public static String[] getTimeRange(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
        Date parse;
        if(time.contains("-")){
            parse = sdf3.parse(time);
        }else{
            if(time.length()==8){
                parse = sdf4.parse(time);
            }else{
                parse = sdf2.parse(time);
            }
        }

        Calendar cal  = Calendar.getInstance();
        cal.setTime(parse);
        cal.add(Calendar.HOUR_OF_DAY,-1);
        String startTime = sdf.format(cal.getTime());
        cal.add(Calendar.HOUR_OF_DAY,24);
        String endTime = sdf.format(cal.getTime());
        String[] ss = new String[]{startTime,endTime};
        return ss;
    }

    /**
     * 高级检索
     */
    @RequestMapping(value = "/userunionsearch")
    public Map<String, Object> unionSerach(HttpServletRequest request, @RequestBody Map param) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            Map map = analyParam(param);

        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            map.put("tokenDepartment",null);
        }else{
            //只能查看本单位下的数据
            map.put("tokenDepartment",tokenDepartment);
        }
            Page<Object> objects = PageHelper.startPage((Integer)map.get("pageNum"), (Integer)map.get("pageSize"),  map.get("orderName") + " " + map.get("orderType"));
            List<Map<String, Object>> maps = userService.userunionsearch(map);
            long startRow = objects.getStartRow();
            if(maps!=null&&maps.size()>0){
                for(int i = 0;i<maps.size();i++){
                    maps.get(i).put("ROWNO",startRow+1+i);
                }
            }
            ret.put("data", maps);
            /*Object o = userService.userstatByUsertypeAndDepartmentTotal("".equals(usertype)?null:usertype, "".equals(department)?null:department, data_class_id, startTime1, endTime1,"".equals(searchtxt)?null:searchtxt,type).get(0);;
            ret.put("stat", o);*/
            Map pageMap = new TreeMap();
            pageMap.put("total",objects.getTotal());
            pageMap.put("pageNum",objects.getPageNum());
            pageMap.put("pageSize",objects.getPageSize());
            pageMap.put("pages",objects.getPages());
            pageMap.put("startRow",objects.getStartRow());
            pageMap.put("endRow",objects.getEndRow());
            pageMap.put("orderBy",objects.getOrderBy());
            ret.put("pageinfo",pageMap);
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    public Map analyParam(Map param){
        Map map = new HashMap();

        JSONObject json = new JSONObject(param);
        System.out.println(json.toJSONString());
        JSONArray queryParams = json.getJSONArray("queryParam");
        if(queryParams!=null&&queryParams.size()>0){
            for(int i=0;i<queryParams.size();i++){
                JSONObject queryParam = queryParams.getJSONObject(i);
                String name = queryParam.getString("name");
                String value = queryParam.getString("value");
                if("".equals(value)){
                    value = null;
                }
                if("categorynum".equals(name)){
                    String minCategorynum = queryParam.getString("minValue");
                    String maxCategorynum = queryParam.getString("maxValue");
                    map.put("minCategorynum",!StringUtils.isNumeric(minCategorynum)?null:Integer.parseInt(minCategorynum));
                    map.put("maxCategorynum",!StringUtils.isNumeric(maxCategorynum)?null:Integer.parseInt(maxCategorynum));
                }else if("callnum".equals(name)){
                    String minCallnum = queryParam.getString("minValue");
                    String maxCallnum = queryParam.getString("maxValue");
                    map.put("minCallnum", !StringUtils.isNumeric(minCallnum)?null:Long.parseLong(minCallnum));
                    map.put("maxCallnum",!StringUtils.isNumeric(maxCallnum)?null:Long.parseLong(maxCallnum));
                }else if("download".equals(name)){
                    //默认是GB
                    String minDownload = queryParam.getString("minValue");
                    String maxDownload = queryParam.getString("maxValue");
                    map.put("minDownload",!StringUtils.isNumeric(minDownload)?null:Long.parseLong(minDownload)*1024*1024*1024);
                    map.put("maxDownload",!StringUtils.isNumeric(maxDownload)?null:Long.parseLong(maxDownload)*1024*1024*1024);
                }else if("startTime".equals(name)){
                    map.put(name,value.replaceAll("-",""));
                }else if("endTime".equals(name)){
                    map.put(name,value.replaceAll("-",""));
                }else{
                    map.put(name,value);
                }
            }
        }

        JSONObject pageParam = json.getJSONObject("pageParam");
        int pageNum = 1;
        int pageSize = 10;
        String orderName = "download";
        String orderType = "desc";
        if(pageParam!=null){
            String pageNum1 = pageParam.getString("pageNum");
            String pageSize1 = pageParam.getString("pageSize");
            String orderName1 = pageParam.getString("orderName");
            String orderType1 = pageParam.getString("orderType");
            if(pageNum1!=null&&!"".equals(pageNum1)&& NumberUtils.isDigits(pageNum1)){
                pageNum = Integer.parseInt(pageNum1);
            }
            if(pageSize1!=null&&!"".equals(pageSize1)&& NumberUtils.isDigits(pageSize1)){
                pageSize = Integer.parseInt(pageSize1);
            }
            if(orderName1!=null&&!"".equals(orderName1)){
                orderName = orderName1;
            }
            if(orderType1!=null&&!"".equals(orderType1)){
                orderType = orderType1;
            }
        }
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        map.put("orderName",orderName);
        map.put("orderType",orderType);
        return map;
    }

    /**
     * 用户审核
     */
    @PostMapping(value = "/checkuser")
    public Map<String, Object> checkuser(HttpServletRequest request,String userid,String checkdesc) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            String token = request.getHeader("token");
            SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
            String tokenDepartment = sysSession.getDepartment();
            Object o = userService.getUserById(userid).get(0);
            if(o!=null){
                Map o1 = (Map) o;
                String department = o1.get("DEPARTMENT").toString();
                if(tokenDepartment.equals(department)){
                    userService.checkuser(userid,checkdesc);
                }else{
                    ret.put("status", 1);
                    ret.put("msg", "only can modify user in your deparmemt");
                }
            }

        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 用户审核
     */
    @PostMapping(value = "/uncheckuser")
    public Map<String, Object> uncheckuser(HttpServletRequest request,String userid,String checkdesc) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            String token = request.getHeader("token");
            SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
            String tokenDepartment = sysSession.getDepartment();
            Object o = userService.getUserById(userid).get(0);
            if(o!=null){
                Map o1 = (Map) o;
                String department = o1.get("DEPARTMENT").toString();
                if(tokenDepartment.equals(department)){

                    userService.uncheckuser(userid,checkdesc);
                }else{
                    ret.put("status", 1);
                    ret.put("msg", "only can modify user in your deparmemt");
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

}
