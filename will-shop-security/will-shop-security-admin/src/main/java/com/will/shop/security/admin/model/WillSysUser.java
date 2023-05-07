package com.will.shop.security.admin.model;

import lombok.Data;

import java.util.Set;

/**
 * 用户详细信息
 *
 * @author will
 */
@Data
public class WillSysUser {

    /**
     * 用户ID
     */
    private Long userId;

    private boolean enabled;

    private Set<String> authorities;

    private String username;

    private Long shopId;
}
