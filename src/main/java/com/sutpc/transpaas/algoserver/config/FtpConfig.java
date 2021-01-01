package com.sutpc.transpaas.algoserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * FTP参数配置
 */
@Data
@Component
public class FtpConfig {

  @Value("${ftp.host}")
  private String host;

  @Value("${ftp.port}")
  private int port;

  @Value("${ftp.username}")
  private String username;

  @Value("${ftp.password}")
  private String password;

  @Value("${ftp.passiveMode}")
  private Boolean passiveMode;

}
