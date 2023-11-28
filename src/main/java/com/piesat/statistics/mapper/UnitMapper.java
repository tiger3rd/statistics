package com.piesat.statistics.mapper;

import com.piesat.statistics.bean.DepartmentBean;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UnitMapper {
    @Select({"SELECT * FROM API_DEPARTMENT_INFO_NEW"})
    List<DepartmentBean> getAll();
}
