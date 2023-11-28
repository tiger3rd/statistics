package com.piesat.statistics.task;

import com.piesat.statistics.bean.MonitorCheckUserInfo;
import com.piesat.statistics.mapper.DataMapper;
import com.piesat.statistics.service.DataService;
import com.piesat.statistics.service.MonitorCheckUserService;
import com.piesat.statistics.service.WhiteListService;
import com.piesat.statistics.util.WeekToDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class StatisProblemUserTask {
    Logger logger = LoggerFactory.getLogger(StatisProblemUserTask.class);
    @Autowired
    DataService dataService;
    @Resource
    DataMapper dataMapper;
    @Autowired
    private MonitorCheckUserService monitorCheckUserService;
    @Autowired
    private WhiteListService whiteListService;

    private int year;
    private int week_of_year;

    @Scheduled(cron = "${cron}")
    public void staticProblemTotal(){
        logger.info("begin static problem user");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR,-1);
        year = cal.get(Calendar.YEAR);
        week_of_year = cal.get(Calendar.WEEK_OF_YEAR);

        String startDayOfWeekNo = WeekToDateUtil.getStartDayOfWeekNo(year, week_of_year);
        String endDayOfWeekNo = WeekToDateUtil.getEndDayOfWeekNo(year, week_of_year);

        String startTime = startDayOfWeekNo.replaceAll("-","");;
        String endTime = endDayOfWeekNo.replaceAll("-","");;
        getProblemUser(null,null,null,startTime,endTime,null);
        logger.info("end static problem user");
    }

    /**
     * 各个纬度的问题账户列表。维度为空时，列出所有问题账户。当一个账户多个维度存在问题时，列出多次
     * @param usertype
     * @param department
     * @param is_check
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    public Map<String, Object> getProblemUser(String usertype, String department, String is_check, String startTime, String endTime, String type){
        Map<String, Object> ret = new HashMap<>(3);
        ret.put("status", 0);
        ret.put("msg", "success");

        String[] horTwoPeriod = getHorTwoPeriod(startTime+"00", endTime+"23");
        String[] dayTwoPeriod = getDayTwoPeriod(startTime, endTime);

        Map<String, Long> totalUserAndDataAndVolume = null;
        Map<String, Long> yjDownloadAvg = null;
        Long categoryAvg = null;
        Long downloadAvg = null;
        Long callnumAvg = null;
        Double callnumaddpercentvg = null;
        Double downloadaddpercentAvg = null;
        Double categorynumaddpercentAvg = null;

        //查询全部类型
        totalUserAndDataAndVolume = dataService.getTotalUserAndDataAndVolume(startTime, endTime);
        yjDownloadAvg = dataService.getYJDownloadAvg(startTime + "00", startTime + "23");

        categoryAvg = totalUserAndDataAndVolume.get("dataAvg");
        //downloadAvg = totalUserAndDataAndVolume.get("volumeAvg");
        downloadAvg = yjDownloadAvg.get("volumeAvg");
        callnumAvg = totalUserAndDataAndVolume.get("callnumAvg");
        Map<String, Double> totalAddPercent = dataService.getTotalAddPercent(dayTwoPeriod[0],dayTwoPeriod[1],dayTwoPeriod[2],dayTwoPeriod[3]);
        callnumaddpercentvg = totalAddPercent.get("callnumaddpercentvg");
        downloadaddpercentAvg = totalAddPercent.get("downloadaddpercentAvg");
        categorynumaddpercentAvg = totalAddPercent.get("categorynumaddpercentAvg");

        List<String> whiteList = whiteListService.getWhiteList();
        Set<String> whiteSet = new HashSet<>();
        whiteSet.addAll(whiteList);

        //各单位访问资料种类数维度异常的账户数
        Map categorynum_avgMap = new HashMap();
        categorynum_avgMap.put("categorynum",categoryAvg);
        List<Map<String, Object>> categorynum_list = dataMapper.getProblemUser(
                usertype, department, is_check, dayTwoPeriod[0],dayTwoPeriod[1],dayTwoPeriod[2],dayTwoPeriod[3],categorynum_avgMap);
        for (Map<String, Object> entry : categorynum_list) {
            entry.put("type","category");
        }

        //各单位访问次数维度异常的账户数
        Map callnum_avgMap = new HashMap();
        callnum_avgMap.put("callnum",callnumAvg);
        List<Map<String, Object>> callnum_list = dataMapper.getProblemUser(
                usertype, department, is_check, dayTwoPeriod[0],dayTwoPeriod[1],dayTwoPeriod[2],dayTwoPeriod[3],callnum_avgMap);
        for (Map<String, Object> entry : callnum_list) {
            entry.put("type","callnum");
        }

        //各单位访问量维度异常的账户数
        Map download_avgMap = new HashMap();
        download_avgMap.put("download",downloadAvg);
        List<Map<String, Object>> download_list = dataMapper.getYJDownloadProblemUser(
                usertype, department, is_check, horTwoPeriod[0],horTwoPeriod[1],horTwoPeriod[2],horTwoPeriod[3],download_avgMap);
        for (Map<String, Object> entry : download_list) {
            entry.put("type","download");
        }

        //各单位访问次数增长维度异常的账户数
        Map callnumaddpercent_avgMap = new HashMap();
        callnumaddpercent_avgMap.put("CALLNUMADDPERCENT",callnumaddpercentvg);
        List<Map<String, Object>> callnumaddpercent_list = dataMapper.getProblemUser(
                usertype, department, is_check, dayTwoPeriod[0],dayTwoPeriod[1],dayTwoPeriod[2],dayTwoPeriod[3],callnumaddpercent_avgMap);
        for (Map<String, Object> entry : callnumaddpercent_list) {
            entry.put("type","callnumadd");
        }

        //各单位访问量增长维度异常的账户数
        Map downloadaddpercent_avgMap = new HashMap();
        downloadaddpercent_avgMap.put("DOWNLOADADDPERCENT",downloadaddpercentAvg);
        List<Map<String, Object>> downloadaddpercent_list = dataMapper.getProblemUser(
                usertype, department, is_check, dayTwoPeriod[0],dayTwoPeriod[1],dayTwoPeriod[2],dayTwoPeriod[3],downloadaddpercent_avgMap);
        for (Map<String, Object> entry : downloadaddpercent_list) {
            entry.put("type","downloadadd");
        }

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String,MonitorCheckUserInfo> map = new HashMap();

        for (Map<String, Object> entry : categorynum_list) {
            String USER_ID = entry.get("USER_ID").toString();
            if(map.containsKey(USER_ID)){
                MonitorCheckUserInfo monitorCheckUserInfo = map.get(USER_ID);
                monitorCheckUserInfo.setType(monitorCheckUserInfo.getType()+",category");
            }else{
                MonitorCheckUserInfo monitorCheckUserInfo = new MonitorCheckUserInfo();
                monitorCheckUserInfo.setUser_id(USER_ID);
                monitorCheckUserInfo.setCheckStatus("1");//未核实状态
                monitorCheckUserInfo.setCreateTime(new Date());//创建时间
                monitorCheckUserInfo.setType("category");
                map.put(USER_ID,monitorCheckUserInfo);
            }
        }

        result.addAll(categorynum_list);

        result.addAll(callnum_list);
        for (Map<String, Object> entry : callnum_list) {
            String USER_ID = entry.get("USER_ID").toString();
            if(map.containsKey(USER_ID)){
                MonitorCheckUserInfo monitorCheckUserInfo = map.get(USER_ID);
                monitorCheckUserInfo.setType(monitorCheckUserInfo.getType()+",callnum");
            }else{
                MonitorCheckUserInfo monitorCheckUserInfo = new MonitorCheckUserInfo();
                monitorCheckUserInfo.setUser_id(USER_ID);
                monitorCheckUserInfo.setCheckStatus("1");//未核实状态
                monitorCheckUserInfo.setCreateTime(new Date());//创建时间
                monitorCheckUserInfo.setType("callnum");
                map.put(USER_ID,monitorCheckUserInfo);
            }
        }
        result.addAll(download_list);
        for (Map<String, Object> entry : download_list) {
            String USER_ID = entry.get("USER_ID").toString();
            if(map.containsKey(USER_ID)){
                MonitorCheckUserInfo monitorCheckUserInfo = map.get(USER_ID);
                monitorCheckUserInfo.setType(monitorCheckUserInfo.getType()+",download");
            }else{
                MonitorCheckUserInfo monitorCheckUserInfo = new MonitorCheckUserInfo();
                monitorCheckUserInfo.setUser_id(USER_ID);
                monitorCheckUserInfo.setCheckStatus("1");//未核实状态
                monitorCheckUserInfo.setCreateTime(new Date());//创建时间
                monitorCheckUserInfo.setType("download");
                map.put(USER_ID,monitorCheckUserInfo);
            }
        }
        result.addAll(callnumaddpercent_list);
        for (Map<String, Object> entry : callnumaddpercent_list) {
            String USER_ID = entry.get("USER_ID").toString();
            if(map.containsKey(USER_ID)){
                MonitorCheckUserInfo monitorCheckUserInfo = map.get(USER_ID);
                monitorCheckUserInfo.setType(monitorCheckUserInfo.getType()+",callnumadd");
            }else{
                MonitorCheckUserInfo monitorCheckUserInfo = new MonitorCheckUserInfo();
                monitorCheckUserInfo.setUser_id(USER_ID);
                monitorCheckUserInfo.setCheckStatus("1");//未核实状态
                monitorCheckUserInfo.setCreateTime(new Date());//创建时间
                monitorCheckUserInfo.setType("callnumadd");
                map.put(USER_ID,monitorCheckUserInfo);
            }
        }
        result.addAll(downloadaddpercent_list);
        for (Map<String, Object> entry : downloadaddpercent_list) {
            String USER_ID = entry.get("USER_ID").toString();
            if(map.containsKey(USER_ID)){
                MonitorCheckUserInfo monitorCheckUserInfo = map.get(USER_ID);
                monitorCheckUserInfo.setType(monitorCheckUserInfo.getType()+",downloadadd");
            }else{
                MonitorCheckUserInfo monitorCheckUserInfo = new MonitorCheckUserInfo();
                monitorCheckUserInfo.setUser_id(USER_ID);
                monitorCheckUserInfo.setCheckStatus("1");//未核实状态
                monitorCheckUserInfo.setCreateTime(new Date());//创建时间
                monitorCheckUserInfo.setType("downloadadd");
                map.put(USER_ID,monitorCheckUserInfo);
            }
        }

        Set<String> strings = map.keySet();
        for(String user_id:strings){
            if(whiteSet.contains(user_id)){
                //如果在白名单中，不保存改问题账户
               continue;
            }
            MonitorCheckUserInfo monitorCheckUserInfo = map.get(user_id);
            //monitorCheckUserInfo.setWeekOfYear(String.valueOf(week_of_year));
            monitorCheckUserInfo.setWeekOfYear(String.valueOf(10));
            monitorCheckUserInfo.setYear(String.valueOf(year));
            monitorCheckUserService.save(monitorCheckUserInfo);
        }
        ret.put("data",result);
        return ret;
    }

    public String[] getHorTwoPeriod(String startTime,String endTime){
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
        return new String[]{startTime1,endTime1,startTime2,endTime2};
    }

    public String[] getDayTwoPeriod(String startTime,String endTime){
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
        return new String[]{startTime1,endTime1,startTime2,endTime2};
    }

}
