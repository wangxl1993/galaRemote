package com.wxl.gala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class}) //不需要连接数据库
@SpringBootApplication
public class GalaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalaApplication.class, args);
    }

}
