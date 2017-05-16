package com.pduleba;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.pduleba.http.boot.HttpBoot;

@Configuration
@ComponentScan(basePackageClasses = {HttpBoot.class})
@PropertySource(
        value={"classpath:configuration.properties"})
public class SpringContext {

}
