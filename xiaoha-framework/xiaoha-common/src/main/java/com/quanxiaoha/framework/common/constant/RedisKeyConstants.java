package com.quanxiaoha.framework.common.constant;

public class RedisKeyConstants {
    /**
     * 验证码 KEY 前缀
     */
    public static final String VERIFICATION_CODE_KEY_PREFIX = "verification_code:";

    public static final String XIAOHASHU_ID_AUTOINCREMENT_KEY = "xiaohashu_id_autoincrement";

    /**
     * 用户角色数据KEY前缀
     */
    private static final String USER_ROLE_KEY_PREFIX = "user_roles:";

    /**
     * 构建验证码
     * @param phone
     * @return
     */
    public static String buildVerificationCodeKey(String phone) {
        return VERIFICATION_CODE_KEY_PREFIX + phone;
    }

    /**
     * 构建用户-角色Key
     *
     */
    public static String buildUserRoleKey(String userId) {
        return USER_ROLE_KEY_PREFIX + userId;
    }

}

