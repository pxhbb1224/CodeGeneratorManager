package com.cmbchina.code_generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.cmbchina.code_generator.mapper")
@SpringBootApplication
public class CodeGeneratorManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeGeneratorManagerApplication.class, args);
    }

}
