package com.krista.spring.boot.dto.user;

import com.krista.spring.boot.dto.common.PageVo;
import lombok.Data;

/**
 * @Auther: dw_wanghonghong
 * @Date: 2018/8/10 15:11
 * @Description: 用户列表查询Vo
 */
@Data
public class UserPageVo extends PageVo {
    private String userName;
}
