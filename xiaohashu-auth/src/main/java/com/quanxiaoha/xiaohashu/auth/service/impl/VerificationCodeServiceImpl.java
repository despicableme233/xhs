package com.quanxiaoha.xiaohashu.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.quanxiaoha.framework.common.constant.RedisKeyConstants;
import com.quanxiaoha.framework.common.enums.ResponseCodeEnum;
import com.quanxiaoha.framework.common.exception.BizException;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.model.vo.verificationcode.SendVerificationCodeReqVO;
import com.quanxiaoha.xiaohashu.auth.service.VerificationCodeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Response<?> sendVerificationCode(SendVerificationCodeReqVO reqVO) {
//        get the phone of the user
        String phone = reqVO.getPhone();
//        construct the redis key
        String key = RedisKeyConstants.VERIFICATION_CODE_KEY_PREFIX + phone;
        boolean isExist = redisTemplate.hasKey(key);
        if (isExist) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

//        六位数的验证码
        String code = RandomUtil.randomString(6);
//       todo: 调用短信发送服务
        log.info("==> 手机号: {}, 已发送验证码：【{}】", phone, code);
        redisTemplate.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
        return Response.success();
    }
}
