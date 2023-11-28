package com.piesat.statistics.service;

import com.piesat.statistics.bean.DataBean;
import com.piesat.statistics.bean.DataClassBean;
import com.piesat.statistics.bean.UserInfo;
import com.piesat.statistics.mapper.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataService {

    @Resource
    DataMapper dataMapper;

    @Autowired
    UserService userService;

    public Map<String, DataClassBean> getAllDataClass() {
        List<DataClassBean> list = dataMapper.getAllDataClass();

        Map<String, DataClassBean> ret = new HashMap<>();

        for (DataClassBean bean : list) {
            ret.put(bean.getData_class_id(), bean);
        }

        return ret;
    }

    public Map<String, DataBean> getAllPublishedData() {
        Map<String, DataClassBean> classBeanMap = getAllDataClass();

        List<DataBean> list = dataMapper.getAllPublishedData();

        Map<String, DataBean> ret = new HashMap<>();

        for (DataBean bean : list) {
            String className = "";
            if (classBeanMap.get(bean.getData_class_id()) != null) {
                className = classBeanMap.get(bean.getData_class_id()).getData_class_name();
            }

            bean.setData_class_name(className);

            ret.put(bean.getData_code(), bean);
        }

        return ret;
    }

    public Map<String, DataBean> getAllData() {
        Map<String, DataClassBean> classBeanMap = getAllDataClass();

        List<DataBean> list = dataMapper.getAllData();

        Map<String, DataBean> ret = new HashMap<>();

        for (DataBean bean : list) {
            String className = "";
            if (classBeanMap.get(bean.getData_class_id()) != null) {
                className = classBeanMap.get(bean.getData_class_id()).getData_class_name();
            }

            bean.setData_class_name(className);

            ret.put(bean.getData_code(), bean);
        }

        return ret;
    }

    public List<String> getAccessData(String startTime, String endTime) {
        return dataMapper.getAccessData(startTime, endTime);
    }

    public Map<String, Object> getAccessByDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = dataMapper.getAccessByDepartment(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("DEPARTMENT").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public Long getTotalVolume(String startTime, String endTime) {
        return dataMapper.getTotalVolume(startTime, endTime);
    }

    public Long getTotalVolumeByGroupId(String groupId, String startTime, String endTime) {
        return dataMapper.getTotalVolumeByGroupId(groupId, startTime, endTime);
    }

    public Map<String, Object> getVolumeByGroupId(String startTime, String endTime) {
        List<Map<String, Object>> list = dataMapper.getVolumeByGroupId(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("TYPE").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public Map<String, Map<String, Object>> getVolumeByDatetimeAndUserType(String startTime, String endTime) {
        Map<String, Map<String, Object>> ret = new HashMap<>();

        List<Map<String, Object>> list = dataMapper.getVolumeByDatetimeAndUserType(startTime, endTime);

        for (Map<String, Object> entry : list) {
            String datetime = entry.get("DATATIME").toString();
            String type = entry.get("TYPE").toString();
            Object count = entry.get("TOTAL");

            if (ret.get(datetime) == null) {
                ret.put(datetime, new HashMap<>());
            }

            Map<String, Object> departmentObj = ret.get(datetime);

            departmentObj.put(type, count);
        }

        return ret;
    }

    public Map<String, Map<String, Object>> getDrillVolumeByDatetimeAndUserType(String startTime, String endTime) {
        Map<String, Map<String, Object>> ret = new HashMap<>();

        List<Map<String, Object>> list = dataMapper.getDrillVolumeByDatetimeAndUserType(startTime, endTime);

        for (Map<String, Object> entry : list) {
            String datetime = entry.get("DATATIME").toString();
            String type = entry.get("TYPE").toString();
            Object count = entry.get("TOTAL");

            if (ret.get(datetime) == null) {
                ret.put(datetime, new HashMap<>());
            }

            Map<String, Object> departmentObj = ret.get(datetime);

            departmentObj.put(type, count);
        }

        return ret;
    }

    public Map<String, Object> getVolumeByDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = dataMapper.getVolumeByDepartment(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("DEPARTMENT").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public List<Map<String, Object>> getVolumeByUserType(String startTime, String endTime, String userType, int limit) {
        Map<String, UserInfo> userInfoMap = userService.getAllUserInfo();

        List<Map<String, Object>> list = dataMapper.getVolumeByUserType(startTime, endTime, userType, limit);

        for (Map<String, Object> entity : list) {
            UserInfo ui = userInfoMap.get(entity.get("USERID"));
            List userid = userService.getUserById((String) entity.get("USERID"));
            Map map = (Map) userid.get(0);
            Object department_name = map.get("DEPARTMENT_NAME");

            if (ui != null) {
                entity.put("USERNAME", ui.getUser_name());
                entity.put("SYSTEM", ui.getSystem());
                entity.put("SYSTEM_DES", ui.getSystem_des());
                entity.put("DEPARTMENT", ui.getDepartment());
                entity.put("DEPARTMENT_NAME", department_name);
            }
        }

        return list;
    }

    public List<Map<String, Object>> getVolumeByDataClassId(int country, String startTime, String endTime) {
        Map<String, DataClassBean> classMap = getAllDataClass();

        List<Map<String, Object>> list = dataMapper.getVolumeByDataClassId(country, startTime, endTime);

        for (Map<String, Object> entry : list) {
            if (entry.get("DATACLASSID") != null) {
                String classId = entry.get("DATACLASSID").toString();

                if (classMap.get(classId) != null) {
                    entry.put("DATACLASSNAME", classMap.get(classId).getData_class_name());
                }
            }
        }

        return list;
    }

    public List<Map<String, Object>> getVolumeFilterCountryByGroupId(int country, String startTime, String endTime) {
        return dataMapper.getVolumeFilterCountryByGroupId(country, startTime, endTime);
    }

    public Map<String, Map<String, Object>> getVolumeByDataIdAndDepartment(int country, String startTime, String endTime) {
        List<Map<String, Object>> list  = dataMapper.getVolumeByDataIdAndDepartment(country, startTime, endTime);

        Map<String, Map<String, Object>> ret = new HashMap<>();

        for (Map<String, Object> entry : list) {
            if (entry.get("DATACODE") != null) {
                String datacode = entry.get("DATACODE").toString();

                if (ret.get(datacode) == null) {
                    ret.put(datacode, new HashMap<String, Object>());
                }

                Map<String, Object> obj = ret.get(datacode);

                if (entry.get("DEPARTMENT") != null) {
                    obj.put(entry.get("DEPARTMENT").toString(), entry.get("TOTAL"));
                }
            }
        }

        return ret;
    }

    public Map<String, Map<String, Map<String, Object>>> getVolumeByUserTypeGroupByDataIdAndDepartment(int country, String startTime, String endTime) {
        List<Map<String, Object>> list  = dataMapper.getVolumeByUserTypeGroupByDataIdAndDepartment(country, startTime, endTime);

        Map<String, Map<String, Map<String, Object>>> ret = new HashMap<>();

        for (Map<String, Object> entry : list) {
            if (entry.get("GROUPID") != null) {
                String groupId = entry.get("GROUPID").toString();

                if (ret.get(groupId) == null) {
                    ret.put(groupId, new HashMap<String, Map<String, Object>>());
                }

                Map<String, Map<String, Object>> groupIdObj = ret.get(groupId);

                if (entry.get("DATACODE") != null) {
                    String datacode = entry.get("DATACODE").toString();

                    if (groupIdObj.get(datacode) == null) {
                        groupIdObj.put(datacode, new HashMap<String, Object>());
                    }

                    Map<String, Object> obj = groupIdObj.get(datacode);

                    if (entry.get("DEPARTMENT") != null) {
                        obj.put(entry.get("DEPARTMENT").toString(), entry.get("TOTAL"));
                    }
                }
            }
        }

        return ret;
    }

    public Map dataClassCount(String startTime, String endTime, String tokenDepartment) {
        List<Map<String, Object>> list =  dataMapper.dataClassCount(startTime, endTime, tokenDepartment);
        Integer total = dataMapper.dataCalssTotalCount(startTime, endTime, tokenDepartment);

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String data_class_id = entry.get("DATA_CLASS_ID").toString();
            String clssnum = entry.get("CLSSNUM").toString();;
            String data_class_name = entry.get("DATA_CLASS_NAME").toString();;
            Map<String, Object> obj = new HashMap<>();
            obj.put("data_class_id", data_class_id);
            obj.put("data_class_name", data_class_name);
            obj.put("clssnum", clssnum);
            resultList.add(obj);

        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }


    public List<Map<String, Object>> dataDetailList(String data_class_id,String startTime, String endTime,String searchtxt, String tokenDepartment) {
        return dataMapper.dataDetailList(data_class_id,startTime, endTime,searchtxt, tokenDepartment);
    }

    public List dataClassTotal(String data_class_id,String startTime, String endTime,String searchtxt, String tokenDepartment) {
        return dataMapper.dataClassTotal(data_class_id,startTime, endTime,searchtxt, tokenDepartment);
    }

    public List dataDetailListByLimit(String data_class_id,String startTime, String endTime,Integer limit) {
        List<Map<String, Object>> list =  dataMapper.dataDetailListByLimit(data_class_id,startTime, endTime,limit);
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String data_name = entry.get("DATA_NAME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("data_name", data_name);
            obj.put("callnum", callnum);
            obj.put("download", download);
            resultList.add(obj);
        }
        return resultList;
    }

    public Map dataCategoryCountGroupByDepartment(String is_country, String startTime, String endTime, String tokenDepartment,String type) {
        if("1".equals(type)){
            List<Map<String, Object>> list =  dataMapper.basicDataCategoryCountGroupByDepartment(is_country, startTime, endTime, tokenDepartment);
            List<Map<String, Object>> list2 =  dataMapper.basicDataCategoryCountGroupByDepartment(is_country, startTime, endTime, null);
            Integer total = 0;
            Map map = new HashMap();
            List<Map<String, Object>> resultList = new ArrayList();
            for (Map<String, Object> entry : list) {
                String department = entry.get("DEPARTMENT").toString();
                String department_name = entry.get("DEPARTMENT_NAME").toString().replaceAll("省","").replaceAll("市","").replaceAll("区","");
                String is_country_result = entry.get("IS_COUNTRY").toString();;
                String departnum = entry.get("DEPARTNUM").toString();;
                Map<String, Object> obj = new HashMap<>();
                obj.put("department", department);
                obj.put("department_name", department_name);
                obj.put("is_country", is_country_result);
                obj.put("departnum", departnum);
                //total += Integer.parseInt(departnum);
                resultList.add(obj);

            }
            for (Map<String, Object> entry : list2) {
                String departnum = entry.get("DEPARTNUM").toString();;
                total += Integer.parseInt(departnum);
            }
            map.put("list",resultList);
            map.put("total",total);
            return map;
        }else if("4".equals(type)){
            List<Map<String, Object>> list =  dataMapper.dataCategoryCountGroupByDepartmentNotActive(is_country, startTime, endTime, tokenDepartment);
            List<Map<String, Object>> list2 =  dataMapper.dataCategoryCountGroupByDepartmentNotActive(is_country, startTime, endTime, null);
            Integer total = 0;
            Map map = new HashMap();
            List<Map<String, Object>> resultList = new ArrayList();

            for (Map<String, Object> entry : list) {
                String department = entry.get("DEPARTMENT").toString();
                String department_name = entry.get("DEPARTMENT_NAME").toString().replaceAll("省","").replaceAll("市","").replaceAll("区","");
                String is_country_result = entry.get("IS_COUNTRY").toString();;
                String departnum = entry.get("DEPARTNUM").toString();;
                Map<String, Object> obj = new HashMap<>();
                obj.put("department", department);
                obj.put("department_name", department_name);
                obj.put("is_country", is_country_result);
                obj.put("departnum", departnum);
                //total += Integer.parseInt(departnum);
                resultList.add(obj);

            }
            for (Map<String, Object> entry : list2) {
                String departnum = entry.get("DEPARTNUM").toString();;
                total += Integer.parseInt(departnum);
            }
            map.put("list",resultList);
            map.put("total",total);
            return map;
        }else{
            List<Map<String, Object>> list =  dataMapper.dataCategoryCountGroupByDepartment(is_country, startTime, endTime, tokenDepartment);
            List<Map<String, Object>> list2 =  dataMapper.dataCategoryCountGroupByDepartment(is_country, startTime, endTime, null);
            Integer total = 0;
            Map map = new HashMap();
            List<Map<String, Object>> resultList = new ArrayList();

            for (Map<String, Object> entry : list) {
                String department = entry.get("DEPARTMENT").toString();
                String department_name = entry.get("DEPARTMENT_NAME").toString().replaceAll("省","").replaceAll("市","").replaceAll("区","");
                String is_country_result = entry.get("IS_COUNTRY").toString();;
                String departnum = entry.get("DEPARTNUM").toString();;
                Map<String, Object> obj = new HashMap<>();
                obj.put("department", department);
                obj.put("department_name", department_name);
                obj.put("is_country", is_country_result);
                obj.put("departnum", departnum);
                //total += Integer.parseInt(departnum);
                resultList.add(obj);

            }
            for (Map<String, Object> entry : list2) {
                String departnum = entry.get("DEPARTNUM").toString();;
                total += Integer.parseInt(departnum);
            }
            map.put("list",resultList);
            map.put("total",total);
            return map;
        }


    }

    public Map dataCategoryCountByDepartment(String department,String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataCategoryCountByDepartment(department, startTime, endTime);
        Integer totalCount = dataMapper.dataCategoryTotalCountByDepartment(department, startTime, endTime);

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;

            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            resultList.add(obj);

        }
        map.put("list",list);
        map.put("totalCount",totalCount);
        return map;
    }

    public List dataCategoryCountGroupByUserType(String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataCategoryCountGroupByUserType(startTime, endTime);
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String groupnum = entry.get("GROUPNUM").toString();;
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("groupnum", groupnum);

            resultList.add(obj);

        }
        return resultList;
    }

    public Map dataCategoryCountByUserType(String usertype, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataCategoryCountByUserType(usertype,startTime, endTime);
        Integer totalCount =  dataMapper.dataCategoryTotalCountByUserType(usertype,startTime, endTime);
        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();;
            String num = entry.get("NUM").toString();;

            Map<String, Object> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("num", num);

            resultList.add(obj);

        }
        map.put("list",list);
        map.put("totalCount",totalCount);
        return map;
    }

    public List dataCountByDepartmentAndUserType(String department, String usertype, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataCountByDepartmentAndUserType(department, usertype,startTime, endTime);
        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String data_name = entry.get("DATA_NAME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            String usernum = entry.get("USERNUM").toString();
            String data_class_id = entry.get("DATA_CLASS_ID").toString();
            String api_data_code = entry.get("API_DATA_CODE").toString();

            Map<String, Object> obj = new HashMap<>();
            obj.put("data_name", data_name);
            obj.put("callnum", callnum);
            obj.put("download", download);
            obj.put("usernum", usernum);
            obj.put("data_class_id", data_class_id);
            obj.put("api_data_code", api_data_code);

            resultList.add(obj);

        }
        return resultList;
    }


    public Map dataCountGroupByDataclass(String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataCountGroupByDataclass(startTime, endTime);
        long total = 0l;
        //Long total = dataMapper.totalDataSize(startTime, endTime);
        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String data_class_id = entry.get("DATA_CLASS_ID").toString();
            String download = entry.get("DOWNLOAD").toString();

            Map<String, Object> obj = new HashMap<>();
            obj.put("data_class_id", data_class_id);
            obj.put("download", download);

            total +=Long.parseLong(download);

            resultList.add(obj);

        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }

    public List accessCategoryTop5(String startTime, String endTime, String tokenDepartment) {
        List<Map<String, Object>> list =  dataMapper.accessCategoryTop5(startTime, endTime, tokenDepartment);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String sys_user_id = entry.get("SYS_USER_ID").toString();
            String datacodenumber = entry.get("DATACODENUMBER").toString();
            String system = entry.get("SYSTEM").toString();
            String user_name = entry.get("USER_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("sys_user_id", sys_user_id);
            obj.put("datacodenumber", datacodenumber);
            obj.put("system", system);
            obj.put("user_name", user_name);

            List<Map<String, Object>> maps = dataMapper.departmentByuserId(sys_user_id);
            String department = maps.get(0).get("DEPARTMENT").toString();
            String department_name = maps.get(0).get("DEPARTMENT_NAME").toString();
            obj.put("department", department);
            obj.put("department_name", department_name);

            String group_id = maps.get(0).get("GROUP_ID").toString();
            obj.put("group_id", group_id);
            resultList.add(obj);

        }
        return resultList;
    }

    public Set<String> accessCategoryTop50(String usertype,String department,String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.accessCategoryTop50(usertype, department,startTime, endTime);
        Set<String> userids = new HashSet<>();
        if(list!=null&&list.size()>0){
            for (Map<String, Object> entry : list) {
                String sys_user_id = entry.get("SYS_USER_ID").toString();
                userids.add(sys_user_id);
            }
        }
        return userids;
    }

    public List<Map<String, Object>> getTop50Data(Set<String> userids, String startTime, String endTime){
        List<Map<String, Object>> maps = dataMapper.top50Userstat(userids,startTime,endTime);
        return maps;
    }

    public List<Map<String, Object>> getTop50Data(String usertype,String department,String is_check,String startTime, String endTime, String type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        Date parse1 = null;
        Date parse2 = null;
        try {
            parse1 = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-1);
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;
        startTime1 = startTime;
        endTime1 = endTime;
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime());
        List<Map<String, Object>> maps = dataMapper.top50Userstat2(usertype, department, is_check, startTime1, endTime1,startTime2, endTime2);

        String judgeField = "";
        Map<String, Long> totalUserAndDataAndVolume = null;
        Long categoryAvg = null;
        Long downloadAvg = null;
        Long callnumAvg = null;

        Double callnumaddpercentvg = null;
        Double downloadaddpercentAvg = null;
        Double categorynumaddpercentAvg = null;
        if("category".equals(type)||"callnum".equals(type)||"download".equals(type)){
            totalUserAndDataAndVolume = getTotalUserAndDataAndVolume(startTime, endTime);
            categoryAvg = totalUserAndDataAndVolume.get("dataAvg");
            downloadAvg = totalUserAndDataAndVolume.get("volumeAvg");
            callnumAvg = totalUserAndDataAndVolume.get("callnumAvg");
        }else if("callnumadd".equals(type)||"downloadadd".equals(type)||"categoryadd".equals(type)){
            Map<String, Double> totalAddPercent = getTotalAddPercent(startTime1, endTime1,startTime2, endTime2);
            callnumaddpercentvg = totalAddPercent.get("callnumaddpercentvg");
            downloadaddpercentAvg = totalAddPercent.get("downloadaddpercentAvg");
            categorynumaddpercentAvg = totalAddPercent.get("categorynumaddpercentAvg");
        }
        for (Map<String, Object> entry : maps) {
            if("category".equals(type)){
                String precallnum = entry.get("CATEGORYNUM").toString();
                if(Long.parseLong(precallnum)>categoryAvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }else if("callnum".equals(type)){
                String callnum = entry.get("CALLNUM").toString();
                if(Long.parseLong(callnum)>callnumAvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }else if("download".equals(type)){
                String download = entry.get("DOWNLOAD").toString();
                if(Long.parseLong(download)>downloadAvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }else if("callnumadd".equals(type)){
                String callnumaddpercent = entry.get("CALLNUMADDPERCENT").toString();
                if(Double.parseDouble(callnumaddpercent)>callnumaddpercentvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }else if("downloadadd".equals(type)){
                String downloadaddpercent = entry.get("DOWNLOADADDPERCENT").toString();
                if(Double.parseDouble(downloadaddpercent)>downloadaddpercentAvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }else if("categoryadd".equals(type)){
                String categorynumaddpercent = entry.get("CATEGORYNUMADDPERCENT").toString();
                if(Double.parseDouble(categorynumaddpercent)>categorynumaddpercentAvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }
        }
        return maps;
    }

    /**
     * 夜间访问量
     * @param usertype
     * @param department
     * @param is_check
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    public List<Map<String, Object>> getTop50DownloadData(String usertype,String department,String is_check,String startTime, String endTime, String type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        Calendar cal1 = Calendar.getInstance();
        Date parse1 = null;
        Date parse2 = null;
        try {
            parse1 = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-1);
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;
        startTime1 = startTime;
        endTime1 = endTime;
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime());
        List<Map<String, Object>> maps = dataMapper.top50UserDownloadstat(usertype, department, is_check, startTime1, endTime1,startTime2, endTime2);

        Map<String, Long> totalUserAndDataAndVolume = null;
        Long categoryAvg = null;
        Long downloadAvg = null;
        Long callnumAvg = null;

        Double callnumaddpercentvg = null;
        Double downloadaddpercentAvg = null;
        Double categorynumaddpercentAvg = null;
        if("download".equals(type)){
            //totalUserAndDataAndVolume = getTotalUserAndDataAndVolume(startTime, endTime);
            totalUserAndDataAndVolume = getYJDownloadAvg(startTime, endTime);
            categoryAvg = totalUserAndDataAndVolume.get("dataAvg");
            downloadAvg = totalUserAndDataAndVolume.get("volumeAvg");
            callnumAvg = totalUserAndDataAndVolume.get("callnumAvg");
        }/*else if("downloadadd".equals(type)){
            Map<String, Double> totalAddPercent = getTotalAddPercent(startTime1, endTime1,startTime2, endTime2);
            callnumaddpercentvg = totalAddPercent.get("callnumaddpercentvg");
            downloadaddpercentAvg = totalAddPercent.get("downloadaddpercentAvg");
            categorynumaddpercentAvg = totalAddPercent.get("categorynumaddpercentAvg");
        }*/
        for (Map<String, Object> entry : maps) {
            if("download".equals(type)){
                String download = entry.get("DOWNLOAD").toString();
                if(Long.parseLong(download)>downloadAvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }/*else if("downloadadd".equals(type)){
                String downloadaddpercent = entry.get("DOWNLOADADDPERCENT").toString();
                if(Double.parseDouble(downloadaddpercent)>downloadaddpercentAvg){
                    entry.put("CHECKFLAG","true");
                }else{
                    entry.put("CHECKFLAG","false");
                }
            }*/
        }
        return maps;
    }

    public double getPercent(long nowValue,long preValue){
        double callnumaddprecent = 0;
        if(preValue==0&&nowValue!=0){
            callnumaddprecent = 100;
        }else if(preValue!=0&&nowValue==0){
            callnumaddprecent = -100;
        }else if(preValue==0&&nowValue==0){
            callnumaddprecent = 0;
        }else{
            double tempprecent = (nowValue-preValue)/preValue*100;
            BigDecimal   b   =   new BigDecimal(tempprecent);
            callnumaddprecent   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return callnumaddprecent;
    }

    public List accessNumTop5(String startTime, String endTime, String tokenDepartment) {
        List<Map<String, Object>> list =  dataMapper.accessNumTop5(startTime, endTime,tokenDepartment);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String sys_user_id = entry.get("SYS_USER_ID").toString();
            String callnum = entry.get("CALLNUM").toString();
            String system = entry.get("SYSTEM").toString();
            String user_name = entry.get("USER_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("sys_user_id", sys_user_id);
            obj.put("callnum", callnum);
            obj.put("system", system);
            obj.put("user_name", user_name);
            List<Map<String, Object>> maps = dataMapper.departmentByuserId(sys_user_id);
            String department = maps.get(0).get("DEPARTMENT").toString();
            String department_name = maps.get(0).get("DEPARTMENT_NAME").toString();
            obj.put("department", department);
            obj.put("department_name", department_name);
            String group_id = maps.get(0).get("GROUP_ID").toString();
            obj.put("group_id", group_id);
            resultList.add(obj);
        }
        return resultList;
    }

    public Set<String> accessNumTop50(String usertype,String department,String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.accessNumTop50(usertype, department, startTime, endTime);
        Set<String> userids = new HashSet<>();
        if(list!=null&&list.size()>0){
            for (Map<String, Object> entry : list) {
                String sys_user_id = entry.get("SYS_USER_ID").toString();
                userids.add(sys_user_id);
            }
        }
        return userids;
    }

    public List downloadTop5(String startTime, String endTime, String tokenDepartment) {
        List<Map<String, Object>> list =  dataMapper.downloadTop5(startTime, endTime,tokenDepartment);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String sys_user_id = entry.get("SYS_USER_ID").toString();
            String download = entry.get("DOWNLOAD").toString();
            String system = entry.get("SYSTEM").toString();
            String user_name = entry.get("USER_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("sys_user_id", sys_user_id);
            obj.put("download", download);
            obj.put("system", system);
            obj.put("user_name", user_name);
            List<Map<String, Object>> maps = dataMapper.departmentByuserId(sys_user_id);
            String department = maps.get(0).get("DEPARTMENT").toString();
            String department_name = maps.get(0).get("DEPARTMENT_NAME").toString();
            obj.put("department", department);
            obj.put("department_name", department_name);
            String group_id = maps.get(0).get("GROUP_ID").toString();
            obj.put("group_id", group_id);
            resultList.add(obj);
        }
        return resultList;
    }

    public Set<String> downloadTop50(String usertype,String department,String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.downloadTop50(usertype, department, startTime, endTime);
        Set<String> userids = new HashSet<>();
        if(list!=null&&list.size()>0){
            for (Map<String, Object> entry : list) {
                String sys_user_id = entry.get("SYS_USER_ID").toString();
                userids.add(sys_user_id);
            }
        }
        return userids;
    }

    public List accessNumAddTop5(String startTime, String endTime, String tokenDepartment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        Date parse1 = null;
        Date parse2 = null;
        try {
            parse1 = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;
        startTime1 = startTime;
        endTime1 = endTime;
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime());

        List<Map<String, Object>> list =  dataMapper.accessNumAddTop5(startTime1, endTime1,startTime2, endTime2, tokenDepartment);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String sys_user_id = entry.get("SYS_USER_ID").toString();
            String addquerydata = entry.get("ADDQUERYDATA").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("sys_user_id", sys_user_id);
            obj.put("addquerydata", addquerydata);

            List<Map<String, Object>> maps = dataMapper.departmentByuserId(sys_user_id);
            String department = maps.get(0).get("DEPARTMENT").toString();
            String department_name = maps.get(0).get("DEPARTMENT_NAME").toString();
            obj.put("department", department);
            obj.put("department_name", department_name);

            List<Map<String, Object>> maps1 = dataMapper.queryUserByUserId(sys_user_id);
            String system = maps1.get(0).get("SYSTEM").toString();
            String user_name = maps1.get(0).get("USER_NAME").toString();
            obj.put("system", system);
            obj.put("user_name", user_name);
            String group_id = maps.get(0).get("GROUP_ID").toString();
            obj.put("group_id", group_id);
            resultList.add(obj);
        }
        return resultList;
    }

    public Set accessNumAddTop50(String usertype,String department, String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        Date parse1 = null;
        Date parse2 = null;
        try {
            parse1 = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;
        startTime1 = startTime;
        endTime1 = endTime;
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime());

        List<Map<String, Object>> list =  dataMapper.accessNumAddTop50(usertype, department, startTime1, endTime1,startTime2, endTime2);
        Set<String> userids = new HashSet<>();
        if(list!=null&&list.size()>0){
            for (Map<String, Object> entry : list) {
                String sys_user_id = entry.get("SYS_USER_ID").toString();
                userids.add(sys_user_id);
            }
        }
        return userids;
    }

    public List downlaodAddTop5(String startTime, String endTime, String tokenDepartment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        Date parse1 = null;
        Date parse2 = null;
        try {
            parse1 = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;
        startTime1 = startTime;
        endTime1 = endTime;
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime());

        List<Map<String, Object>> list =  dataMapper.downlaodAddTop5(startTime1, endTime1,startTime2, endTime2,tokenDepartment);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String sys_user_id = entry.get("SYS_USER_ID").toString();
            String adddowndata = entry.get("ADDDOWNDATA").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("sys_user_id", sys_user_id);
            obj.put("adddowndata", adddowndata);
            List<Map<String, Object>> maps = dataMapper.departmentByuserId(sys_user_id);
            String department = maps.get(0).get("DEPARTMENT").toString();
            String department_name = maps.get(0).get("DEPARTMENT_NAME").toString();
            obj.put("department", department);
            obj.put("department_name", department_name);

            List<Map<String, Object>> maps1 = dataMapper.queryUserByUserId(sys_user_id);
            String system = maps1.get(0).get("SYSTEM").toString();
            String user_name = maps1.get(0).get("USER_NAME").toString();
            obj.put("system", system);
            obj.put("user_name", user_name);
            String group_id = maps.get(0).get("GROUP_ID").toString();
            obj.put("group_id", group_id);
            resultList.add(obj);
        }
        return resultList;
    }

    public Set<String> downlaodAddTop50(String usertype,String department, String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        Date parse1 = null;
        Date parse2 = null;
        try {
            parse1 = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;
        startTime1 = startTime;
        endTime1 = endTime;
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime());

        List<Map<String, Object>> list =  dataMapper.downlaodAddTop50(usertype, department, startTime1, endTime1,startTime2, endTime2);
        Set<String> userids = new HashSet<>();
        if(list!=null&&list.size()>0){
            for (Map<String, Object> entry : list) {
                String sys_user_id = entry.get("SYS_USER_ID").toString();
                userids.add(sys_user_id);
            }
        }
        return userids;
    }

    public List dataCountByDatacode(String datacode,Integer limit, String startTime, String endTime) {
        List<Map<String, Object>> list;
        if(limit!=null&&!"".equals(limit)){
            list =  dataMapper.dataCountByDatacodeAndLimit(datacode,limit,startTime, endTime);
        }else{
            list =  dataMapper.dataCountByDatacode(datacode,startTime, endTime);
        }

        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String sys_user_id = entry.get("SYS_USER_ID").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            String user_name = entry.get("USER_NAME").toString();
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("sys_user_id", sys_user_id);
            obj.put("callnum", callnum);
            obj.put("download", download);
            obj.put("user_name", user_name);
            obj.put("department", department);
            obj.put("department_name", department_name);
            resultList.add(obj);
        }
        return resultList;
    }

    public Map dataTop5(String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataTop5(startTime, endTime);
        //Long total = dataMapper.dataTotalTop5(startTime, endTime);
        Long total = 0l;

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String api_data_code = entry.get("API_DATA_CODE").toString();
            String download = entry.get("DOWNLOAD").toString();
            String callnum = entry.get("CALLNUM").toString();
            String data_name = entry.get("DATA_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("api_data_code", api_data_code);
            obj.put("download", download);
            obj.put("callnum", callnum);
            obj.put("data_name", data_name);
            resultList.add(obj);
            total  += Long.parseLong(download);
        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }

    public Map dataTop5InDepartment(String startTime, String endTime, String tokenDepartment) {
        String[] datacodes = new String[]{"SURF_CHN_MUL_MIN","SURF_CHN_MUL_HOR","RADA_L2_FMT","RADA_L2_ELEV_ST","OCEN_CHN_BUOY"};
        String[] datacodeNames = new String[]{"中国地面分钟数据","中国地面逐小时数据","天气雷达全体扫标准格式基数据","天气雷达逐仰角标准格式基数据","中国浮标观测数据"};

        List<Map<String, Object>> list =  dataMapper.dataTop5Indepartment(startTime, endTime, tokenDepartment);
        Long total = 0l;

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        Map<String,Map<String, Object>> resultMap = new HashMap();
        for (Map<String, Object> entry : list) {
            String api_data_code = entry.get("API_DATA_CODE").toString();
            String download = entry.get("DOWNLOAD").toString();
            String callnum = entry.get("CALLNUM").toString();
            String data_name = entry.get("DATA_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("api_data_code", api_data_code);
            obj.put("download", download);
            obj.put("callnum", callnum);
            obj.put("data_name", data_name);
            total  += Long.parseLong(download);
            resultMap.put(api_data_code,obj);
        }

        for(int i =0;i<datacodes.length;i++){
            String datacode = datacodes[i];
            if(resultMap.containsKey(datacode)){
                Map<String, Object> obj = resultMap.get(datacode);
                resultList.add(obj);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("api_data_code", datacode);
                obj.put("download", 0);
                obj.put("callnum", 0);
                obj.put("data_name", datacodeNames[i]);
                resultList.add(obj);
            }
        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }

    public Map dataTop(String startTime, String endTime,String department) {
        List<Map<String, Object>> topData =  dataMapper.getTopData(department);

        List<Map<String, Object>> list =  dataMapper.dataTopIndepartment(startTime, endTime, department);
        Long total = 0l;

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        Map<String,Map<String, Object>> resultMap = new HashMap();
        for (Map<String, Object> entry : list) {
            String api_data_code = entry.get("API_DATA_CODE").toString();
            String download = entry.get("DOWNLOAD").toString();
            String callnum = entry.get("CALLNUM").toString();
            String data_name = entry.get("DATA_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("api_data_code", api_data_code);
            obj.put("download", download);
            obj.put("callnum", callnum);
            obj.put("data_name", data_name);
            total  += Long.parseLong(download);
            //resultList.add(obj);
            resultMap.put(api_data_code,obj);
        }

        for(int i =0;i<topData.size();i++){
            String datacode = topData.get(i).get("DATA_CODE").toString();
            String dataname = topData.get(i).get("DATA_NAME").toString();
            if(resultMap.containsKey(datacode)){
                Map<String, Object> obj = resultMap.get(datacode);
                resultList.add(obj);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("api_data_code", datacode);
                obj.put("download", 0);
                obj.put("callnum", 0);
                obj.put("data_name", dataname);
                resultList.add(obj);
            }
        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }

    public Map dataTopInDepartment(String startTime, String endTime, String tokenDepartment) {
        List<Map<String, Object>> topData =  dataMapper.getTopData(tokenDepartment);

        List<Map<String, Object>> list =  dataMapper.dataTopIndepartment(startTime, endTime, tokenDepartment);
        Long total = 0l;

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        Map<String,Map<String, Object>> resultMap = new HashMap();
        for (Map<String, Object> entry : list) {
            String api_data_code = entry.get("API_DATA_CODE").toString();
            String download = entry.get("DOWNLOAD").toString();
            String callnum = entry.get("CALLNUM").toString();
            String data_name = entry.get("DATA_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("api_data_code", api_data_code);
            obj.put("download", download);
            obj.put("callnum", callnum);
            obj.put("data_name", data_name);
            total  += Long.parseLong(download);
            //resultList.add(obj);
            resultMap.put(api_data_code,obj);
        }

        for(int i =0;i<topData.size();i++){
            String datacode = topData.get(i).get("DATA_CODE").toString();
            String dataname = topData.get(i).get("DATA_NAME").toString();
            if(resultMap.containsKey(datacode)){
                Map<String, Object> obj = resultMap.get(datacode);
                resultList.add(obj);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("api_data_code", datacode);
                obj.put("download", 0);
                obj.put("callnum", 0);
                obj.put("data_name", dataname);
                resultList.add(obj);
            }
        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }


    public Map dataStatByDatacode(String datacode, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataStatByDatacode(datacode,startTime, endTime);
        Long total = dataMapper.dataTotalTop5(startTime, endTime);

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String data_name = entry.get("DATA_NAME").toString();
            String d_datetime = entry.get("D_DATETIME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("data_name", data_name);
            obj.put("d_datetime", d_datetime);
            obj.put("callnum", callnum);
            obj.put("download", download);
            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }

    public Map dataStatByDatacodeByHor(String datacode, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataStatByDatacodeByHor(datacode,startTime, endTime);
        Long total = dataMapper.dataTotalTop5ByHor(startTime, endTime);

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String data_name = entry.get("DATA_NAME").toString();
            String d_datetime = entry.get("D_DATETIME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("data_name", data_name);
            obj.put("d_datetime", d_datetime);
            obj.put("callnum", callnum);
            obj.put("download", download);
            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }

    public Map dataStatByDatacodeGroupByDepartment(String datacode, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataStatByDatacodeGroupByDepartment(datacode,startTime, endTime);
        Long total = dataMapper.dataStatByDatacodeTotalGroupByDepartment(datacode, startTime, endTime);

        Map map = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("callnum", callnum);
            obj.put("download", download);
            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("total",total);
        return map;
    }

    public List dataStatByDatacodeAndDepartment(String datacode,String department, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataStatByDatacodeAndDepartment(datacode,department,startTime, endTime);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String user_name = entry.get("USER_NAME").toString();
            String user_id = entry.get("USER_ID").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("user_name", user_name);
            obj.put("user_id", user_id);
            obj.put("callnum", callnum);
            obj.put("download", download);

            List<Map<String, Object>> maps1 = dataMapper.queryUserByUserId(user_id);
            String system = maps1.get(0).get("SYSTEM").toString();

            obj.put("system", system);

            resultList.add(obj);
        }
        return resultList;
    }



    public List dataStatByUseridGroupByDatacode(String userid, String startTime, String endTime,String searchtxt,String orderbyStr) {
        List<Map<String, Object>> list =  dataMapper.dataStatByUseridGroupByDatacode(userid,startTime, endTime,searchtxt,orderbyStr);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String api_data_code = entry.get("API_DATA_CODE").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            String data_name = entry.get("DATA_NAME").toString();
            String data_class_id = entry.get("DATA_CLASS_ID").toString();
            String data_class_name = entry.get("DATA_CLASS_NAME").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("api_data_code", api_data_code);
            obj.put("callnum", callnum);
            obj.put("download", download);
            obj.put("data_name", data_name);
            obj.put("data_class_id", data_class_id);
            obj.put("data_class_name", data_class_name);
            resultList.add(obj);
        }
        return resultList;
    }

    public List dataCategoryNumByUserid(String userid, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataCategoryNumByUserid(userid, startTime, endTime);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String d_datetime = entry.get("D_DATETIME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            String categorynum = entry.get("CATEGORYNUM").toString();
            String usernum = entry.get("USERNUM").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("d_datetime", d_datetime);
            obj.put("callnum", callnum);
            obj.put("download", download);
            obj.put("categorynum", categorynum);
            obj.put("usernum", usernum);
            resultList.add(obj);
        }
        return resultList;
    }

    public List dataStatByUseridAndDatacode(String userid,String datacode, String startTime, String endTime) {
        List<Map<String, Object>> list =  dataMapper.dataStatByUseridAndDatacode(userid, datacode, startTime, endTime);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String d_datetime = entry.get("D_DATETIME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            Map<String, Object> obj = new HashMap<>();
            obj.put("d_datetime", d_datetime);
            obj.put("callnum", callnum);
            obj.put("download", download);
            resultList.add(obj);
        }
        return resultList;
    }

    public List datastatGroupBydep(String department, String startTime, String endTime, String orderby, String limit) {
        if(orderby==null||"".equalsIgnoreCase(orderby)){
            orderby = "DOWNLOAD DESC";
        }
        List<Map<String, Object>> list =  dataMapper.datastatLimitGroupBydep(department, startTime, endTime, orderby, limit);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            String categorynum = entry.get("CATEGORYNUM").toString();
            String sys_user_id = entry.get("SYS_USER_ID").toString();
            String group_id = entry.get("GROUP_ID").toString();
            String user_name = entry.get("USER_NAME").toString();
            String system = entry.get("SYSTEM").toString();

            Map<String, Object> obj = new HashMap<>();
            obj.put("callnum", callnum);
            obj.put("download", download);
            obj.put("categorynum", categorynum);
            obj.put("sys_user_id", sys_user_id);
            obj.put("group_id", group_id);
            obj.put("user_name", user_name);
            obj.put("system", system);
            resultList.add(obj);
        }
        return resultList;
    }

    public List topDataDepList(String datacode,String is_country, String startTime, String endTime, String type) {
        List<Map<String, Object>> list;
        List resultList = new ArrayList();

        list = dataMapper.topDataDepList(datacode,is_country, startTime, endTime);
        List<Map<String, Object>> dataDepTimeDataList = dataMapper.dataDepTimeDataList2(is_country, datacode, startTime, endTime);
        List<String> fullTimeList = getFullTimeList(startTime, endTime);

        Map userDepTimeDataList2 = getUserDepTimeDataList2(dataDepTimeDataList,fullTimeList);
        List<Map<String, Object>> dataDepUserDataList = dataMapper.dataDepUserDataList2(is_country, datacode, startTime, endTime);
        Map userDepClassDataList2 = getUserDepClassDataList2(dataDepUserDataList);
        for (Map<String, Object> entry : list) {
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            //String categorynum = entry.get("CATEGORYNUM").toString();
            String usernum = entry.get("USERNUM").toString();
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();

            Map<String, Object> obj = new LinkedHashMap<>();

            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("usernum", usernum);
            obj.put("download", download);
            obj.put("callnum", callnum);

            obj.put("timelist",userDepTimeDataList2.get(department));
            obj.put("dataclasslist",userDepClassDataList2.get(department));
            resultList.add(obj);
        }

        return resultList;
    }

    public List getUserDepTimeDataList(List<Map<String, Object>> userDepTimeDataList){
        List list = new ArrayList();
        if(userDepTimeDataList!=null&&userDepTimeDataList.size()>0){
            for (Map<String, Object> entry : userDepTimeDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                //String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String d_datetime = entry.get("D_DATETIME").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                //obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("d_datetime", d_datetime);
                list.add(obj);
            }
        }
        return list;
    }

    public List getUserDepClassDataList(List<Map<String, Object>> userDepClassDataList){
        List list = new ArrayList();
        if(userDepClassDataList!=null&&userDepClassDataList.size()>0){
            for (Map<String, Object> entry : userDepClassDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                //String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String sys_user_id = entry.get("SYS_USER_ID").toString();
                String user_name = entry.get("USER_NAME").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                //obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("sys_user_id", sys_user_id);
                obj.put("user_name", user_name);
                list.add(obj);
            }
        }
        return list;
    }

    public static List<String> getFullTimeList(String startTime, String endTime){
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date parse = null;
        Date parse2 = null;
        try {
            parse = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(parse);
        //前开后闭原则
        //前后都闭原则（最新）
        //cal.add(Calendar.DAY_OF_MONTH,1);
        while (cal.getTimeInMillis()<=parse2.getTime()){
            String format = sdf.format(cal.getTime());
            list.add(format);
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public Map getUserDepTimeDataList2(List<Map<String, Object>> userDepTimeDataList,List<String> fullTimeList){
        Map<String,List> map = new HashMap<>();
        Map<String,Set> map2 = new HashMap<>();
        if(userDepTimeDataList!=null&&userDepTimeDataList.size()>0){
            String department;
            for (Map<String, Object> entry : userDepTimeDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                //String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String d_datetime = entry.get("D_DATETIME").toString();
                department = entry.get("DEPARTMENT").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                //obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("d_datetime", d_datetime);

                if(map.containsKey(department)){
                    map.get(department).add(obj);
                }else{
                    List list = new ArrayList();
                    list.add(obj);
                    map.put(department,list);
                }

                if(map2.containsKey(department)){
                    map2.get(department).add(d_datetime);
                }else{
                    Set set = new HashSet();
                    set.add(d_datetime);
                    map2.put(department,set);
                }
            }
        }

        Set<String> strings = map.keySet();
        for (String department:strings) {
            List list = map.get(department);
            if(list.size()<fullTimeList.size()){
                Set set = map2.get(department);
                List newList = new ArrayList();
                Integer j = 0;
                for (int i = 0;i<fullTimeList.size();i++){
                    String timeStr = fullTimeList.get(i);
                    if(set.contains(timeStr)){
                        Object o = list.get(j);
                        newList.add(o);
                        j++;
                    }else{
                        Map<String, String> emptyObj = new HashMap<>();
                        emptyObj.put("callnum", "0");
                        emptyObj.put("download", "0");
                        emptyObj.put("usernum", "0");
                        emptyObj.put("d_datetime", timeStr);
                        newList.add(emptyObj);
                    }
                }

                map.put(department,newList);
            }
        }

        return map;
    }

    public Map getDataClassTimeDataList(List<Map<String, Object>> dataClassTimeDataList,List<String> fullTimeList){
        Map<String,List> map = new HashMap<>();
        Map<String,Set> map2 = new HashMap<>();
        if(dataClassTimeDataList!=null&&dataClassTimeDataList.size()>0){
            String DATA_CLASS_ID;
            for (Map<String, Object> entry : dataClassTimeDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String d_datetime = entry.get("D_DATETIME").toString();
                DATA_CLASS_ID = entry.get("DATA_CLASS_ID").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("d_datetime", d_datetime);

                if(map.containsKey(DATA_CLASS_ID)){
                    map.get(DATA_CLASS_ID).add(obj);
                }else{
                    List list = new ArrayList();
                    list.add(obj);
                    map.put(DATA_CLASS_ID,list);
                }

                if(map2.containsKey(DATA_CLASS_ID)){
                    map2.get(DATA_CLASS_ID).add(d_datetime);
                }else{
                    Set set = new HashSet();
                    set.add(d_datetime);
                    map2.put(DATA_CLASS_ID,set);
                }
            }
        }

        Set<String> strings = map.keySet();
        for (String department:strings) {
            List list = map.get(department);
            if(list.size()<fullTimeList.size()){
                Set set = map2.get(department);
                List newList = new ArrayList();
                Integer j = 0;
                for (int i = 0;i<fullTimeList.size();i++){
                    String timeStr = fullTimeList.get(i);
                    if(set.contains(timeStr)){
                        Object o = list.get(j);
                        newList.add(o);
                        j++;
                    }else{
                        Map<String, String> emptyObj = new HashMap<>();
                        emptyObj.put("callnum", "0");
                        emptyObj.put("download", "0");
                        emptyObj.put("usernum", "0");
                        emptyObj.put("categorynum", "0");
                        emptyObj.put("d_datetime", timeStr);
                        newList.add(emptyObj);
                    }
                }

                map.put(department,newList);
            }
        }

        return map;
    }





    public Map getUserDepClassDataList2(List<Map<String, Object>> userDepClassDataList){
        Map<String,List> map = new HashMap<>();
        if(userDepClassDataList!=null&&userDepClassDataList.size()>0){
            for (Map<String, Object> entry : userDepClassDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                //String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String sys_user_id = entry.get("SYS_USER_ID").toString();
                String user_name = entry.get("USER_NAME").toString();
                String department = entry.get("DEPARTMENT").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                //obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("sys_user_id", sys_user_id);
                obj.put("user_name", user_name);
                if(map.containsKey(department)){
                    map.get(department).add(obj);
                }else{
                    List list = new ArrayList();
                    list.add(obj);
                    map.put(department,list);
                }
            }
        }
        return map;
    }

    public Map getDataClassTop5DataList(List<Map<String, Object>> dataClassTop5DataList){
        Map<String,List> map = new HashMap<>();
        //List list = new ArrayList();
        if(dataClassTop5DataList!=null&&dataClassTop5DataList.size()>0){
            for (Map<String, Object> entry : dataClassTop5DataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String data_class_id = entry.get("DATA_CLASS_ID").toString();
                String data_class_name = entry.get("DATA_CLASS_NAME").toString();
                String data_code = entry.get("DATA_CODE").toString();
                String data_name = entry.get("DATA_NAME").toString();

                //String department = entry.get("DEPARTMENT").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("data_class_id", data_class_id);
                obj.put("data_class_name", data_class_name);
                obj.put("data_code", data_code);
                obj.put("data_name", data_name);

                if(map.containsKey(data_class_id)){
                    map.get(data_class_id).add(obj);
                }else{
                    List list = new ArrayList();
                    list.add(obj);
                    map.put(data_class_id,list);
                }
            }
        }
        Set<String> strings = map.keySet();
        for(String str:strings){
            List newList = new ArrayList();
            List list = map.get(str);
            if(list.size()>5){
                for(int i =0 ;i<5;i++){
                    Object o = list.get(i);
                    newList.add(o);
                }
                map.put(str,newList);
            }
        }
        return map;
    }

    public List<Map<String, Object>> dataUserstatByDatacode(String datacode,String startTime, String endTime,String searchtxt, String tokenDepartment){
        List<Map<String, Object>> list = dataMapper.dataUserstatByDatacode(datacode, startTime, endTime,searchtxt, tokenDepartment);
        return list;
    }

    public List<Map<String, Object>> dataUserstatByDatacodeTop5(String datacode,String startTime, String endTime,String searchtxt, String tokenDepartment){
        List<Map<String, Object>> list = dataMapper.dataUserstatByDatacode(datacode, startTime, endTime,searchtxt, tokenDepartment);
        return list;
    }

    public List dataClassList(String startTime, String endTime, String tokenDepartment) {
        List<Map<String, Object>> list;
        List resultList = new ArrayList();
        List<String> fullTimeList = getFullTimeList(startTime, endTime);

        list = dataMapper.dataClassList(startTime, endTime, tokenDepartment);

        List<Map<String, Object>> dataClassTimeDataList = dataMapper.dataClassTimeDataList(startTime, endTime, tokenDepartment);
        List<Map<String, Object>> dataClassTop5DataList = dataMapper.dataClassTop5DataList(startTime, endTime, tokenDepartment);
        Map userDepTimeDataList2 = getDataClassTimeDataList(dataClassTimeDataList,fullTimeList);
        Map userDepClassDataList2 = getDataClassTop5DataList(dataClassTop5DataList);

        for (Map<String, Object> entry : list) {
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            String categorynum = entry.get("CATEGORYNUM").toString();
            String usernum = entry.get("USERNUM").toString();
            String data_class_id = entry.get("DATA_CLASS_ID").toString();
            String data_class_name = entry.get("DATA_CLASS_NAME").toString();

            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("data_class_id", data_class_id);
            obj.put("data_class_name", data_class_name);
            obj.put("usernum", usernum);
            obj.put("download", download);
            obj.put("callnum", callnum);
            obj.put("categorynum", categorynum);

            obj.put("timelist",userDepTimeDataList2.get(data_class_id));
            obj.put("dataclasslist",userDepClassDataList2.get(data_class_id));

            resultList.add(obj);
        }

        return resultList;
    }

    public List<Map<String, Object>> dataunionsearch(Map map){
        String usertype = (String)map.get("usertype");
        String departemnt = (String)map.get("department");
        String tokenDepartment = (String)map.get("tokenDepartment");
        String system = (String)map.get("system");
        String username = (String)map.get("username");
        String dataname = (String)map.get("dataname");

        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");

        Integer minUsernum = map.get("minUsernum")==null?null:(Integer)map.get("minUsernum");
        Integer maxUsernum = map.get("maxUsernum")==null?null:(Integer)map.get("maxUsernum");
        Long minCallnum = map.get("minCallnum")==null?null:(Long)map.get("minCallnum");
        Long maxCallnum = map.get("maxCallnum")==null?null:(Long)map.get("maxCallnum");
        Long minDownload = map.get("minDownload")==null?null:(Long)map.get("minDownload");
        Long maxDownload = map.get("maxDownload")==null?null:(Long)map.get("maxDownload");

        List<Map<String, Object>> list = dataMapper.dataunionsearch(usertype, departemnt, tokenDepartment, system, username,dataname,
                startTime, endTime,minUsernum,maxUsernum,minCallnum,maxCallnum,minDownload,maxDownload);
        return list;
    }

    public List<Map<String, Object>> getDataDefine(String searchtxt) {
        List<Map<String, Object>> list =  dataMapper.getDataDefine(searchtxt);
        return list;
    }

    public List<Map<String, Object>> getTopData(String department) {
        List<Map<String, Object>> list =  dataMapper.getTopData(department);
        return list;
    }

    public void deleteTopData(String department) {
        dataMapper.deleteTopData(department);
    }

    public void saveTopData(String[] datacodes,String department) {
        if(datacodes!=null&&datacodes.length>0){
            for(int i =1;i<=datacodes.length;i++){
                dataMapper.saveTopData(datacodes[i-1],i,department);
            }
        }
    }

    public Map<String, Long> getTotalUserAndDataAndVolume(String startTime, String endTime) {
        List<Map<String, Object>> list = dataMapper.getTotalUserAndDataAndVolume(startTime, endTime);

        //Map<String, Map<String, List<Long>>> tmp = new HashMap<>();
        Map<String, List<Long>> subObj = new HashMap<>();
        for (Map<String, Object> entry : list) {
            //String datatime = entry.get("DATATIME").toString();
            String userId = entry.get("USERID").toString();
            long dataCount = Long.parseLong(entry.get("DATACOUNT").toString());
            long volume = Long.parseLong(entry.get("VOLUME").toString());
            long callnum = Long.parseLong(entry.get("CALLNUM").toString());
            if(subObj.get("dataCount") == null){
                subObj.put("dataCount", new ArrayList<Long>());
            }
            if(subObj.get("volume") == null){
                subObj.put("volume", new ArrayList<Long>());
            }
            if(subObj.get("callnum") == null){
                subObj.put("callnum", new ArrayList<Long>());
            }

            subObj.get("dataCount").add(dataCount);
            subObj.get("volume").add(volume);
            subObj.get("callnum").add(callnum);
        }

        Map<String, Map<String, Long>> ret = new HashMap<>();

        int limit = 50;

        //for (String datatime : tmp.keySet()) {
            //Map<String, List<Long>> subObj = tmp.get(datatime);

            List<Long> dataCount = subObj.get("dataCount");
            dataCount.sort((a, b) -> {
                if (a > b) {
                    return -1;
                } else if (a < b) {
                    return 1;
                }

                return 0;
            });
            int dataCountIndex = 0;
            long dataCountSum = 0;

            for (int i = 0; i < dataCount.size() && i < limit; i++) {
                dataCountSum += dataCount.get(i);

                dataCountIndex++;
            }

            List<Long> volume = subObj.get("volume");
            volume.sort((a, b) -> {
                if (a > b) {
                    return -1;
                } else if (a < b) {
                    return 1;
                }

                return 0;
            });
            int volumeIndex = 0;
            long volumeSum = 0;

            for (int i = 0; i < volume.size() && i < limit; i++) {
                volumeSum += volume.get(i);

                volumeIndex++;
            }

            List<Long> callnum = subObj.get("callnum");
            callnum.sort((a, b) -> {
                if (a > b) {
                    return -1;
                } else if (a < b) {
                    return 1;
                }

                return 0;
            });
            int callnumIndex = 0;
            long callnumSum = 0;

            for (int i = 0; i < callnum.size() && i < limit; i++) {
                callnumSum += callnum.get(i);

                callnumIndex++;
            }

            Map<String, Long> obj = new HashMap<>();
            obj.put("dataAvg", dataCountSum/dataCountIndex);
            obj.put("volumeAvg", volumeSum/volumeIndex);
            obj.put("callnumAvg", callnumSum/callnumIndex);

        return obj;
    }

    /**
     * 夜间访问量top50平均值
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Long> getYJDownloadAvg(String startTime, String endTime) {
        List<Map<String, Object>> list = dataMapper.getYJDownloadAvg(startTime, endTime);

        //Map<String, Map<String, List<Long>>> tmp = new HashMap<>();
        Map<String, List<Long>> subObj = new HashMap<>();
        for (Map<String, Object> entry : list) {
            //String datatime = entry.get("DATATIME").toString();
            String userId = entry.get("USERID").toString();
            long dataCount = Long.parseLong(entry.get("DATACOUNT").toString());
            long volume = Long.parseLong(entry.get("VOLUME").toString());
            long callnum = Long.parseLong(entry.get("CALLNUM").toString());
            if(subObj.get("dataCount") == null){
                subObj.put("dataCount", new ArrayList<Long>());
            }
            if(subObj.get("volume") == null){
                subObj.put("volume", new ArrayList<Long>());
            }
            if(subObj.get("callnum") == null){
                subObj.put("callnum", new ArrayList<Long>());
            }

            subObj.get("dataCount").add(dataCount);
            subObj.get("volume").add(volume);
            subObj.get("callnum").add(callnum);
        }

        Map<String, Map<String, Long>> ret = new HashMap<>();

        int limit = 50;

        //for (String datatime : tmp.keySet()) {
        //Map<String, List<Long>> subObj = tmp.get(datatime);

        List<Long> dataCount = subObj.get("dataCount");
        dataCount.sort((a, b) -> {
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            }

            return 0;
        });
        int dataCountIndex = 0;
        long dataCountSum = 0;

        for (int i = 0; i < dataCount.size() && i < limit; i++) {
            dataCountSum += dataCount.get(i);

            dataCountIndex++;
        }

        List<Long> volume = subObj.get("volume");
        volume.sort((a, b) -> {
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            }

            return 0;
        });
        int volumeIndex = 0;
        long volumeSum = 0;

        for (int i = 0; i < volume.size() && i < limit; i++) {
            volumeSum += volume.get(i);

            volumeIndex++;
        }

        List<Long> callnum = subObj.get("callnum");
        callnum.sort((a, b) -> {
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            }

            return 0;
        });
        int callnumIndex = 0;
        long callnumSum = 0;

        for (int i = 0; i < callnum.size() && i < limit; i++) {
            callnumSum += callnum.get(i);

            callnumIndex++;
        }

        Map<String, Long> obj = new HashMap<>();
        obj.put("dataAvg", dataCountSum/dataCountIndex);
        obj.put("volumeAvg", volumeSum/volumeIndex);
        obj.put("callnumAvg", callnumSum/callnumIndex);

        return obj;
    }

    public Map<String, Double> getTotalAddPercent(String startTime, String endTime, String startTime2, String endTime2) {
        List<Map<String, Object>> list = dataMapper.getTotalAddPercent(startTime, endTime,startTime2,endTime2);

        Map<String, List<Double>> subObj = new HashMap<>();
        for (Map<String, Object> entry : list) {
            String userId = entry.get("USER_ID").toString();
            double callnumaddpercent = Double.parseDouble(entry.get("CALLNUMADDPERCENT").toString());
            double downloadaddpercent = Double.parseDouble(entry.get("DOWNLOADADDPERCENT").toString());
            double categorynumaddpercent = Double.parseDouble(entry.get("CATEGORYNUMADDPERCENT").toString());
            if(subObj.get("callnumaddpercent") == null){
                subObj.put("callnumaddpercent", new ArrayList<Double>());
            }
            if(subObj.get("downloadaddpercent") == null){
                subObj.put("downloadaddpercent", new ArrayList<Double>());
            }
            if(subObj.get("categorynumaddpercent") == null){
                subObj.put("categorynumaddpercent", new ArrayList<Double>());
            }

            subObj.get("callnumaddpercent").add(callnumaddpercent);
            subObj.get("downloadaddpercent").add(downloadaddpercent);
            subObj.get("categorynumaddpercent").add(categorynumaddpercent);
        }

        int limit = 50;
        List<Double> callnumaddpercentList = subObj.get("callnumaddpercent");
        callnumaddpercentList.sort((a, b) -> {
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            }

            return 0;
        });
        int callnumaddpercentIndex = 0;
        long callnumaddpercentSum = 0;

        for (int i = 0; i < callnumaddpercentList.size() && i < limit; i++) {
            callnumaddpercentSum += callnumaddpercentList.get(i);
            callnumaddpercentIndex++;
        }

        List<Double> downloadaddpercentList = subObj.get("downloadaddpercent");
        downloadaddpercentList.sort((a, b) -> {
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            }

            return 0;
        });
        int downloadaddpercentIndex = 0;
        long downloadaddpercentSum = 0;

        for (int i = 0; i < downloadaddpercentList.size() && i < limit; i++) {
            downloadaddpercentSum += downloadaddpercentList.get(i);
            downloadaddpercentIndex++;
        }

        List<Double> categorynumaddpercentList = subObj.get("categorynumaddpercent");
        categorynumaddpercentList.sort((a, b) -> {
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            }

            return 0;
        });
        int categorynumaddpercentIndex = 0;
        long categorynumaddpercentSum = 0;

        for (int i = 0; i < categorynumaddpercentList.size() && i < limit; i++) {
            categorynumaddpercentSum += categorynumaddpercentList.get(i);
            categorynumaddpercentIndex++;
        }

        Map<String, Double> obj = new HashMap<>();

        obj.put("callnumaddpercentvg", new BigDecimal(callnumaddpercentSum/callnumaddpercentIndex).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        obj.put("downloadaddpercentAvg", new BigDecimal(downloadaddpercentSum/downloadaddpercentIndex).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        obj.put("categorynumaddpercentAvg", new BigDecimal(categorynumaddpercentSum/categorynumaddpercentIndex).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());

        return obj;
    }

}
