package org.zka.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.zka.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *  电影信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_movie")
@ApiModel(value="SysMovie对象", description="")
public class SysMovie extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "电影名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "分类id")
    @TableField("cid")
    private Integer cid;

    @ApiModelProperty(value = "内容描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "关键字，用来搜索")
    @TableField("keyword")
    private String keyword;

    @ApiModelProperty(value = "导演姓名")
    @TableField("director")
    private String director;

    @ApiModelProperty(value = "主演")
    @TableField("actor")
    private String actor;

    @ApiModelProperty(value = "缩略图")
    @TableField("image")
    private String image;

    @ApiModelProperty(value = "视频类型:1.免费视频  2.付费视频")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "播放连接")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "播放id,阿里云提供")
    @TableField("play_id")
    private String playId;
}
