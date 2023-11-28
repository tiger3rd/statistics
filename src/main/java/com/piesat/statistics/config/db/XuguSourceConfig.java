package com.piesat.statistics.config.db;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.piesat.statistics.mapper", sqlSessionFactoryRef = "xuguSqlSessionFactory")
public class XuguSourceConfig {

    @Bean(name = "xuguDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.xugu")
    public DataSource xuguDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "xuguSqlSessionFactory")
    @Primary
    public SqlSessionFactory xuguSqlSessionFactory(@Qualifier("xuguDataSource")DataSource xuguDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        factoryBean.setDataSource(xuguDataSource);

        return factoryBean.getObject();
    }

    @Bean(name = "xuguTransactionManager")
    @Primary
    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("xuguDataSource")DataSource xuguDataSource) {
        return new DataSourceTransactionManager(xuguDataSource);
    }

}