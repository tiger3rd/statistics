package com.piesat.statistics.auth;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by admin on 2021/9/8.
 */
public class AuthToken implements AuthenticationToken {

    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
