package org.zka;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@Slf4j
@SpringBootApplication
@MapperScan("org.zka.mapper")
public class ServiceApp {
    public static void main( String[] args ) {
        SpringApplication.run(ServiceApp.class,args);
        log.info("项目启动成功！");
    }
}
