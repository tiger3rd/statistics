package com.piesat.statistics.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by admin on 2021/9/9.
 */
@Component
@ConfigurationProperties(prefix = "permission-config")
public class PropertiesUtil {
    private List<LinkedHashMap<String,String>> perms;

    public List<LinkedHashMap<String, String>> getPerms() {
        return perms;
    }

    public void setPerms(List<LinkedHashMap<String, String>> perms) {
        this.perms = perms;
    }
}
