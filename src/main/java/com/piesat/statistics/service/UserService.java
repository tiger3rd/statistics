package com.piesat.statistics.service;

import com.piesat.statistics.bean.DataBean;
import com.piesat.statistics.bean.DataClassBean;
import com.piesat.statistics.bean.UserInfo;
import com.piesat.statistics.mapper.DataMapper;
import com.piesat.statistics.mapper.UserMapper;
import com.piesat.statistics.pojo.vo.UserDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {
    @Resource
    UserMapper mapper;

    @Resource
    DataMapper dataMapper;

    public Map<String, UserInfo> getAllUserInfo() {
        List<UserInfo> list = mapper.getAllUserInfo();

        Map<String, UserInfo> data = new HashMap<>();

        for (UserInfo entry : list) {
            data.put(entry.getUser_id().toString(), entry);
        }

        return data;
    }

    public UserInfo getUserInfoById(String userId) {
        UserInfo ui = mapper.getUserInfoById(userId);

        return ui;
    }

    public List getUserById(String userid) {
        List<Map<String, Object>> userById = mapper.getUserById(userid);
        return userById;
    }

    public Integer getTotalCount() {
        return mapper.getTotalCount();
    }

    public Map<String, Object> getTotalCountGroupByType() {
        List<Map<String, Object>> list = mapper.getTotalCountGroupByType();

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("TYPE").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public Map<String, Integer> getTotalCountGroupByDepartment() {
        List<Map<String, Object>> list = mapper.getTotalCountGroupByDepartment();

        Map<String, Integer> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            int tmp = 0;

            if (entry.get("TOTAL") != null) {
                tmp = Integer.parseInt(entry.get("TOTAL").toString());
            }

            data.put(entry.get("DEPARTMENT").toString(), tmp);
        }

        return data;
    }

    public Map<String, Map<String, Object>> getTotalCountGroupByDeparmentAndType() {
        Map<String, Map<String, Object>> ret = new HashMap<>();

        List<Map<String, Object>> list = mapper.getTotalCountGroupByDeparmentAndType();

        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String type = entry.get("TYPE").toString();
            Object count = entry.get("TOTAL");

            if (ret.get(department) == null) {
                ret.put(department, new HashMap<>());
            }

            Map<String, Object> departmentObj = ret.get(department);

            departmentObj.put(type, count);
        }

        return ret;
    }

    public Integer getNewCount(String startTime, String endTime) {
        return mapper.getNewCount(startTime, endTime);
    }

    public Integer getDisableCount(String startTime, String endTime) {
        return mapper.getDisableCount(startTime, endTime);
    }

    public Map<String, Object> getNewCountGroupByType(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getNewCountGroupByType(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("TYPE").toString(), entry.get("TOTAL"));
        }

        return data;
    }


    public Map<String, Object> getNewCountGroupByDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getNewCountGroupByDepartment(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("DEPARTMENT").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public List getHeaderNewCountGroupByDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getHeaderNewCountGroupByDepartment(startTime, endTime);
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            Map map = new TreeMap();
            map.put("id",entry.get("DEPARTMENT").toString());
            map.put("name",entry.get("DEPARTMENT_NAME").toString());
            resultList.add(map);
        }

        return resultList;
    }

    public List<Map<String, Object>> getTotalCountGroupByType(String startTime, String endTime) throws ParseException {
        List<Map<String, Object>> ret = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Date endTimeObj = sdf.parse(endTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startTime));
        while (cal.getTime().before(endTimeObj)) {
            String day = sdf2.format(cal.getTime());

            List<Map<String, Object>> list = mapper.getCountGroupByType(day + " 23:59:59");

            Map<String, Object> data = new HashMap<>();
            data.put("DAY", day);

            for (Map<String, Object> entry : list) {
                data.put(entry.get("TYPE").toString(), entry.get("TOTAL"));
            }

            ret.add(data);

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return ret;
    }

    public List<Map<String, Object>> getCountGroupByDepartment(String startTime, String endTime) throws ParseException {
        List<Map<String, Object>> ret = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Date endTimeObj = sdf.parse(endTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startTime));

        while (cal.getTime().before(endTimeObj)) {
            String day = sdf2.format(cal.getTime());

            List<Map<String, Object>> list = mapper.getCountGroupByDepartment(day + " 00:00:00");

            Map<String, Object> data = new HashMap<>();
            data.put("DAY", day);

            for (Map<String, Object> entry : list) {
                data.put(entry.get("DEPARTMENT").toString(), entry.get("TOTAL"));
            }

            ret.add(data);

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return ret;
    }


    public Integer getTotalActiveCount(String startTime, String endTime) {
        return mapper.getTotalActiveCount(startTime, endTime);
    }

    public Map<String, Object> getTotalActiveCountByType(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getTotalActiveCountByType(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("TYPE").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public Map<String, Integer> getActiveCountByDepartmentAndType(String startTime, String endTime) {
        Map<String, Integer> ret = new HashMap<>();

        List<Map<String, Object>> list = mapper.getActiveCountByDepartment(startTime, endTime);

        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            Object count = entry.get("TOTAL");

            int tmp = 0;

            if (count != null) {
                tmp = Integer.parseInt(count.toString());
            }

            ret.put(department, tmp);
        }

        return ret;
    }


    public Integer getAccessCount(String startTime, String endTime) {
        return mapper.getAccessCount(startTime, endTime);
    }

    public Map<String, Object> getAccessCountByType(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getAccessCountByType(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("TYPE").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public Map<String, Object> getAccessCountByDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getAccessCountByDepartment(startTime, endTime);

        Map<String, Object> data = new HashMap<>();

        for (Map<String, Object> entry : list) {
            data.put(entry.get("DEPARTMENT").toString(), entry.get("TOTAL"));
        }

        return data;
    }

    public List getHeaderAccessCountDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getHeaderAccessCountDepartment(startTime, endTime);
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            Map map = new TreeMap();
            map.put("id",entry.get("DEPARTMENT").toString());
            map.put("name",entry.get("DEPARTMENT_NAME").toString());
            resultList.add(map);
        }

        return resultList;
    }

    public List getHeaderAccessHorCountDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getHeaderAccessHorCountDepartment(startTime, endTime);
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            Map map = new TreeMap();
            map.put("id",entry.get("DEPARTMENT").toString());
            map.put("name",entry.get("DEPARTMENT_NAME").toString());
            resultList.add(map);
        }

        return resultList;
    }

    public Map<String, Map<String, Object>> getAccessCountByDayAndType(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getAccessCountByDayAndType(startTime, endTime);

        Map<String, Map<String, Object>> ret = new HashMap<>();

        for (Map<String, Object> entry : list) {
            String datatime = entry.get("DATATIME").toString();
            String type = entry.get("TYPE").toString();
            Object count = entry.get("TOTAL");

            if (ret.get(datatime) == null) {
                ret.put(datatime, new HashMap<>());
            }

            Map<String, Object> obj = ret.get(datatime);

            obj.put(type, count);
        }

        return ret;
    }

    public Map<String, Map<String, Object>> getAccessCountByHorAndType(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getAccessCountByHorAndType(startTime, endTime);

        Map<String, Map<String, Object>> ret = new HashMap<>();

        for (Map<String, Object> entry : list) {
            String datatime = entry.get("DATATIME").toString();
            String type = entry.get("TYPE").toString();
            Object count = entry.get("TOTAL");

            if (ret.get(datatime) == null) {
                ret.put(datatime, new HashMap<>());
            }

            Map<String, Object> obj = ret.get(datatime);

            obj.put(type, count);
        }

        return ret;
    }

    public Map<String, Map<String, Object>> getAccessCountByDayAndDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getAccessCountByHorAndDepartment(startTime, endTime);

        Map<String, Map<String, Object>> ret = new HashMap<>();

        for (Map<String, Object> entry : list) {
            String datatime = entry.get("DATATIME").toString();
            String department = entry.get("DEPARTMENT").toString();
            Object count = entry.get("TOTAL");

            if (ret.get(datatime) == null) {
                ret.put(datatime, new HashMap<>());
            }

            Map<String, Object> obj = ret.get(datatime);

            obj.put(department, count);
        }

        return ret;
    }

    public Map<String, Map<String, Object>> getAccessCountByHorAndDepartment(String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getAccessCountByHorAndDepartment(startTime, endTime);

        Map<String, Map<String, Object>> ret = new HashMap<>();

        for (Map<String, Object> entry : list) {
            String datatime = entry.get("DATATIME").toString();
            String department = entry.get("DEPARTMENT").toString();
            Object count = entry.get("TOTAL");

            if (ret.get(datatime) == null) {
                ret.put(datatime, new HashMap<>());
            }

            Map<String, Object> obj = ret.get(datatime);

            obj.put(department, count);
        }

        return ret;
    }

    public List<Map<String, Object>> getSingleUserStatisticsByDay(String userId, String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getSingleUserStatisticsByDay(userId, startTime, endTime);

        return list;
    }

    public List<Map<String, Object>> getSingleUserStatisticsByHor(String userId, String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.getSingleUserStatisticsByHor(userId, startTime, endTime);

        return list;
    }

    public List<Map<String, Object>> getUserListByCountry(int country) {
        return mapper.getUserListByCountry(country);
    }

    public List<Map<String, Object>> getVolumeByUserGroupByDataClassID(String userId, String startTime, String endTime) {
        List<DataClassBean> dataClassBeanList = dataMapper.getAllDataClass();

        Map<String, DataClassBean> classMap = new HashMap<>();

        for (DataClassBean bean : dataClassBeanList) {
            classMap.put(bean.getData_class_id(), bean);
        }

        List<Map<String, Object>> list = mapper.getVolumeByUserGroupByDataClassID(userId, startTime, endTime);

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

    public List<Map<String, Object>> getVolumeByUserGroupByDataClassIDUseHorTable(String userId, String startTime, String endTime) {
        List<DataClassBean> dataClassBeanList = dataMapper.getAllDataClass();

        Map<String, DataClassBean> classMap = new HashMap<>();

        for (DataClassBean bean : dataClassBeanList) {
            classMap.put(bean.getData_class_id(), bean);
        }

        List<Map<String, Object>> list = mapper.getVolumeByUserGroupByDataClassIDUseHorTable(userId, startTime, endTime);

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

    public List<Map<String, Object>> getDetailByUser(String userId, String startTime, String endTime) {
        List<DataBean> dataList = dataMapper.getAllData();

        Map<String, DataBean> dataMap = new HashMap<>();

        for (DataBean bean : dataList) {
            dataMap.put(bean.getData_code(), bean);
        }

        List<Map<String, Object>> list = mapper.getDetailByUser(userId, startTime, endTime);

        for (Map<String, Object> entry : list) {
            if (entry.get("DATACODE") != null) {
                String datacode = entry.get("DATACODE").toString();

                if (dataMap.get(datacode) != null) {
                    entry.put("DATANAME", dataMap.get(datacode).getData_name());
                }
            }
        }

        return list;
    }


    public Map<String, Map<String, Long>> getTotalUserAndDataAndVolume(String startTime, String endTime) throws ParseException {
        List<Map<String, Object>> list = mapper.getTotalUserAndDataAndVolume(startTime, endTime);

        Map<String, Map<String, List<Long>>> tmp = new HashMap<>();
        for (Map<String, Object> entry : list) {
            String datatime = entry.get("DATATIME").toString();
            String userId = entry.get("USERID").toString();
            long dataCount = Long.parseLong(entry.get("DATACOUNT").toString());
            long volume = Long.parseLong(entry.get("VOLUME").toString());
            long callnum = Long.parseLong(entry.get("CALLNUM").toString());

            if (tmp.get(datatime) == null) {
                Map<String, List<Long>> subObj = new HashMap<>();
                subObj.put("dataCount", new ArrayList<Long>());
                subObj.put("volume", new ArrayList<Long>());
                subObj.put("callnum", new ArrayList<Long>());

                tmp.put(datatime, subObj);
            }

            Map<String, List<Long>> subObj = tmp.get(datatime);

            subObj.get("dataCount").add(dataCount);
            subObj.get("volume").add(volume);
            subObj.get("callnum").add(callnum);
        }

        Map<String, Map<String, Long>> ret = new HashMap<>();

        int limit = 50;

        for (String datatime : tmp.keySet()) {
            Map<String, List<Long>> subObj = tmp.get(datatime);

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

            ret.put(datatime, obj);
        }

        return ret;
    }

    public Map<String, Map<String, Long>> getTotalUserAndDataAndVolumeHor(String startTime, String endTime) throws ParseException {
        List<Map<String, Object>> list = mapper.getTotalUserAndDataAndVolumeHor(startTime, endTime);

        Map<String, Map<String, List<Long>>> tmp = new HashMap<>();
        for (Map<String, Object> entry : list) {
            String datatime = entry.get("DATATIME").toString();
            String userId = entry.get("USERID").toString();
            long dataCount = Long.parseLong(entry.get("DATACOUNT").toString());
            long volume = Long.parseLong(entry.get("VOLUME").toString());
            long callnum = Long.parseLong(entry.get("CALLNUM").toString());

            if (tmp.get(datatime) == null) {
                Map<String, List<Long>> subObj = new HashMap<>();
                subObj.put("dataCount", new ArrayList<Long>());
                subObj.put("volume", new ArrayList<Long>());
                subObj.put("callnum", new ArrayList<Long>());

                tmp.put(datatime, subObj);
            }

            Map<String, List<Long>> subObj = tmp.get(datatime);

            subObj.get("dataCount").add(dataCount);
            subObj.get("volume").add(volume);
            subObj.get("callnum").add(callnum);
        }

        Map<String, Map<String, Long>> ret = new HashMap<>();

        int limit = 50;

        for (String datatime : tmp.keySet()) {
            Map<String, List<Long>> subObj = tmp.get(datatime);

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

            ret.put(datatime, obj);
        }

        return ret;
    }

    public Map<String, Map<String, Long>> getTotalUserAndDataAndVolumeByHor(String startTime, String endTime) throws ParseException {
        List<Map<String, Object>> list = mapper.getTotalUserAndDataAndVolumeByHor(startTime, endTime);

        Map<String, Map<String, List<Long>>> tmp = new HashMap<>();
        for (Map<String, Object> entry : list) {
            String datatime = entry.get("DATATIME").toString();
            String userId = entry.get("USERID").toString();
            long dataCount = Long.parseLong(entry.get("DATACOUNT").toString());
            long volume = Long.parseLong(entry.get("VOLUME").toString());
            long callnum = Long.parseLong(entry.get("CALLNUM").toString());

            if (tmp.get(datatime) == null) {
                Map<String, List<Long>> subObj = new HashMap<>();
                subObj.put("dataCount", new ArrayList<Long>());
                subObj.put("volume", new ArrayList<Long>());
                subObj.put("callnum", new ArrayList<Long>());

                tmp.put(datatime, subObj);
            }

            Map<String, List<Long>> subObj = tmp.get(datatime);

            subObj.get("dataCount").add(dataCount);
            subObj.get("volume").add(volume);
            subObj.get("callnum").add(callnum);
        }

        Map<String, Map<String, Long>> ret = new HashMap<>();

        int limit = 50;

        for (String datatime : tmp.keySet()) {
            Map<String, List<Long>> subObj = tmp.get(datatime);

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

            ret.put(datatime, obj);
        }

        return ret;
    }

    public List<String> accessTimes(String userId, String dataCode, String startTime, String endtime) {
        return mapper.accessTimes(userId, dataCode, startTime, endtime);
    }


    public Map userByType(String usertype,String isnation,String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.userByType(usertype,isnation, startTime, endTime);
        Integer totalCount = mapper.userCountType(usertype, isnation,startTime, endTime);
        Map map = new HashMap();
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String usernumber = entry.get("USERNUMBER").toString();

            Map<String, String> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("usernumber", usernumber);

            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("totalCount",totalCount);
        return map;
    }

    public Map userByTypeByIsactive(Boolean isactive,String usertype,String isnation,String startTime, String endTime) {
        String substring1 = startTime.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 8);
        String substring2 = endTime.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 8);
        List<Map<String, Object>> list = null;
        Integer totalCount = 0;
        if(isactive){
            list = mapper.userByTypeByActive(usertype,isnation, startTime, endTime,substring1,substring2);
            totalCount = mapper.userCountTypeByActive(usertype,isnation, startTime, endTime,substring1,substring2);
        }else{
            list = mapper.userByTypeByNotActive(usertype,isnation, startTime, endTime,substring1,substring2);
            totalCount = mapper.userCountTypeByNotActive(usertype,isnation, startTime, endTime,substring1,substring2);
        }

        Map map = new HashMap();
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String usernumber = entry.get("USERNUMBER").toString();

            Map<String, String> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("usernumber", usernumber);

            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("totalCount",totalCount);
        return map;
    }

    public List<UserDetail> userByTypeAndDep(String usertype, String department, String startTime, String endTime) {
        return mapper.userByTypeAndDep(usertype, department, startTime, endTime);
    }

    public List userCategoryDetail(String userid,  String startTime, String endTime) {
        List<Map<String, Object>> list =  mapper.userCategoryDetail(userid, startTime, endTime);
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String data_code = entry.get("DATA_CODE").toString();
            String data_name = entry.get("DATA_NAME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();
            String data_class_id = entry.get("DATA_CLASS_ID").toString();
            String data_class_name = entry.get("DATA_CLASS_NAME").toString();

            Map<String, String> obj = new HashMap<>();
            obj.put("data_code", data_code);
            obj.put("data_name", data_name);
            obj.put("callnum", callnum);
            obj.put("download", download);
            obj.put("data_class_id", data_class_id);
            obj.put("data_class_name", data_class_name);

            resultList.add(obj);
        }
        return resultList;
    }

    public List userAccessDetail(String userid,  String startTime, String endTime) {
        List<Map<String, Object>> list =  mapper.userAccessDetail(userid, startTime, endTime);
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String data_code = entry.get("DATA_CODE").toString();
            String data_name = entry.get("DATA_NAME").toString();
            String d_datetime = entry.get("D_DATETIME").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();

            Map<String, String> obj = new HashMap<>();
            obj.put("data_code", data_code);
            obj.put("data_name", data_name);
            obj.put("d_datetime", d_datetime);
            obj.put("callnum", callnum);
            obj.put("download", download);

            resultList.add(obj);
        }
        return resultList;
    }

    public Map userTypeCount(String startTime, String endTime,String type, String tokenDepartment) {
        if("1".equals(type)){
            return basicuserTotal(tokenDepartment);
        }else if("2".equals(type)){
            return newUserTotal(startTime, endTime,tokenDepartment);
        }else if("3".equals(type)){
            String startTime1 = startTime.replaceAll("-","");
            String endTime1 = endTime.replaceAll("-","");
            return activeUserTotal(startTime1, endTime1, tokenDepartment);
        }else {
            String startTime1 = startTime.replaceAll("-","");
            String endTime1 = endTime.replaceAll("-","");
            return activeUserTotal(startTime1, endTime1, tokenDepartment);
        }

        /*String[] groupids =new String[]{"G_YW","G_KY","G_KYJX","G_SJGL"};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

        startTime1 = startTime + " 00:00:00";
        endTime1 = endTime + " 00:00:00";
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime()) + " 00:00:00";


        List<Map<String, Object>> list =  mapper.userTypeCount(startTime1, endTime1);
        List<Map<String, Object>> list2 =  mapper.userTypeCount(startTime2, endTime2);
        Set<String> set = new HashSet<>();
        Map<String,Map<String, Object>> tempMap  = new TreeMap<>();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            obj.put("oldnum", "0");
            tempMap.put(group_id,obj);
        }

        for (Map<String, Object> entry : list2) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);

            if(tempMap.containsKey(group_id)){
                Map<String, Object> obj = tempMap.get(group_id);
                obj.put("oldnum",num);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", group_id);
                obj.put("num", "0");
                obj.put("oldnum", num);
                tempMap.put(group_id,obj);
            }
        }

        for (String groupid:groupids) {
            Map<String, Object> stringObjectMap = tempMap.get(groupid);
            if(stringObjectMap!=null){
                resultList.add(stringObjectMap);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", groupid);
                obj.put("num", "0");
                obj.put("oldnum", 0);
                resultList.add(obj);
            }
        }
        return resultList;*/
    }

    public List newUserToalGroupByUsertype(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

        startTime1 = startTime + " 00:00:00";
        endTime1 = endTime + " 00:00:00";
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime()) + " 00:00:00";

        List<Map<String, Object>> list =  mapper.newUserToalGroupByUsertype(startTime1, endTime1);
        List<Map<String, Object>> list2 =  mapper.newUserToalGroupByUsertype(startTime2, endTime2);
        Set<String> set = new HashSet<>();
        Map<String,Map<String, Object>> tempMap  = new TreeMap<>();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            obj.put("oldnum", "0");
            tempMap.put(group_id,obj);
        }

        for (Map<String, Object> entry : list2) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);

            if(tempMap.containsKey(group_id)){
                Map<String, Object> obj = tempMap.get(group_id);
                obj.put("oldnum",num);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", group_id);
                obj.put("num", "0");
                obj.put("oldnum", num);
                tempMap.put(group_id,obj);
            }
        }

        Set<String> keys = tempMap.keySet();
        for (String str:keys) {
            Map<String, Object> stringObjectMap = tempMap.get(str);
            resultList.add(stringObjectMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int num1 = Integer.parseInt(o1.get("num").toString());
                int num2 = Integer.parseInt(o2.get("num").toString());
                return num2-num1;
            }
        });
        return resultList;
    }

    public List activeUserToalGroupByUsertype(String startTime, String endTime) {
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

        List<Map<String, Object>> list =  mapper.activeUserToalGroupByUsertype(startTime1, endTime1);
        List<Map<String, Object>> list2 =  mapper.activeUserToalGroupByUsertype(startTime2, endTime2);
        Set<String> set = new HashSet<>();
        Map<String,Map<String, Object>> tempMap  = new TreeMap<>();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            obj.put("oldnum", "0");
            tempMap.put(group_id,obj);
        }

        for (Map<String, Object> entry : list2) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);

            if(tempMap.containsKey(group_id)){
                Map<String, Object> obj = tempMap.get(group_id);
                obj.put("oldnum",num);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", group_id);
                obj.put("num", "0");
                obj.put("oldnum", num);
                tempMap.put(group_id,obj);
            }
        }

        Set<String> keys = tempMap.keySet();
        for (String str:keys) {
            Map<String, Object> stringObjectMap = tempMap.get(str);
            resultList.add(stringObjectMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int num1 = Integer.parseInt(o1.get("num").toString());
                int num2 = Integer.parseInt(o2.get("num").toString());
                return num2-num1;
            }
        });
        return resultList;
    }

    public List userTypeCountByIsactive(Boolean isactive, String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

        startTime1 = startTime + " 00:00:00";
        endTime1 = endTime + " 00:00:00";
        endTime2 = startTime1;
        startTime2 = sdf.format(cal1.getTime()) + " 00:00:00";
        List<Map<String, Object>> list;
        List<Map<String, Object>> list2;
        String substring1 = startTime1.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 8);
        String substring2 = endTime1.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 8);
        String substring3 = startTime2.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 8);
        String substring4 = endTime2.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 8);
        if(isactive){
            list =  mapper.userTypeCountActive(startTime1, endTime1,substring1,substring2);
            list2 =  mapper.userTypeCountActive(startTime2, endTime2,substring3,substring4);
        }else{
            list =  mapper.userTypeCountNoActive(startTime1, endTime1,substring1,substring2);
            list2 =  mapper.userTypeCountNoActive(startTime2, endTime2,substring3,substring4);
        }

        Set<String> set = new HashSet<>();
        Map<String,Map<String, Object>> tempMap  = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            obj.put("oldnum", "0");
            tempMap.put(group_id,obj);
        }

        for (Map<String, Object> entry : list2) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);

            if(tempMap.containsKey(group_id)){
                Map<String, Object> obj = tempMap.get(group_id);
                obj.put("oldnum",num);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", group_id);
                obj.put("num", "0");
                obj.put("oldnum", num);
                tempMap.put(group_id,obj);
            }
        }

        Set<String> keys = tempMap.keySet();
        for (String str:keys) {
            Map<String, Object> stringObjectMap = tempMap.get(str);
            resultList.add(stringObjectMap);
        }
        return resultList;
    }


    public Map activeuserTotalByUsertypeGroupByDepartment(String usertype,String isnation,String startTime, String endTime) {
        List<Map<String, Object>> list = mapper.activeuserTotalByUsertypeGroupByDepartment(usertype,isnation, startTime, endTime);
        Integer totalCount = mapper.userCountType(usertype, isnation,startTime, endTime);
        Map map = new HashMap();
        List resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String usernumber = entry.get("USERNUMBER").toString();

            Map<String, String> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("usernumber", usernumber);

            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("totalCount",totalCount);
        return map;
    }

    public Map activeUserTotal(String startTime, String endTime, String tokenDepartment) {
        Map map = new TreeMap();
        Integer total = 0;
        Integer oldTotal = 0;
        String[] groupids =new String[]{"G_YW","G_KY","G_KYJX","G_SJGL"};
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
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;

        startTime1 = startTime;
        endTime1 = endTime;

        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-1);
        endTime2 = sdf.format(cal1.getTime());
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));

        startTime2 = sdf.format(cal1.getTime());

        List<Map<String, Object>> list =  mapper.activeUserTotal(startTime1, endTime1, tokenDepartment);
        List<Map<String, Object>> list2 =  mapper.activeUserTotal(startTime2, endTime2, tokenDepartment);
        Set<String> set = new HashSet<>();
        Map<String,Map<String, Object>> tempMap  = new TreeMap<>();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            total += Integer.parseInt(num);
            set.add(group_id);
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            obj.put("oldnum", "0");
            tempMap.put(group_id,obj);
        }

        for (Map<String, Object> entry : list2) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            oldTotal += Integer.parseInt(num);
            set.add(group_id);
            if(tempMap.containsKey(group_id)){
                Map<String, Object> obj = tempMap.get(group_id);
                obj.put("oldnum",num);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", group_id);
                obj.put("num", "0");
                obj.put("oldnum", num);
                tempMap.put(group_id,obj);
            }
        }
