package com.sutpc.transpaas.algoserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.sutpc.transpaas.algoserver"})
@MapperScan({"com.sutpc.transpaas.algoserver.dao"})
@EnableScheduling
@EnableDiscoveryClient
@RestController
public class AlgoServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AlgoServerApplication.class, args);
  }

}
