//
//
package org.zka.model.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户查询实体
 */
@Data
public class SysLoginLogQueryVo implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private String username;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTimeBegin;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTimeEnd;

}

