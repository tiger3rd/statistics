package com.piesat.statistics.service;

import com.piesat.statistics.bean.MonitorCheckUserInfo;
import com.piesat.statistics.mapper.MonitorCheckUserMapper;
import com.piesat.statistics.mapper.DataMapper;
import com.piesat.statistics.util.WeekToDateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MonitorCheckUserService {
    @Resource
    MonitorCheckUserMapper monitorUserCheckMapper;
    @Resource
    DataMapper dataMapper;

    public void save(MonitorCheckUserInfo user){
        monitorUserCheckMapper.saveUser(user);
    }

    public MonitorCheckUserInfo getUserById (Integer id){
        return monitorUserCheckMapper.getUserById(id);
    }

    public void updateUserCheck(MonitorCheckUserInfo user){
        monitorUserCheckMapper.updateUserCheck(user);
    }

    public void updateUserFeedback(MonitorCheckUserInfo user){
        monitorUserCheckMapper.updateUserFeedback(user);
    }

    public List<Map<String, Object>> getList(String department, String checkStatus,String type) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR,-1);
        int year = cal.get(Calendar.YEAR);
        int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);

        String startDayOfWeekNo = WeekToDateUtil.getStartDayOfWeekNo(year, week_of_year);
        String endDayOfWeekNo = WeekToDateUtil.getEndDayOfWeekNo(year, week_of_year);

        //return monitorUserCheckMapper.getList(department, checkStatus, type);
        List<Map<String, Object>> list = monitorUserCheckMapper.getList(department, checkStatus, type,year,week_of_year);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> entry : list) {
            Map<String, Object> map = new HashMap();
            Object ID = entry.get("ID");
            map.put("ID",ID);
            Object USER_ID = entry.get("USER_ID").toString()==null?"":entry.get("USER_ID");
            map.put("USER_ID",USER_ID);
            Object CHECKSTATUS = entry.get("CHECKSTATUS")==null?"":entry.get("CHECKSTATUS");
            map.put("CHECKSTATUS",CHECKSTATUS);
            Object typesss = entry.get("TYPE")==null?"":entry.get("TYPE");
            map.put("TYPE",typesss);
            Object YEAR = entry.get("YEAR")==null?"":entry.get("YEAR");
            map.put("YEAR",YEAR);
            Object WEEKOFYEAR = entry.get("WEEKOFYEAR")==null?"":entry.get("WEEKOFYEAR");
            map.put("WEEKOFYEAR",WEEKOFYEAR);
            Object CREATETIME = entry.get("CREATETIME")==null?"":entry.get("CREATETIME");
            map.put("CREATETIME",CREATETIME);
            Object CREATEBY = entry.get("CREATEBY")==null?"":entry.get("CREATEBY");
            map.put("CREATEBY",CREATEBY);
            Object UPDATETIME = entry.get("UPDATETIME")==null?"":entry.get("UPDATETIME");
            map.put("UPDATETIME",UPDATETIME);
            Object UPDATEBY = entry.get("UPDATEBY")==null?"":entry.get("UPDATEBY");
            map.put("UPDATEBY",UPDATEBY);
            Object CHECKTIME = entry.get("CHECKTIME")==null?"":entry.get("CHECKTIME");
            map.put("CHECKTIME",CHECKTIME);
            Object CHECKDAYLIMIT = entry.get("CHECKDAYLIMIT")==null?"":entry.get("CHECKDAYLIMIT");
            map.put("CHECKDAYLIMIT",CHECKDAYLIMIT);
            Object CHECKDESC = entry.get("CHECKDESC")==null?"":entry.get("CHECKDESC");
            map.put("CHECKDESC",CHECKDESC);
            Object EMAILTITLE = entry.get("EMAILTITLE")==null?"":entry.get("EMAILTITLE");
            map.put("EMAILTITLE",EMAILTITLE);
            Object TARGETEMAIL = entry.get("TARGETEMAIL")==null?"":entry.get("TARGETEMAIL");
            map.put("TARGETEMAIL",TARGETEMAIL);
            Object EMAILCONTENT = entry.get("EMAILCONTENT")==null?"":entry.get("EMAILCONTENT");
            map.put("EMAILCONTENT",EMAILCONTENT);
            Object EMAILPUSHSTATUS = entry.get("EMAILPUSHSTATUS")==null?"":entry.get("EMAILPUSHSTATUS");
            map.put("EMAILPUSHSTATUS",EMAILPUSHSTATUS);
            Object FEEDBACKSTATUS = entry.get("FEEDBACKSTATUS")==null?"":entry.get("FEEDBACKSTATUS");
            map.put("FEEDBACKSTATUS",FEEDBACKSTATUS);
            Object MEASURESTATUS = entry.get("MEASURESTATUS")==null?"":entry.get("MEASURESTATUS");
            map.put("MEASURESTATUS",MEASURESTATUS);
            Object FEEDBACKFILEPATH = entry.get("FEEDBACKFILEPATH")==null?"":entry.get("FEEDBACKFILEPATH");
            map.put("FEEDBACKFILEPATH",FEEDBACKFILEPATH);
            Object FEEDBACKDESC = entry.get("FEEDBACKDESC")==null?"":entry.get("FEEDBACKDESC");
            map.put("FEEDBACKDESC",FEEDBACKDESC);

            Object USER_NAME = entry.get("USER_NAME")==null?"":entry.get("USER_NAME");
            map.put("USER_NAME",USER_NAME);
            Object SYSTEM = entry.get("SYSTEM")==null?"":entry.get("SYSTEM");
            map.put("SYSTEM",SYSTEM);
            Object VERIFY_STATUS = entry.get("VERIFY_STATUS")==null?"":entry.get("VERIFY_STATUS");
            map.put("VERIFY_STATUS",VERIFY_STATUS);
            Object EMAIL = entry.get("EMAIL")==null?"":entry.get("EMAIL");
            map.put("EMAIL",EMAIL);
            Object GROUP_ID = entry.get("GROUP_ID")==null?"":entry.get("GROUP_ID");
            map.put("GROUP_ID",GROUP_ID);
            Object DEPARTMENT = entry.get("DEPARTMENT")==null?"":entry.get("DEPARTMENT");
            map.put("DEPARTMENT",DEPARTMENT);
            Object DEPARTMENT_NAME = entry.get("DEPARTMENT_NAME")==null?"":entry.get("DEPARTMENT_NAME");
            map.put("DEPARTMENT_NAME",DEPARTMENT_NAME);

            //开始时间
            map.put("STARTTIME",startDayOfWeekNo);
            //结束时间
            map.put("ENETIME",endDayOfWeekNo);

            result.add(map);
        }
        return result;
    }

    public List<Map<String, Object>> getList2(String department, String checkStatus,String type,int pageNum, int pageSize) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR,-1);
        int year = cal.get(Calendar.YEAR);
        int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);

        List<Map<String, Object>> result = new ArrayList();
        if(type==null){
            List<Map<String, Object>> categorynum_list = monitorUserCheckMapper.getList(department, checkStatus, "category", year, week_of_year);
            for (Map<String, Object> entry :categorynum_list) {
                entry.put("TYPE","category");
            }
            List<Map<String, Object>> callnum_list = monitorUserCheckMapper.getList(department, checkStatus, "callnum", year, week_of_year);
            for (Map<String, Object> entry :callnum_list) {
                entry.put("TYPE","callnum");
            }
            List<Map<String, Object>> download_list = monitorUserCheckMapper.getList(department, checkStatus, "download", year, week_of_year);
            for (Map<String, Object> entry :download_list) {
                entry.put("TYPE","download");
            }
            List<Map<String, Object>> callnumaddpercent_list = monitorUserCheckMapper.getList(department, checkStatus, "callnumadd", year, week_of_year);
            for (Map<String, Object> entry :callnumaddpercent_list) {
                entry.put("TYPE","callnumadd");
            }
            List<Map<String, Object>> downloadaddpercent_list = monitorUserCheckMapper.getList(department, checkStatus, "downloadadd", year, week_of_year);
            for (Map<String, Object> entry :downloadaddpercent_list) {
                entry.put("TYPE","downloadadd");
            }
            result.addAll(categorynum_list);
            result.addAll(callnum_list);
            result.addAll(download_list);
            result.addAll(callnumaddpercent_list);
            result.addAll(downloadaddpercent_list);

            /*if(result.size()>10){
                result = result.subList(0,10);
            }*/
            return result;
        }else{
            result = monitorUserCheckMapper.getList(department, checkStatus, type, year, week_of_year);
            for (Map<String, Object> entry :result) {
                entry.put("TYPE",type);
            }
            /*if(result.size()>10){
                result = result.subList(0,10);
            }*/
            return result;
        }

    }

    public List<Map<String, Object>> getListByUserId(String user_id) {
        return monitorUserCheckMapper.getListByUserId(user_id);
    }

    public List<Map<String, Object>> getProblemTotal(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR,-1);
        int year = cal.get(Calendar.YEAR);
        int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);

        List<Map<String, Object>> countryDepartment2 = dataMapper.getCountryDepartment();
        List<Map<String, Object>> countryDepartment = new ArrayList<>();
        Map<String, Object> kejisi = countryDepartment2.get(0);
        Map<String, Object> guojisi = countryDepartment2.get(1);
        List<Map<String, Object>> maps1 = countryDepartment2.subList(2,countryDepartment2.size());
        countryDepartment.addAll(maps1);
        countryDepartment.add(kejisi);
        countryDepartment.add(guojisi);

        List<Map<String, Object>> depProblemUserCountList = monitorUserCheckMapper.getProblemUserGroupByDepartment(null,"1", year, week_of_year);

        //各单位访问资料种类数维度异常的账户数
        List<Map<String, Object>> categorynum_list = monitorUserCheckMapper.getProblemUserGroupByDepartment("category","1", year, week_of_year);
        Map<String,String> categorynumMap = new HashMap();
        for (Map<String, Object> entry : categorynum_list) {
            String departmentcode = entry.get("DEPARTMENT").toString();
            String problemnum = entry.get("PROBLEMNUM").toString();
            categorynumMap.put(departmentcode,problemnum);
        }

        //各单位访问次数维度异常的账户数
        List<Map<String, Object>> callnum_list = monitorUserCheckMapper.getProblemUserGroupByDepartment("callnum","1", year, week_of_year);
        Map<String,String> callnumMap = new HashMap();
        for (Map<String, Object> entry : callnum_list) {
            String departmentcode = entry.get("DEPARTMENT").toString();
            String problemnum = entry.get("PROBLEMNUM").toString();
            callnumMap.put(departmentcode,problemnum);
        }

        //各单位访问量维度异常的账户数
        List<Map<String, Object>> download_list = monitorUserCheckMapper.getProblemUserGroupByDepartment("download","1", year, week_of_year);
        Map<String,String> downloadMap = new HashMap();
        for (Map<String, Object> entry : download_list) {
            String departmentcode = entry.get("DEPARTMENT").toString();
            String problemnum = entry.get("PROBLEMNUM").toString();
            downloadMap.put(departmentcode,problemnum);
        }

        //各单位访问次数增长维度异常的账户数
        List<Map<String, Object>> callnumaddpercent_list = monitorUserCheckMapper.getProblemUserGroupByDepartment("callnumadd","1", year, week_of_year);
        Map<String,String> callnumaddpercentMap = new HashMap();
        for (Map<String, Object> entry : callnumaddpercent_list) {
            String departmentcode = entry.get("DEPARTMENT").toString();
            String problemnum = entry.get("PROBLEMNUM").toString();
            callnumaddpercentMap.put(departmentcode,problemnum);
        }

        //各单位访问量增长维度异常的账户数
        List<Map<String, Object>> downloadaddpercent_list = monitorUserCheckMapper.getProblemUserGroupByDepartment("downloadadd","1", year, week_of_year);
        Map<String,String> downloadaddpercentMap = new HashMap();
        for (Map<String, Object> entry : downloadaddpercent_list) {
            String departmentcode = entry.get("DEPARTMENT").toString();
            String problemnum = entry.get("PROBLEMNUM").toString();
            downloadaddpercentMap.put(departmentcode,problemnum);
        }

        //统计所有单位各个纬度的问题账户总数
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Map<String, Object> entry : countryDepartment) {
            String DEPARTMENT_ID = entry.get("DEPARTMENT_ID").toString();
            String DEPARTMENT_NAME = entry.get("DEPARTMENT_NAME").toString();

            Map map = new HashMap<>();
            Boolean flag = false;
            for (Map<String, Object> entry2 : depProblemUserCountList) {
                String departmentcode = entry2.get("DEPARTMENT").toString();
                if(DEPARTMENT_ID.equals(departmentcode)){
                    flag = true;
                    map.put("DEPARTMENT",DEPARTMENT_ID);
                    map.put("DEPARTMENT_NAME",DEPARTMENT_NAME);
                    String categorynumProblemUser = categorynumMap.get(DEPARTMENT_ID)==null?"0":categorynumMap.get(DEPARTMENT_ID);
                    String callnumProblemUser = callnumMap.get(DEPARTMENT_ID)==null?"0":callnumMap.get(DEPARTMENT_ID);
                    String downloadProblemUser = downloadMap.get(DEPARTMENT_ID)==null?"0":downloadMap.get(DEPARTMENT_ID);
                    String callnumaddpercentProblemUser = callnumaddpercentMap.get(DEPARTMENT_ID)==null?"0":callnumaddpercentMap.get(DEPARTMENT_ID);
                    String downloadaddpercentProblemUser = downloadaddpercentMap.get(DEPARTMENT_ID)==null?"0":downloadaddpercentMap.get(DEPARTMENT_ID);

                    map.put("categoryProblemUser",categorynumProblemUser);
                    map.put("callnumProblemUser",callnumProblemUser);
                    map.put("downloadProblemUser",downloadProblemUser);
                    map.put("callnumaddProblemUser",callnumaddpercentProblemUser);
                    map.put("downloadaddProblemUser",downloadaddpercentProblemUser);

                    Integer total = Integer.parseInt(categorynumProblemUser)+Integer.parseInt(callnumProblemUser)+Integer.parseInt(downloadProblemUser)+Integer.parseInt(callnumaddpercentProblemUser)+Integer.parseInt(downloadaddpercentProblemUser);
                    map.put("totalProblemUser",total);
                }else{

                }
                if(!flag){
                    //没有问题账户，数量均为0
                    map.put("DEPARTMENT",DEPARTMENT_ID);
                    map.put("DEPARTMENT_NAME",DEPARTMENT_NAME);
                    map.put("totalProblemUser","0");
                    map.put("categoryProblemUser","0");
                    map.put("callnumProblemUser","0");
                    map.put("downloadProblemUser","0");
                    map.put("callnumaddProblemUser","0");
                    map.put("downloadaddProblemUser","0");
                }
            }

            maps.add(map);
        }
        return maps;
    }

}
