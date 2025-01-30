package com.ohgiraffers.ukki.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.apache.ibatis.type.JdbcType;

@Configuration
@MapperScan(basePackages = "com.ohgiraffers.ukki", annotationClass = Mapper.class)
public class MybatisConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.setCallSettersOnNulls(true);
            configuration.setJdbcTypeForNull(JdbcType.NULL);
            configuration.setMapUnderscoreToCamelCase(true);
        };
    }
}
