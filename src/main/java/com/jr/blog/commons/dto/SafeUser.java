package com.jr.blog.commons.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

@Data
public class SafeUser implements Serializable {
    /**
     *主键
     */
    private Integer userId;

    /**
     *用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 转台 (0为正常 1为禁用)
     */
    private Integer status;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     *角色 (0为普通用户 1为管理员)
     */
    private Integer role;

}
