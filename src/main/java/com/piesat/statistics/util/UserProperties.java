package com.piesat.statistics.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "login-user")
@Component
public class UserProperties {

    private List<AllowUser> userlist;

    public List<AllowUser> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<AllowUser> userlist) {
        this.userlist = userlist;
    }
}