/*        Set<String> keys = tempMap.keySet();
        for (String str:keys) {
            Map<String, Object> stringObjectMap = tempMap.get(str);
            resultList.add(stringObjectMap);
        }*/
        for (String groupid:groupids) {
            Map<String, Object> stringObjectMap = tempMap.get(groupid);
            if(stringObjectMap!=null){
                resultList.add(stringObjectMap);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", groupid);
                obj.put("num", "0");
                obj.put("oldnum", 0);
                resultList.add(obj);
            }
        }
        map.put("list",resultList);
        map.put("total",total);
        map.put("oldTotal",oldTotal);
        return map;
    }

    public Map newUserTotal(String startTime, String endTime, String tokenDepartment) {
        Map map = new TreeMap();
        Integer total = 0;
        Integer oldTotal = 0;
        String[] groupids =new String[]{"G_YW","G_KY","G_KYJX","G_SJGL"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal1 = Calendar.getInstance();
        Date parse1 = null;
        Date parse2 = null;
        try {
            parse1 = sdf.parse(startTime);
            parse2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String startTime1;
        String endTime1;
        String startTime2;
        String endTime2;

        startTime1 = startTime +" 00:00:00";
        endTime1 = endTime +" 23:59:59";

        long temp = parse2.getTime()-parse1.getTime();
        long diff = temp/1000/3600/24;
        cal1.setTime(parse1);
        cal1.add(Calendar.DAY_OF_MONTH,-1);
        endTime2 = sdf.format(cal1.getTime()) +" 23:59:59";
        cal1.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(String.valueOf(diff)));

        startTime2 = sdf.format(cal1.getTime()) +" 00:00:00";

        List<Map<String, Object>> list =  mapper.newUserTotal(startTime1, endTime1, tokenDepartment);
        List<Map<String, Object>> list2 =  mapper.newUserTotal(startTime2, endTime2, tokenDepartment);
        Set<String> set = new HashSet<>();
        Map<String,Map<String, Object>> tempMap  = new TreeMap<>();
        List<Map<String, Object>> resultList = new ArrayList();

        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            set.add(group_id);
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            total += Integer.parseInt(num);
            obj.put("oldnum", "0");
            tempMap.put(group_id,obj);
        }

        for (Map<String, Object> entry : list2) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            oldTotal += Integer.parseInt(num);
            set.add(group_id);
            if(tempMap.containsKey(group_id)){
                Map<String, Object> obj = tempMap.get(group_id);
                obj.put("oldnum",num);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", group_id);
                obj.put("num", "0");
                obj.put("oldnum", num);
                tempMap.put(group_id,obj);
            }
        }
        /*Set<String> keys = tempMap.keySet();
        for (String str:keys) {
            Map<String, Object> stringObjectMap = tempMap.get(str);
            resultList.add(stringObjectMap);
        }*/
        for (String groupid:groupids) {
            Map<String, Object> stringObjectMap = tempMap.get(groupid);
            if(stringObjectMap!=null){
                resultList.add(stringObjectMap);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", groupid);
                obj.put("num", "0");
                obj.put("oldnum", 0);
                resultList.add(obj);
            }
        }
        map.put("list",resultList);
        map.put("total",total);
        map.put("oldTotal",oldTotal);
        return map;
    }

    public Map basicuserTotal(String tokenDepartment) {
        Map map  = new TreeMap<>();
        String[] groupids =new String[]{"G_YW","G_KY","G_KYJX","G_SJGL"};
        Integer total = 0;
        Integer oldTotal = 0;

        List<Map<String, Object>> list =  mapper.basicuserTotal(tokenDepartment);
        Map<String,Map<String, Object>> tempMap  = new TreeMap<>();
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String group_id = entry.get("GROUP_ID").toString();
            String num = entry.get("NUM").toString();;
            Map<String, Object> obj = new HashMap<>();
            obj.put("group_id", group_id);
            obj.put("num", num);
            total += Integer.parseInt(num);
            obj.put("oldnum", "0");
            //resultList.add(obj);
            tempMap.put(group_id,obj);
        }

        for (String groupid:groupids) {
            Map<String, Object> stringObjectMap = tempMap.get(groupid);
            if(stringObjectMap!=null){
                resultList.add(stringObjectMap);
            }else{
                Map<String, Object> obj = new HashMap<>();
                obj.put("group_id", groupid);
                obj.put("num", "0");
                obj.put("oldnum", 0);
                resultList.add(obj);
            }
        }

        //return resultList;
        map.put("list",resultList);
        map.put("total",total);
        map.put("oldTotal",oldTotal);
        return map;
    }

    public Map activeUserDepList(String usertype,String is_country,String isactive, String startTime, String endTime) {
        List<Map<String, Object>> list;
        if("false".equalsIgnoreCase(isactive)){
            list = mapper.activeUserDepList(usertype,is_country, startTime, endTime);
        }else{
            list = mapper.notActiveUserDepList(usertype,is_country, startTime, endTime);
        }

        Map map = new HashMap();
        List resultList = new ArrayList();

        Integer totalCount = 0;
        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String usernumber = entry.get("USERNUMBER").toString();
            Map<String, String> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("usernumber", usernumber);
            totalCount += Integer.parseInt(usernumber);
            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("totalCount",totalCount);
        return map;
    }

    public Map newUserDepList(String usertype,String is_country, String startTime, String endTime) {
        List<Map<String, Object>> list;
        list = mapper.newUserDepList(usertype,is_country, startTime+" 00:00:00", endTime+" 00:00:00");

        Map map = new HashMap();
        List resultList = new ArrayList();

        Integer totalCount = 0;
        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String usernumber = entry.get("USERNUMBER").toString();
            Map<String, String> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("usernumber", usernumber);
            totalCount += Integer.parseInt(usernumber);
            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("totalCount",totalCount);
        return map;
    }

    public Map basicUserDepList(String usertype) {
        List<Map<String, Object>> list;
        list = mapper.basicUserDepList(usertype);

        Map map = new HashMap();
        List resultList = new ArrayList();

        Integer totalCount = 0;
        for (Map<String, Object> entry : list) {
            String department = entry.get("DEPARTMENT").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String usernumber = entry.get("USERNUMBER").toString();
            Map<String, String> obj = new HashMap<>();
            obj.put("department", department);
            obj.put("department_name", department_name);
            obj.put("usernumber", usernumber);
            totalCount += Integer.parseInt(usernumber);
            resultList.add(obj);
        }
        map.put("list",resultList);
        map.put("totalCount",totalCount);
        return map;
    }

    public List accessCountByUserType(String usertype, String startTime, String endTime) {
        List<Map<String, Object>> list =  mapper.accessCountByUserType(usertype, startTime, endTime);
        List<Map<String, Object>> resultList = new ArrayList();
        for (Map<String, Object> entry : list) {
            String system = entry.get("SYSTEM").toString();
            String department_name = entry.get("DEPARTMENT_NAME").toString();
            String department = entry.get("DEPARTMENT").toString();
            String user_name = entry.get("USER_NAME").toString();
            String user_id = entry.get("USER_ID").toString();
            String callnum = entry.get("CALLNUM").toString();
            String download = entry.get("DOWNLOAD").toString();

            Map<String, Object> obj = new HashMap<>();
            obj.put("system", system);
            obj.put("department_name", department_name);
            obj.put("department", department);
            obj.put("user_name", user_name);
            obj.put("user_id", user_id);
            obj.put("callnum", callnum);
            obj.put("download", download);
            resultList.add(obj);
        }
        return resultList;
    }

    public List accessCountByUserTypeGroupBy(String usertype, String startTime, String endTime) {
        List<Map<String, Object>> list =  mapper.accessCountByUserTypeGroupBy(usertype, startTime, endTime);
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

    public List<UserDetail> activeUserList(String usertype,String department, String is_country,String isactive, String startTime, String endTime) {
        if("false".equalsIgnoreCase(isactive)){
            return mapper.activeUserList(usertype, department,is_country, startTime, endTime);
        }else{
            return mapper.notActiveUserList(usertype, department,is_country, startTime, endTime);
        }

    }

    public List<UserDetail> newUserList(String usertype,String department, String is_country,String isactive, String startTime, String endTime) {
        String recordStartTime = startTime.replaceAll("-","");
        String recordEndTime = endTime.replaceAll("-","");
        String createStartTime = startTime+ " 00:00:00";
        String createEndTime = endTime+ " 00:00:00";;

        return mapper.newUserList(usertype, department,is_country, recordStartTime, recordEndTime,createStartTime,createEndTime);
    }

    public List<UserDetail> basicUserList(String usertype,String department, String is_country,String isactive, String startTime, String endTime) {
        return mapper.basicUserList(usertype, department, startTime, endTime);
    }

    public List userDepList(String usertype,String is_country,String isactive, String startTime, String endTime, String type, String tokenDepartment) {
        List<Map<String, Object>> list;
        List resultList = new ArrayList();
        List<String> fullTimeList = getFullTimeList(startTime, endTime);
        if("1".equalsIgnoreCase(type)){
            //国际司放到最后,不要排到前面（和数据局排序字段不一致，需手动处理）GJS
            list = mapper.basicUserDepList2(usertype,is_country, startTime, endTime, tokenDepartment);
            List<Map<String, Object>> userDepTimeDataList = mapper.userDepTimeDataList3(usertype, is_country, startTime, endTime, tokenDepartment);
            List<Map<String, Object>> userDepClassDataList = mapper.userDepClassDataList3(usertype, is_country, startTime, endTime, tokenDepartment);
            Map userDepTimeDataList2 = getUserDepTimeDataList2(userDepTimeDataList,fullTimeList,list);
            Map userDepClassDataList2 = getUserDepClassDataList2(userDepClassDataList);
            Boolean hasGjs = false;//是否含有国际司
            Map<String, Object> gjsObj = null;
            for (Map<String, Object> entry : list) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String department = entry.get("DEPARTMENT").toString();
                String department_name = entry.get("DEPARTMENT_NAME").toString();
                Map<String, Object> obj = new LinkedHashMap<>();
                obj.put("department", department);
                obj.put("department_name", department_name);
                obj.put("usernum", usernum);
                obj.put("download", download);
                obj.put("callnum", callnum);
                obj.put("categorynum", categorynum);

                //List userDepTimeDataList1 = getUserDepTimeDataList(userDepTimeDataList);
                obj.put("timelist",userDepTimeDataList2.get(department)==null?new ArrayList<>():userDepTimeDataList2.get(department));
                List userDepClassDataList1 = getUserDepClassDataList(userDepClassDataList);
                obj.put("dataclasslist",userDepClassDataList2.get(department)==null?new ArrayList<>():userDepClassDataList2.get(department));
                if("GJS".equals(department)){
                    hasGjs = true;
                    gjsObj = obj;
                    continue;
                }
                resultList.add(obj);
            }
            if(hasGjs){
                resultList.add(gjsObj);
            }


        }else if("2".equalsIgnoreCase(type)){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String usreCreateStartTime = null;
            String userCreateEndTime = null;
            try {
                usreCreateStartTime = sdf2.format(sdf1.parse(startTime))+" 00:00:00";
                userCreateEndTime = sdf2.format(sdf1.parse(endTime))+" 23:59:59";;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list = mapper.newUserDepList2(usertype,is_country, startTime, endTime, usreCreateStartTime, userCreateEndTime, tokenDepartment);
            List<Map<String, Object>> userDepTimeDataList = mapper.newUserDepTimeDataList2(usertype, is_country, startTime, endTime, usreCreateStartTime, userCreateEndTime, tokenDepartment);
            List<Map<String, Object>> userDepClassDataList = mapper.newUserDepClassDataList2(usertype, is_country, startTime, endTime, usreCreateStartTime, userCreateEndTime, tokenDepartment);
            Map userDepTimeDataList2 = getUserDepTimeDataList2(userDepTimeDataList,fullTimeList,list);
            Map userDepClassDataList2 = getUserDepClassDataList2(userDepClassDataList);

            for (Map<String, Object> entry : list) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String department = entry.get("DEPARTMENT").toString();
                String department_name = entry.get("DEPARTMENT_NAME").toString();

                Map<String, Object> obj = new LinkedHashMap<>();

                obj.put("department", department);
                obj.put("department_name", department_name);
                obj.put("usernum", usernum);
                obj.put("download", download);
                obj.put("callnum", callnum);
                obj.put("categorynum", categorynum);

                //List userDepTimeDataList1 = getUserDepTimeDataList(userDepTimeDataList);
                obj.put("timelist",userDepTimeDataList2.get(department)==null?new ArrayList<>():userDepTimeDataList2.get(department));
                List userDepClassDataList1 = getUserDepClassDataList(userDepClassDataList);
                obj.put("dataclasslist",userDepClassDataList2.get(department)==null?new ArrayList<>():userDepClassDataList2.get(department));
                resultList.add(obj);
            }
        }else{
            list = mapper.userDepList(usertype,is_country, startTime, endTime, tokenDepartment);

            List<Map<String, Object>> userDepTimeDataList = mapper.userDepTimeDataList3(usertype, is_country, startTime, endTime, tokenDepartment);
            List<Map<String, Object>> userDepClassDataList = mapper.userDepClassDataList3(usertype, is_country, startTime, endTime, tokenDepartment);
            Map userDepTimeDataList2 = getUserDepTimeDataList2(userDepTimeDataList,fullTimeList,list);
            Map userDepClassDataList2 = getUserDepClassDataList2(userDepClassDataList);

            for (Map<String, Object> entry : list) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String department = entry.get("DEPARTMENT").toString();
                String department_name = entry.get("DEPARTMENT_NAME").toString();

                Map<String, Object> obj = new LinkedHashMap<>();
                obj.put("department", department);
                obj.put("department_name", department_name);
                obj.put("usernum", usernum);
                obj.put("download", download);
                obj.put("callnum", callnum);
                obj.put("categorynum", categorynum);

                /*//List<Map<String, Object>> userDepTimeDataList = mapper.userDepTimeDataList2(usertype, is_country, startTime, endTime);
                List userDepTimeDataList1 = getUserDepTimeDataList(userDepTimeDataList);
                obj.put("timelist",userDepTimeDataList1);

                //List<Map<String, Object>> userDepClassDataList = mapper.userDepClassDataList2(usertype, is_country, startTime, endTime);
                List userDepClassDataList1 = getUserDepClassDataList(userDepClassDataList);
                obj.put("dataclasslist",userDepClassDataList1);*/

                obj.put("timelist",userDepTimeDataList2.get(department));
                obj.put("dataclasslist",userDepClassDataList2.get(department));

                resultList.add(obj);
            }
        }

        return resultList;
    }

    public List depUserList(String department, String startTime, String endTime, String type) {
        if("1".equals(type)){
            List<Map<String, Object>> list;
            List resultList = new ArrayList();
            List<String> fullTimeList = getFullTimeList(startTime, endTime);

            list = mapper.basicDepUserList(department, startTime, endTime);
            List<Map<String, Object>> depUserTimeDataList = mapper.depUserTimeDataList(department, startTime, endTime);
            List<Map<String, Object>> depUserClassDataList = mapper.depUserClassDataList(department, startTime, endTime);

            Map depUserTimeDataList2 = getDepUserTimeDataList(depUserTimeDataList,fullTimeList,list);
            Map depUserClassDataList2 = getDepUserClassDataList(depUserClassDataList);

            for (Map<String, Object> entry : list) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String group_id = entry.get("GROUP_ID").toString();
                //String department_name = entry.get("DEPARTMENT_NAME").toString();

                Map<String, Object> obj = new LinkedHashMap<>();
                obj.put("group_id", group_id);
                //obj.put("department_name", department_name);
                obj.put("usernum", usernum);
                obj.put("download", download);
                obj.put("callnum", callnum);
                obj.put("categorynum", categorynum);

                obj.put("timelist",depUserTimeDataList2.get(group_id));
                obj.put("dataclasslist",depUserClassDataList2.get(group_id));

                resultList.add(obj);
            }
            Collections.sort(resultList, new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int no1 = getGroupIDOrderNo(o1.get("group_id").toString());
                    int no2 = getGroupIDOrderNo(o2.get("group_id").toString());
                    return no1-no2;
                }
            });
            return resultList;
        }else{
            List<Map<String, Object>> list;
            List resultList = new ArrayList();
            List<String> fullTimeList = getFullTimeList(startTime, endTime);

            list = mapper.depUserList(department, startTime, endTime);

            List<Map<String, Object>> depUserTimeDataList = mapper.depUserTimeDataList(department, startTime, endTime);
            List<Map<String, Object>> depUserClassDataList = mapper.depUserClassDataList(department, startTime, endTime);
            Map depUserTimeDataList2 = getDepUserTimeDataList(depUserTimeDataList,fullTimeList, list);
            Map depUserClassDataList2 = getDepUserClassDataList(depUserClassDataList);


            for (Map<String, Object> entry : list) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String group_id = entry.get("GROUP_ID").toString();
                //String department_name = entry.get("DEPARTMENT_NAME").toString();

                Map<String, Object> obj = new LinkedHashMap<>();
                obj.put("group_id", group_id);
                //obj.put("department_name", department_name);
                obj.put("usernum", usernum);
                obj.put("download", download);
                obj.put("callnum", callnum);
                obj.put("categorynum", categorynum);

                obj.put("timelist",depUserTimeDataList2.get(group_id));
                obj.put("dataclasslist",depUserClassDataList2.get(group_id));

                resultList.add(obj);
            }
            Collections.sort(resultList, new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int no1 = getGroupIDOrderNo(o1.get("group_id").toString());
                    int no2 = getGroupIDOrderNo(o2.get("group_id").toString());
                    return no1-no2;
                }
            });
            return resultList;
        }

    }

    public static int getGroupIDOrderNo(String group_id){
        if("G_YW".equals(group_id)){
            return 1;
        }else if("G_KY".equals(group_id)){
            return 2;
        }else if("G_KYJX".equals(group_id)){
            return 3;
        }else if("G_SJGL".equals(group_id)){
            return 4;
        }else{
            return 5;
        }
    }

    public List getUserDepTimeDataList(List<Map<String, Object>> userDepTimeDataList){
        List list = new ArrayList();
        if(userDepTimeDataList!=null&&userDepTimeDataList.size()>0){
            for (Map<String, Object> entry : userDepTimeDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String d_datetime = entry.get("D_DATETIME").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
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
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String data_class_id = entry.get("DATA_CLASS_ID").toString();
                String data_class_name = entry.get("DATA_CLASS_NAME").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("data_class_id", data_class_id);
                obj.put("data_class_name", data_class_name);
                list.add(obj);
            }
        }
        return list;
    }

    public Map getUserDepTimeDataList2(List<Map<String, Object>> userDepTimeDataList,List<String> fullTimeList,List<Map<String, Object>> departmentList){
        Map<String,List> map = new HashMap<>();
        Map<String,Set> map2 = new HashMap<>();
        if(userDepTimeDataList!=null&&userDepTimeDataList.size()>0){
            for (Map<String, Object> entry : userDepTimeDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String d_datetime = entry.get("D_DATETIME").toString();
                String department = entry.get("DEPARTMENT").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
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

        for (Map<String, Object> entry : departmentList) {
            String department = entry.get("DEPARTMENT").toString();
                if(map.get(department)!=null){
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
                                emptyObj.put("categorynum", "0");
                                emptyObj.put("usernum", "0");
                                emptyObj.put("d_datetime", timeStr);
                                newList.add(emptyObj);
                            }
                        }
                        map.put(department,newList);
                    }
                }else{
                    List newList = new ArrayList();
                    for (int i = 0;i<fullTimeList.size();i++){
                        String timeStr = fullTimeList.get(i);
                        Map<String, String> emptyObj = new HashMap<>();
                        emptyObj.put("callnum", "0");
                        emptyObj.put("download", "0");
                        emptyObj.put("categorynum", "0");
                        emptyObj.put("usernum", "0");
                        emptyObj.put("d_datetime", timeStr);
                        newList.add(emptyObj);
                    }
                    map.put(department,newList);
                };
        }
        return map;
    }

    public Map getUserDepClassDataList2(List<Map<String, Object>> userDepClassDataList){
        Map<String,List> map = new HashMap<>();
        //List list = new ArrayList();
        if(userDepClassDataList!=null&&userDepClassDataList.size()>0){
            for (Map<String, Object> entry : userDepClassDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String data_class_id = entry.get("DATA_CLASS_ID").toString();
                String data_class_name = entry.get("DATA_CLASS_NAME").toString();

                String department = entry.get("DEPARTMENT").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("data_class_id", data_class_id);
                obj.put("data_class_name", data_class_name);

                if(map.containsKey(department)){
                    map.get(department).add(obj);
                }else{
                    List list = new ArrayList();
                    list.add(obj);
                    map.put(department,list);
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


    public List test(int pageNum,int pageSize) {
        List<UserInfo> pageUserInfo = mapper.getPageUserInfo(pageNum, pageSize);
        return pageUserInfo;
    }

    public List<Map<String, Object>> userstatByUsertypeAndDepartment(String usertype,String departemnt, String data_class_id,String startTime, String endTime,String searchtxt, String type, String verify_status){
        if("1".equals(type)){
            List<Map<String, Object>> list = mapper.totalUserstatByUsertypeAndDepartment(usertype, departemnt, data_class_id, startTime, endTime,searchtxt);
            return list;
        }else if("2".equals(type)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String createStartTime = null;
            String createEndTime = null;
            try {
                createStartTime = sdf2.format(sdf.parse(startTime))+" 00:00:00";
                createEndTime = sdf2.format(sdf.parse(endTime))+" 23:59:59";
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<Map<String, Object>> list;
            if("N".equals(verify_status)){
                list = mapper.disableUserstatByUsertypeAndDepartment(usertype, departemnt, data_class_id, startTime, endTime, searchtxt, createStartTime,createEndTime);
            }else{
                list = mapper.newUserstatByUsertypeAndDepartment(usertype, departemnt, data_class_id, startTime, endTime, searchtxt, createStartTime,createEndTime);
            }
            return list;
        }else if("4".equals(type)){
            List<Map<String, Object>> list = mapper.userstatByUsertypeAndDepartmentNotActive(usertype, departemnt, data_class_id, startTime, endTime,searchtxt);
            return list;
        }else {
            List<Map<String, Object>> list = mapper.userstatByUsertypeAndDepartment(usertype, departemnt, data_class_id, startTime, endTime,searchtxt);
            return list;
        }
    }

    public List<Map<String, Object>> userstatByUsertypeAndDepartmentTotal(String usertype,String departemnt, String data_class_id,String startTime, String endTime,String searchtxt, String type,String verify_status){
        if("1".equals(type)){
            List<Map<String, Object>> list = mapper.totalUserstatByUsertypeAndDepartmentTotal(usertype, departemnt, data_class_id, startTime, endTime,searchtxt);
            return list;
        }else if("2".equals(type)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String createStartTime = null;
            String createEndTime = null;
            try {
                createStartTime = sdf2.format(sdf.parse(startTime))+" 00:00:00";
                createEndTime = sdf2.format(sdf.parse(endTime))+" 23:59:59";
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<Map<String, Object>> list;
            if("N".equals(verify_status)){
                list = mapper.disableUserstatByUsertypeAndDepartmentTotal(usertype, departemnt, data_class_id, startTime, endTime, searchtxt, createStartTime,createEndTime);
            }else{
                list = mapper.newUserstatByUsertypeAndDepartmentTotal(usertype, departemnt, data_class_id, startTime, endTime, searchtxt, createStartTime,createEndTime);
            }

            return list;
        }else if("4".equals(type)){
            List<Map<String, Object>> list = mapper.userstatByUsertypeAndDepartmentTotal(usertype, departemnt, data_class_id, startTime, endTime,searchtxt);
            return list;
        }else {
            List<Map<String, Object>> list = mapper.userstatByUsertypeAndDepartmentTotal(usertype, departemnt, data_class_id, startTime, endTime,searchtxt);
            return list;
        }
    }

    public List<Map<String, Object>> userstatByUserid(String userid,String startTime, String endTime,String searchtxt){
        List<Map<String, Object>> list = mapper.userstatByUserid(userid, startTime, endTime,searchtxt);
        return list;
    }

    public List<Map<String, Object>> userstatByUseridTop5(String userid,String startTime, String endTime){
        List<Map<String, Object>> list = mapper.userstatByUseridTop5(userid, startTime, endTime);
        return list;
    }

    public List<Map<String, Object>> userDataTimeDetail(String userid,String datacode, String startTime, String endTime){
        List<Map<String, Object>> list = mapper.userDataTimeDetail(userid, datacode, startTime+"00", endTime+"00");
        Map<String, Map<String, Long>> avgMap = null;
        try {
            avgMap = getTotalUserAndDataAndVolumeHor(startTime + "00", endTime + "00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Map<String, Object> entity : list) {
            String datatime = entity.get("D_DATETIME").toString();
            if (avgMap.get(datatime) != null) {
                Map<String, Long> subObj = avgMap.get(datatime);
                entity.put("dataAvg", subObj.get("dataAvg"));
                entity.put("volumeAvg", subObj.get("volumeAvg"));
                entity.put("callnumAvg", subObj.get("callnumAvg"));
            }
        }
        return list;
    }

    public Map getDepUserTimeDataList(List<Map<String, Object>> depUserTimeDataList,List<String> fullTimeList,List<Map<String, Object>> groupidlist){
        Map<String,List> map = new HashMap<>();
        Map<String,Set> map2 = new HashMap<>();
        if(depUserTimeDataList!=null&&depUserTimeDataList.size()>0){
            for (Map<String, Object> entry : depUserTimeDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String d_datetime = entry.get("D_DATETIME").toString();
                String GROUP_ID = entry.get("GROUP_ID").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("d_datetime", d_datetime);

                if(map.containsKey(GROUP_ID)){
                    map.get(GROUP_ID).add(obj);
                }else{
                    List list = new ArrayList();
                    list.add(obj);
                    map.put(GROUP_ID,list);
                }

                if(map2.containsKey(GROUP_ID)){
                    map2.get(GROUP_ID).add(d_datetime);
                }else{
                    Set set = new HashSet();
                    set.add(d_datetime);
                    map2.put(GROUP_ID,set);
                }
            }
        }

        for (Map<String, Object> entry : groupidlist) {
            String usertype = entry.get("GROUP_ID").toString();
            List list = map.get(usertype);
            if(list!=null){
                if(list.size()<fullTimeList.size()){
                    Set set = map2.get(usertype);
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
                            emptyObj.put("categorynum", "0");
                            emptyObj.put("usernum", "0");
                            emptyObj.put("d_datetime", timeStr);
                            newList.add(emptyObj);
                        }
                    }
                    map.put(usertype,newList);
                }
            }else{
                List newList = new ArrayList();
                for (int i = 0;i<fullTimeList.size();i++){
                    String timeStr = fullTimeList.get(i);
                    Map<String, String> emptyObj = new HashMap<>();
                    emptyObj.put("callnum", "0");
                    emptyObj.put("download", "0");
                    emptyObj.put("categorynum", "0");
                    emptyObj.put("usernum", "0");
                    emptyObj.put("d_datetime", timeStr);
                    newList.add(emptyObj);
                }
                map.put(usertype,newList);
            }

        }
        return map;
    }

    public Map getDepUserClassDataList(List<Map<String, Object>> depUserClassDataList){
        Map<String,List> map = new HashMap<>();
        //List list = new ArrayList();
        if(depUserClassDataList!=null&&depUserClassDataList.size()>0){
            for (Map<String, Object> entry : depUserClassDataList) {
                String callnum = entry.get("CALLNUM").toString();
                String download = entry.get("DOWNLOAD").toString();
                String categorynum = entry.get("CATEGORYNUM").toString();
                String usernum = entry.get("USERNUM").toString();
                String data_class_id = entry.get("DATA_CLASS_ID").toString();
                String data_class_name = entry.get("DATA_CLASS_NAME").toString();

                String group_id = entry.get("GROUP_ID").toString();

                Map<String, String> obj = new HashMap<>();
                obj.put("callnum", callnum);
                obj.put("download", download);
                obj.put("categorynum", categorynum);
                obj.put("usernum", usernum);
                obj.put("data_class_id", data_class_id);
                obj.put("data_class_name", data_class_name);

                if(map.containsKey(group_id)){
                    map.get(group_id).add(obj);
                }else{
                    List list = new ArrayList();
                    list.add(obj);
                    map.put(group_id,list);
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

    public List<Map<String, Object>> userunionsearch(Map map){
        String usertype = (String)map.get("usertype");
        String departemnt = (String)map.get("department");
        String tokenDepartment = (String)map.get("tokenDepartment");
        String system = (String)map.get("system");
        String username = (String)map.get("username");
        String dataname = (String)map.get("dataname");

        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");

        Integer minCategorynum = map.get("minCategorynum")==null?null:(Integer)map.get("minCategorynum");
        Integer maxCategorynum = map.get("maxCategorynum")==null?null:(Integer)map.get("maxCategorynum");
        Long minCallnum = map.get("minCallnum")==null?null:(Long)map.get("minCallnum");
        Long maxCallnum = map.get("maxCallnum")==null?null:(Long)map.get("maxCallnum");
        Long minDownload = map.get("minDownload")==null?null:(Long)map.get("minDownload");
        Long maxDownload = map.get("maxDownload")==null?null:(Long)map.get("maxDownload");

        List<Map<String, Object>> list = mapper.userunionsearch(usertype, departemnt, tokenDepartment, system, username,dataname,
                startTime, endTime,minCategorynum,maxCategorynum,minCallnum,maxCallnum,minDownload,maxDownload);
        return list;
    }

    public void checkuser(String userid,String checkdesc){
        mapper.checkuser(userid, checkdesc);
    }

    public void uncheckuser(String userid,String checkdesc){
        mapper.uncheckuser(userid, checkdesc);
    }

}
