package org.zka.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import org.zka.model.entity.SysMovie;
import lombok.Data;

@Data // 自动生成getter、setter、toString等
public class SysMovieVo extends SysMovie {
    @TableField(value = "category_name")
    private String categoryName;
}
