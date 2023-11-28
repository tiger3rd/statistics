package com.piesat.statistics.service;

import com.piesat.statistics.mapper.UnitMapper;
import com.piesat.statistics.bean.DepartmentBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UnitService {

    @Resource
    UnitMapper unitMapper;

    public Map<String, Object> getDepartmentMap() {
        List<DepartmentBean> list = unitMapper.getAll();

        Map<String, Object> ret = new HashMap<>();

        for (DepartmentBean unit : list) {
            ret.put(unit.getDepartment_id(), unit.getDepartment_name());
        }

        return ret;
    }

    public Map<String, Object> getDepartmentLevelMap() {
        List<DepartmentBean> list = unitMapper.getAll();

        Map<String, Object> ret = new HashMap<>();

        for (DepartmentBean unit : list) {
            ret.put(unit.getDepartment_id(), unit.getIs_country());
        }

        return ret;
    }

    public Map<String, DepartmentBean> getDepartmentBeanMap() {
        List<DepartmentBean> list = unitMapper.getAll();

        Map<String, DepartmentBean> ret = new HashMap<>();

        for (DepartmentBean unit : list) {
            ret.put(unit.getDepartment_id(), unit);
        }

        return ret;
    }
}
