package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class BaseEntity implements Serializable {

  @TableId
  @ApiModelProperty("主键id")
  private String id;

  @ApiModelProperty("是否删除（0否，1是）")
  @TableField(fill = FieldFill.INSERT)
  private Integer isDeleted;

  @ApiModelProperty("创建时间")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @ApiModelProperty("更新时间")
  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;

  @ApiModelProperty("创建者")
  private String createBy;

  @ApiModelProperty("修改者")
  private String updateBy;

}
