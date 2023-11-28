package com.piesat.statistics.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.piesat.statistics.service.MonitorCheckUserService;
import com.piesat.statistics.bean.DataBean;
import com.piesat.statistics.bean.DepartmentBean;
import com.piesat.statistics.bean.SysSession;
import com.piesat.statistics.service.DataService;
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
@RequestMapping("/data")
public class DataController {
    private final static Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    DataService dataService;

    @Autowired
    UnitService unitService;
    @Autowired
    private MonitorCheckUserService monitorCheckUserService;

    @GetMapping("/basic")
    public Map<String, Object> getBasic(String startTime, String endTime) {
        System.out.println("...");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            Map<String, Object> unitMap = unitService.getDepartmentMap();
            Map<String, DataBean> dataDefineMap = dataService.getAllPublishedData();
            List<String> usedDataList = dataService.getAccessData(startTime, endTime);
            Map<String, Object> accessByDepartment = dataService.getAccessByDepartment(startTime, endTime);


            ret.put("total", dataDefineMap.size());
            ret.put("used", usedDataList.size());
            ret.put("unused", dataDefineMap.size() - usedDataList.size());
            ret.put("accessByDepartment", accessByDepartment);

            ret.put("dataDefineMap", dataDefineMap);
            ret.put("unit", unitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/download")
    public Map<String, Object> getDownload(String startTime, String endTime, String startTime2, String endTime2) {
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

            Long total = dataService.getTotalVolume(startTime, endTime);
            Long total2 = dataService.getTotalVolume(startTime2, endTime2);

            Map<String, Object> volumeByUserType = dataService.getVolumeByGroupId(startTime, endTime);
            Map<String, Map<String, Object>> volumeByDatetimeAndUserType = dataService.getVolumeByDatetimeAndUserType(startTime, endTime);

            ret.put("total", total);
            ret.put("total2", total2);
            ret.put("volumeByUserType", volumeByUserType);
            ret.put("volumeByDatetimeAndUserType", volumeByDatetimeAndUserType);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/drillDownload")
    public Map<String, Object> getDrillDownload(String time) {

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
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
            cal.add(Calendar.HOUR_OF_DAY,-36);
            String startTime = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH,3);
            String endTime = sdf.format(cal.getTime());

            //Long total = dataService.getTotalVolume(startTime, endTime);

            Map<String, Map<String, Object>> volumeByDatetimeAndUserType = dataService.getDrillVolumeByDatetimeAndUserType(startTime, endTime);

            //ret.put("total", total);
            ret.put("volumeByDatetimeAndUserType", volumeByDatetimeAndUserType);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/unitdown")
    public Map<String, Object> getUnitDownload(String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            Map<String, Object> unitMap = unitService.getDepartmentMap();
            Map<String, Object> unitLevel = unitService.getDepartmentLevelMap();

            Long total = dataService.getTotalVolume(startTime, endTime);

            Map<String, Object> volumeByDepartment = dataService.getVolumeByDepartment(startTime, endTime);

            ret.put("total", total);
            ret.put("volumeByDepartment", volumeByDepartment);

            ret.put("unit", unitMap);
            ret.put("unitLevel", unitLevel);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/userdown")
    public Map<String, Object> getDownloadByUserType(String startTime, String endTime, String userType, int limit) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            Long total = dataService.getTotalVolumeByGroupId(userType, startTime, endTime);

            List<Map<String, Object>> volumeByUserType = dataService.getVolumeByUserType(startTime, endTime, userType, limit);

            ret.put("total", total);
            ret.put("volumeByUserType", volumeByUserType);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/detailByData")
    public Map<String, Object> detailByData(int country, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            Map<String, DepartmentBean> unitMap = unitService.getDepartmentBeanMap();
            Map<String, DataBean> dataDefineMap = dataService.getAllPublishedData();

            List<Map<String, Object>> volumeByDataClassId = dataService.getVolumeByDataClassId(country, startTime, endTime);
            Map<String, Map<String, Object>> volumeByDataIdAndDepartment = dataService.getVolumeByDataIdAndDepartment(country, startTime, endTime);

            Map<String, Boolean> units = new HashMap<>();
            for (String dataCode : volumeByDataIdAndDepartment.keySet()) {
                for (String unitCode : volumeByDataIdAndDepartment.get(dataCode).keySet()) {
                    units.put(unitCode, true);
                }
            }

            Map<String, DepartmentBean> filterUnitMap = new HashMap<>();
            for (String code : units.keySet()) {
                filterUnitMap.put(code, unitMap.get(code));
            }

            ret.put("volumeByDataClassId", volumeByDataClassId);
            ret.put("volumeByDataIdAndDepartment", volumeByDataIdAndDepartment);

            ret.put("dataDefineMap", dataDefineMap);
            ret.put("unit", filterUnitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/detailByUsertype")
    public Map<String, Object> detailByUserType(int country, String startTime, String endTime) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
            startTime = sdf.format(sdf2.parse(startTime));
            endTime = sdf.format(sdf2.parse(endTime));

            Map<String, DepartmentBean> unitMap = unitService.getDepartmentBeanMap();
            Map<String, DataBean> dataDefineMap = dataService.getAllPublishedData();

            List<Map<String, Object>> volumeByUserType = dataService.getVolumeFilterCountryByGroupId(country, startTime, endTime);
            Map<String, Map<String, Map<String, Object>>> volumeByUserTypeAndDataIdAndDepartment = dataService.getVolumeByUserTypeGroupByDataIdAndDepartment(country, startTime, endTime);

            Map<String, Boolean> units = new HashMap<>();
            for (String userType : volumeByUserTypeAndDataIdAndDepartment.keySet()) {
                for (String dataCode : volumeByUserTypeAndDataIdAndDepartment.get(userType).keySet()) {
                    for (String unitCode : volumeByUserTypeAndDataIdAndDepartment.get(userType).get(dataCode).keySet()) {
                        units.put(unitCode, true);
                    }
                }
            }

            Map<String, DepartmentBean> filterUnitMap = new HashMap<>();
            for (String code : units.keySet()) {
                filterUnitMap.put(code, unitMap.get(code));
            }

            ret.put("volumeByUserType", volumeByUserType);
            ret.put("volumeByUserTypeAndDataIdAndDepartment", volumeByUserTypeAndDataIdAndDepartment);

            ret.put("dataDefineMap", dataDefineMap);
            ret.put("unit", filterUnitMap);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataClassCount")
    public Map<String, Object> dataClassCount(HttpServletRequest request, String startTime, String endTime) {
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
            ret.put("data", dataService.dataClassCount(startTime1, endTime1, tokenDepartment));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataDetailList")
    public Map<String, Object> dataDetailList(HttpServletRequest request, String data_class_id,String startTime, String endTime,String searchtxt,
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
        }
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            Object o = dataService.dataClassTotal(data_class_id, startTime1, endTime1, "".equals(searchtxt) ? null : searchtxt, tokenDepartment).get(0);

            ret.put("stat", o);
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize, orderName + " " + orderType);
            List<Map<String, Object>> maps = dataService.dataDetailList(data_class_id, startTime1, endTime1, "".equals(searchtxt) ? null : searchtxt, tokenDepartment);
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

    @GetMapping("/dataDetailListByLimit")
    public Map<String, Object> dataDetailListByLimit(String data_class_id,String startTime, String endTime,Integer limit) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", dataService.dataDetailListByLimit(data_class_id,startTime1, endTime1,limit));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataCategoryCountGroupByDepartment")
    public Map<String, Object> dataCategoryCountGroupByDepartment(HttpServletRequest request, String is_country, String startTime, String endTime,String type) {
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
            ret.put("data", dataService.dataCategoryCountGroupByDepartment(is_country, startTime1, endTime1, tokenDepartment, type));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataCategoryCountByDepartment")
    public Map<String, Object> dataCategoryCountByDepartment(HttpServletRequest request, String department,String startTime, String endTime) {
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
            ret.put("data", dataService.dataCategoryCountByDepartment(department,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataCategoryCountGroupByUsertype")
    public Map<String, Object> dataCategoryCountGroupByUserType(String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", dataService.dataCategoryCountGroupByUserType(startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataCategoryCountByUsertype")
    public Map<String, Object> dataCategoryCountByUserType(String usertype,String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", dataService.dataCategoryCountByUserType(usertype,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataCountByDepartmentAndUsertype")
    public Map<String, Object> dataCountByDepartmentAndUsertype(HttpServletRequest request, String department, String usertype, String startTime, String endTime) {
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
            ret.put("data", dataService.dataCountByDepartmentAndUserType(department, usertype, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/dataCountGroupByDataclass")
    public Map<String, Object> dataCountGroupByDataclass(String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        try {
            ret.put("data", dataService.dataCountGroupByDataclass(startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }

        return ret;
    }

    @GetMapping("/userTop5")
    public Map<String, Object> userTop5(HttpServletRequest request, String type, String startTime, String endTime) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }

        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            if("category".equals(type)){
                ret.put("data", dataService.accessCategoryTop5(startTime1, endTime1,tokenDepartment));
            }else if("callnum".equals(type)){
                ret.put("data", dataService.accessNumTop5(startTime1, endTime1,tokenDepartment));
            }else if("download".equals(type)){
                String startTime2 = startTime.replaceAll("-","")+"00";
                String endTime2 = endTime.replaceAll("-","")+"23";;
                ret.put("data", dataService.downloadTop5(startTime2, endTime2,tokenDepartment));
            }else if("callnumadd".equals(type)){
                ret.put("data", dataService.accessNumAddTop5(startTime1, endTime1,tokenDepartment));
            }else if("downloadadd".equals(type)){
                ret.put("data", dataService.downlaodAddTop5(startTime1, endTime1,tokenDepartment));
            }
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/userTop50")
    public Map<String, Object> userTop50(HttpServletRequest request, String usertype,String department, String type, String startTime, String endTime,String is_check,
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


        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            Set<String> userids = null;
            if("category".equals(type)){
                orderName = "categorynum";
            }else if("callnum".equals(type)){
                orderName = "callnum";
            }else if("download".equals(type)){
                orderName = "download";
                String startTime2 = startTime.replaceAll("-","")+"00";
                String endTime2 = endTime.replaceAll("-","")+"00";;
            }else if("callnumadd".equals(type)){
                orderName = "CALLNUMADDPERCENT";
            }else if("downloadadd".equals(type)){
                orderName = "DOWNLOADADDPERCENT";
            }else if("categoryadd".equals(type)){
                orderName = "CATEGORYNUMADDPERCENT";
            }
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize, orderName + " " + orderType);
            List<Map<String, Object>> maps = new ArrayList<>();
            if("download".equals(type)){
                String startTime2 = startTime.replaceAll("-","")+"00";
                String endTime2 = endTime.replaceAll("-","")+"23";;
                maps = dataService.getTop50DownloadData("".equals(usertype)?null:usertype, "".equals(department)?null:department,"".equals(is_check)?null:is_check,startTime2, endTime2, type);
            }else{
                maps = dataService.getTop50Data("".equals(usertype)?null:usertype, "".equals(department)?null:department,"".equals(is_check)?null:is_check,startTime1, endTime1, type);
            }
            long startRow = objects.getStartRow();
            if(maps!=null&&maps.size()>0){
                for(int i = 0;i<maps.size();i++){
                    maps.get(i).put("ROWNO",startRow+1+i);
                }
            }
            ret.put("data",maps);
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

    @GetMapping("/dataTop5")
    public Map<String, Object> dataTop5(String type,String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataTop5(startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/dataTop5InDepartment")
    public Map<String, Object> dataTop5InDepartment(HttpServletRequest request, String type,String startTime, String endTime) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部
        if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataTop5InDepartment(startTime1, endTime1, tokenDepartment));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/dataTop")
    public Map<String, Object> dataTop(HttpServletRequest request,String type,String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;

        //不登录时查看信息中心的，登录后查看各单位自己定制的数据
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment;
        if(sysSession==null){
            tokenDepartment = "NMIC";
        }else{
            tokenDepartment = sysSession.getDepartment();
        }

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataTop(startTime1, endTime1,tokenDepartment));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/dataTopInDepartment")
    public Map<String, Object> dataTopInDepartment(HttpServletRequest request, String type,String startTime, String endTime) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部-此处不看全部了，看自己单位配置的资料列表
        /*if("NMIC".equals(tokenDepartment)){
            tokenDepartment = null;
        }*/
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataTopInDepartment(startTime1, endTime1, tokenDepartment));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }


    @GetMapping("/dataCountByDatacode")
    public Map<String, Object> dataCountByDatacode(String datacode,Integer limit, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataCountByDatacode(datacode,limit,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }


    /**
     * 近一月资料监管情况
     * @param datacode
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/dataStatByDatacode")
    public Map<String, Object> dataStatByDatacode(String datacode, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataStatByDatacode(datacode,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 近一月资料监管情况
     * @param datacode
     * @return
     */
    @GetMapping("/drillDataStatByDatacode")
    public Map<String, Object> drillDataStatByDatacode(String datacode, String time) {

        String[] timeRange = new String[0];
        try {
            timeRange = getTimeRange(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;*/
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataStatByDatacodeByHor(datacode,timeRange[0], timeRange[1]));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 重点资料监管情况-二级页面
     * @param datacode
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/dataStatByDatacodeGroupByDepartment")
    public Map<String, Object> dataStatByDatacodeGroupByDepartment(String datacode, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataStatByDatacodeGroupByDepartment(datacode,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 重点资料监管情况-二级页面
     * @param datacode
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/dataStatByDatacodeAndDepartment")
    public Map<String, Object> dataStatByDatacodeAndDepartment(HttpServletRequest request, String datacode,String department, String startTime, String endTime) {
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
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataStatByDatacodeAndDepartment(datacode,department,startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 重点账户监管情况-二级页面
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/dataStatByUseridGroupByDatacode")
    public Map<String, Object> dataStatByUseridGroupByDatacode(String userid, String startTime, String endTime,String searchtxt,
                                                               @RequestParam(value = "orderName", required = false, defaultValue = "download") String orderName,
                                                               @RequestParam(value = "orderType", required = false, defaultValue = "desc") String orderType) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            //String orderbyStr = orderName+" "+orderType;
            String orderbyStr = getOrderByStr(orderName,orderType);
            ret.put("data", dataService.dataStatByUseridGroupByDatacode(userid,startTime1, endTime1,"".equals(searchtxt)?null:searchtxt,orderbyStr));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 重点账户监管情况-二级页面
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/dataCategoryNumByUserid")
    public Map<String, Object> dataCategoryNumByUserid(String userid, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataCategoryNumByUserid(userid, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 重点账户监管情况-二级页面
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/dataStatByUseridAndDatacode")
    public Map<String, Object> dataStatByUseridAndDatacode(String userid,String datacode, String startTime, String endTime) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.dataStatByUseridAndDatacode(userid, datacode, startTime1, endTime1));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    /**
     * 重点账户监管情况-二级页面
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/datastatGroupBydep")
    public Map<String, Object> datastatGroupBydep(HttpServletRequest request, String department, String startTime, String endTime, String orderby, String limit) {
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
        String endTime1 = endTime.replaceAll("-","");;
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.datastatGroupBydep(department, startTime1, endTime1, orderby, limit));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }


    @GetMapping("/topDataDepList")
    public Map<String, Object> topDataDepList(String datacode,String is_country, String startTime, String endTime,String type) {
        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.topDataDepList(datacode,is_country,startTime1, endTime1,type));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/dataUserstatByDatacode")
    public Map<String, Object> dataUserstatByDatacode(HttpServletRequest request, String datacode, String startTime, String endTime,String searchtxt,
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
        }

        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        String orderBy = getOrderByStr(orderName,orderType);
        try {
            Page<Object> objects = PageHelper.startPage(pageNum, pageSize, orderBy);
            List<Map<String, Object>> maps = dataService.dataUserstatByDatacode(datacode, startTime1, endTime1,"".equals(searchtxt) ? null : searchtxt,tokenDepartment);
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

    public static String getOrderByStr(String orderName, String orderType){
        StringBuilder sb = new StringBuilder();
        String orderBy = "";
        if(orderName!=null&&!"".equals(orderName)){
            String[] split = orderName.split(",");
            String[] split1 = orderType.split(",");
            for (int i=0;i<split.length;i++) {
                String str = split[i];
                if(i<split1.length){
                    sb.append(str+" " +split1[i] + ",");
                }else{
                    sb.append(str+" " +split1[split1.length-1] + ",");
                }
            }
            orderBy = sb.substring(0,sb.length()-1);
        }
        return orderBy;
    }

    @GetMapping("/dataUserstatByDatacodeTop5")
    public Map<String, Object> dataUserstatByDatacodeTop5(HttpServletRequest request, String datacode, String startTime, String endTime,String searchtxt,
                                                      @RequestParam(value = "orderName", required = false, defaultValue = "download") String orderName,
                                                      @RequestParam(value = "orderType", required = false, defaultValue = "desc") String orderType) {
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
            Page<Object> objects = PageHelper.startPage(1, 5, "DEPARTMENT_NO ASC," + orderName + " " + orderType);
            List<Map<String, Object>> maps = dataService.dataUserstatByDatacodeTop5(datacode, startTime1, endTime1,"".equals(searchtxt) ? null : searchtxt, tokenDepartment);
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


    @GetMapping("/dataClassList")
    public Map<String, Object> dataClassList(HttpServletRequest request, String startTime, String endTime) {
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
            ret.put("data", dataService.dataClassList(startTime1, endTime1, tokenDepartment));
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
    @RequestMapping(value = "/dataunionsearch")
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
            List<Map<String, Object>> maps = dataService.dataunionsearch(map);
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
                if("usernum".equals(name)){
                    String minUsernum = queryParam.getString("minValue");
                    String maxUsernum = queryParam.getString("maxValue");
                    map.put("minUsernum",!StringUtils.isNumeric(minUsernum)?null:Integer.parseInt(minUsernum));
                    map.put("maxUsernum",!StringUtils.isNumeric(maxUsernum)?null:Integer.parseInt(maxUsernum));
                }else if("callnum".equals(name)){
                    String minCallnum = queryParam.getString("minValue");
                    String maxCallnum = queryParam.getString("maxValue");
                    map.put("minCallnum",!StringUtils.isNumeric(minCallnum)?null:Long.parseLong(minCallnum));
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

    @RequestMapping("/getDataDefine")
    public Map<String, Object> getDataDefine(String searchtxt) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.getDataDefine("".equals(searchtxt) ? null : searchtxt));
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @RequestMapping("/getTopData")
    public Map<String, Object> getTopData(HttpServletRequest request) {
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        //信息中心账户看全部

        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {
            ret.put("data", dataService.getTopData(tokenDepartment));
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @RequestMapping("/saveTopData")
    public Map<String, Object> saveTopData(HttpServletRequest request,String[] datacodes) {
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        String token = request.getHeader("token");
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(token);
        String tokenDepartment = sysSession.getDepartment();
        try {
            dataService.deleteTopData(tokenDepartment);
            dataService.saveTopData(datacodes, tokenDepartment);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/problemTotal")
    public Map<String, Object> problemTotal(HttpServletRequest request, String usertype,String department, String type, String startTime, String endTime,String is_check,
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

            //List<Map<String, Object>> problemTotal = dataService.getProblemTotal("".equals(usertype) ? null : usertype, "".equals(department) ? null : department, "".equals(is_check) ? null : is_check, startTime1, endTime1, type);
            List<Map<String, Object>> problemTotal = monitorCheckUserService.getProblemTotal();

            ret.put("data",problemTotal);
        } catch (Exception e) {
            logger.error("", e);

            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }

    @GetMapping("/problemUser")
    public Map<String, Object> problemUser(HttpServletRequest request, String usertype,String department, String type, String startTime, String endTime,String is_check,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderName", required = false, defaultValue = "user_id") String orderName,
                                         @RequestParam(value = "orderType", required = false, defaultValue = "desc") String orderType) {

        String startTime1 = startTime.replaceAll("-","");
        String endTime1 = endTime.replaceAll("-","");
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");
        try {

            //List<Map<String, Object>> maps = monitorCheckUserService.getList2("".equals(department)?null:department,"1", "".equals(type)?null:type,pageNum ,pageSize);
            List<Map<String, Object>> result = monitorCheckUserService.getList2("".equals(department)?null:department,"1", "".equals(type)?null:type, pageNum, pageSize);

            int totoal = result.size();
            int startRow = (pageNum-1)*pageSize;
            int endRow = (pageNum)*pageSize;
            if(endRow>totoal){
                endRow = totoal;
            }
            int pages;
            if(result.size()%pageSize==0){
                pages = result.size()/pageSize;
            }else{
                pages = result.size()/pageSize+1;
            }
            Map pageMap = new TreeMap();
            pageMap.put("total",totoal);
            pageMap.put("pageNum",pageNum);
            pageMap.put("pageSize",pageSize);
            pageMap.put("pages",pages);
            pageMap.put("startRow",startRow);
            pageMap.put("endRow",endRow);
            pageMap.put("orderBy",orderName+" "+orderType);

            List<Map<String, Object>> maps = result.subList(startRow, endRow);
            
            if(maps!=null&&maps.size()>0){
                for(int i = 0;i<maps.size();i++){
                    maps.get(i).put("ROWNO",startRow+1+i);
                }
            }

            ret.put("data",maps);
            ret.put("pageinfo",pageMap);
        } catch (Exception e) {
            logger.error("", e);
            ret.put("status", 1);
            ret.put("msg", e.toString());
        }
        return ret;
    }


}
