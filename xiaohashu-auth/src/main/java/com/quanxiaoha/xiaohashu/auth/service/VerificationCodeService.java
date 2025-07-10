package com.quanxiaoha.xiaohashu.auth.service;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.model.vo.verificationcode.SendVerificationCodeReqVO;


public interface VerificationCodeService {

    /**
     * 输入手机号后返回验证码
     * @param reqVO 入参
     * @return
     */
    Response<?> sendVerificationCode(SendVerificationCodeReqVO reqVO);
}
