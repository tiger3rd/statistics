package com.piesat.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piesat.statistics.bean.WhiteList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WhiteListMapper  extends BaseMapper<WhiteList> {

    @Insert({"insert into MONITOR_USER_CHECK_INFO (user_id,invalid,createTime,createBy) VALUES (#{user_id},#{invalid},#{createTime},#{createBy}) "})
    void saveWhiteList(WhiteList user);

    @Select({"select USER_ID from MONITOR_WHITE_LIST WHERE INVALID=0 "})
    List<String> getWhiteList();

}
