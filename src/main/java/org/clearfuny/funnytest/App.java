package org.clearfuny.funnytest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Hello world!
 *
 **/
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages={"org.clearfuny.funnytest"})
public class App implements CommandLineRunner
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("启动成功");
    }

}