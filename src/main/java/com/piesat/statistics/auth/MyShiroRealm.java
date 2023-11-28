package com.piesat.statistics.auth;

import com.github.pagehelper.PageHelper;
import com.piesat.statistics.bean.SysSession;
import com.piesat.statistics.controller.LoginController;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by admin on 2021/9/8.
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        return authenticationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String authToken = (String) token.getPrincipal();
        //1. 根据accessToken，查询用户信息
        SysSession sysSession = (SysSession)LoginController.concurrentHashMap.get(authToken);

        //2. token失效
        if (sysSession == null || sysSession.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        String username = sysSession.getUsername();
        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if (username == null) {
            throw new UnknownAccountException("用户不存在!");
        }
        //5. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, authToken, this.getName());
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthToken;
    }

    @Configuration
    public static class MybatisConfig {

        @Bean
        public PageHelper pageHelper() {
            PageHelper pageHelper = new PageHelper();
            Properties p = new Properties();
            p.setProperty("dialect", "Mysql");
            p.setProperty("offsetAsPageNum", "true");
            p.setProperty("rowBoundsWithCount", "true");
            pageHelper.setProperties(p);
            return pageHelper;
        }

    }
}
