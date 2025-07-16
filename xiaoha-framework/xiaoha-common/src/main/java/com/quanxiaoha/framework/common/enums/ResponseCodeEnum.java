package com.quanxiaoha.framework.common.enums;

import com.quanxiaoha.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {
    SYSTEM_ERROR("AUTH-10000", "修复中..."),

    PARAM_NOT_VALID("AUTH-10001", "参数错误"),

    VERIFICATION_CODE_SEND_FREQUENTLY("AUTH-10002", "验证码请求过于频繁"),
    //业务异常状态码
    VERIFICATION_CODE_ERROR("AUTH-20001", "验证码错误"),
    PHONE_OR_PASSWORD_ERROR("AUTH-10003", "手机号或者密码错误"),

    PHONE_NOT_REGISTER("AUTH-10004", "手机号未注册"),
    ;

    //    异常码
    private final String errorCode;
    //    错误信息
    private final String errorMessage;
}
