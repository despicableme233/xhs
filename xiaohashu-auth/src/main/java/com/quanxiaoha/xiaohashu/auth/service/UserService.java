package com.quanxiaoha.xiaohashu.auth.service;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UserLoginReqVO;
import org.springframework.stereotype.Service;


public interface UserService {


    Response<String> loginAndResister(UserLoginReqVO userLoginReqVO);
}
