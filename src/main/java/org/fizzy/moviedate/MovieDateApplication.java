package org.fizzy.moviedate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.fizzy.moviedate.mapper")
public class MovieDateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieDateApplication.class, args);
    }

}
