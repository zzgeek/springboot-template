package zzgeek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication()
public class ZzGeekBoot {

    public static void main(String[] args) {
        SpringApplication.run(ZzGeekBoot.class, args);
    }


}
