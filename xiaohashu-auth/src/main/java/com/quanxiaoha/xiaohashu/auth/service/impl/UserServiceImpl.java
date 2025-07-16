package com.quanxiaoha.xiaohashu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Preconditions;
import com.quanxiaoha.framework.common.constant.RedisKeyConstants;
import com.quanxiaoha.framework.common.constant.RoleConstants;
import com.quanxiaoha.framework.common.enums.DataStatusEnum;
import com.quanxiaoha.framework.common.enums.DeletedEnum;
import com.quanxiaoha.framework.common.enums.LoginTypeEnum;
import com.quanxiaoha.framework.common.enums.ResponseCodeEnum;
import com.quanxiaoha.framework.common.exception.BizException;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.framework.common.util.JsonUtils;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.RoleDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserRoleDO;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.RoleDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserRoleDOMapper;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UserLoginReqVO;
import com.quanxiaoha.xiaohashu.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserRoleDOMapper userRoleDOMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private RoleDOMapper roleDOMapper;

//    @Resource
//    private PasswordEncoder passwordEncoder;


    /**
     * 登录自动注册用户
     *
     * @param userLoginReqVO 用户登录组测视图
     * @return
     */
    @Override
    public Response<String> loginAndResister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        Integer type = userLoginReqVO.getType();

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);
        Long userId = null;
        switch (loginTypeEnum) {
            case VERIFICATION_CODE:
                String code = userLoginReqVO.getCode();
                //判空
                Preconditions.checkArgument(StrUtil.isNotBlank(code), "验证码不能为空");
                //构建验证码
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                //获取在redis中的验证码
                String verificationCode = (String) redisTemplate.opsForValue().get(key);
                //验证缓存中的验证码和用户发的验证码的一致性
                if (StrUtil.equals(verificationCode, code)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }
                //通过手机号查询记录
                UserDO userDO = userDOMapper.selectByPhone(phone);

                log.info("phone：{}，userDO：{}", phone, userDO);

                //判断是否注册
                if (Objects.isNull(userDO)) {
                    //若此用户未注册，则自行注册；
                    userId = registerUser(phone);
                } else {
                    //已注册，获取用户ID(小哈书Id？)
                    userId = userDO.getId();
                }
                break;
            case PASSWORD:
                String password = userLoginReqVO.getPassword();
                //根据手机号查询
                UserDO userDO1 = userDOMapper.selectByPhone(phone);
                //判断手机号是否注册
                if (Objects.isNull(userDO1)) {
                    throw new BizException(ResponseCodeEnum.PHONE_NOT_REGISTER);
                }
                //密文密码
                String encryptedPassword = userDO1.getPassword();
//                if (!passwordEncoder.matches(password, encryptedPassword)) {
//                    throw new BizException(ResponseCodeEnum.PHONE_OR_PASSWORD_ERROR);
//                }
                userId = userDO1.getId();
                break;
            default:
                break;
        }
        log.info("已注册用户的id：{}", userId);
        //SaToken登录用户，并返回token令牌
        StpUtil.login(userId);
        //获取Token令牌
        SaTokenInfo tokInfo = StpUtil.getTokenInfo();
        return Response.success(tokInfo.tokenValue);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
                    try {
                        Long xiaoHaShuId = redisTemplate.opsForValue().increment(RedisKeyConstants.XIAOHASHU_ID_AUTOINCREMENT_KEY);
                        //给用户分配一个角色（可分配默认角色）
                        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);
                        //将该用户角色id存入list列表中，设置初始容量为1，避免扩容开销
                        List<String> roles = new ArrayList<>(1);
                        roles.add(roleDO.getRoleKey());
                        UserDO userDO = UserDO.builder()
                                .phone(phone)
                                .xiaohashuId(String.valueOf(xiaoHaShuId))
                                .nickname("小红书用户" + xiaoHaShuId)
                                .status(DataStatusEnum.ENABLE.getCode())
                                .createTime(LocalDateTime.now())
                                .updateTime(LocalDateTime.now())
                                .isDeleted(DeletedEnum.NORMAL.getCode())
                                .build();
                        //入库
                        userDOMapper.insert(userDO);

                        UserDO registeredUserDO = userDOMapper.selectByPhone(phone);
                        log.info("已注册用户的id：{}", registeredUserDO.getId());
                        Long registeredUserId = registeredUserDO.getId();
//                        Long userId = userDO.getId();
                        UserRoleDO userRoleDO = UserRoleDO.builder()
                                .userId(registeredUserId)
                                .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                                .createTime(LocalDateTime.now())
                                .updateTime(LocalDateTime.now())
                                .isDeleted(DeletedEnum.NORMAL.getCode())
                                .build();
                        userRoleDOMapper.insert(userRoleDO);
//        将该用户的角色ID存入Redis中
                        List<Long> rolesList = new ArrayList<>();
                        rolesList.add(RoleConstants.COMMON_USER_ROLE_ID);
                        String userRolesKey = RedisKeyConstants.buildUserRoleKey(phone);
                        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));
                        return registeredUserId;
                    } catch (Exception e) {
                        //回滚事务
                        status.setRollbackOnly();
                        return null;
                    }
                }
        );
    }
}
