package com.sutpc.transpaas.algoserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FtpPool {

  FtpClientFactory factory;
  private final GenericObjectPool<FTPClient> internalPool;

  /**
   * 初始化连接池
   *
   * @param factory
   */
  public FtpPool(@Autowired FtpClientFactory factory) {
    this.factory = factory;
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxTotal(20);
    poolConfig.setMinIdle(1);
    poolConfig.setMaxIdle(8);
    poolConfig.setMaxWaitMillis(3000);

    this.internalPool = new GenericObjectPool<FTPClient>(factory, poolConfig);
  }

  /**
   * 从连接池中取连接
   *
   * @return
   */
  public FTPClient getFTPClient() {
    try {
      return internalPool.borrowObject();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将链接归还到连接池
   *
   * @param ftpClient
   */
  public void returnFTPClient(FTPClient ftpClient) {
    try {
      internalPool.returnObject(ftpClient);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 销毁池子
   */
  public void destroy() {
    try {
      internalPool.close();
    } catch (Exception e) {
      log.error("销毁过程异常：", e);
    }
  }
}
