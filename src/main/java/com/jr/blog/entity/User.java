package com.jr.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_user")
public class User {
    /**
     *主键
     */
    @TableId(type = IdType.AUTO)
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
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 密码
     */
    private String password;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
