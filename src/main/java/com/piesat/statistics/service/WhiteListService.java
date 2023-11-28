package com.piesat.statistics.service;

import com.piesat.statistics.bean.WhiteList;
import com.piesat.statistics.mapper.WhiteListMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WhiteListService {
    @Resource
    WhiteListMapper whiteListMapper;

    public void save(WhiteList whiteList){
        whiteListMapper.saveWhiteList(whiteList);
    }

    public List<String> getWhiteList(){
        return whiteListMapper.getWhiteList();
    }
}
