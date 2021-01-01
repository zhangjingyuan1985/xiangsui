package com.sutpc.transpaas.algoserver.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * Mybatis自动填充
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    this.setFieldValByName("isDeleted", 0, metaObject);
    this.setFieldValByName("createTime", new Date(),metaObject);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    this.setFieldValByName("updateTime", new Date(),metaObject);
  }
}
