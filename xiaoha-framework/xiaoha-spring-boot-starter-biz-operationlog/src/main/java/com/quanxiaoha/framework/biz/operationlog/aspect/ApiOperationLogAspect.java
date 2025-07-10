package com.quanxiaoha.framework.biz.operationlog.aspect;


import com.quanxiaoha.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
@Slf4j
public class ApiOperationLogAspect {

    //    切点表达式，对添加@ApiOperationLog的方法
    @Pointcut("@annotation(com.quanxiaoha.framework.biz.operationlog.aspect.ApiOperationLog)")
    public void apiOperationLog() {

    }

    @Around("apiOperationLog()")
    Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
//        方法名
        String methodName = joinPoint.getSignature().getName();
//        类名
        String className = joinPoint.getTarget().getClass().getName();
//        请求参数
        Object[] args = joinPoint.getArgs();
//        将参数转化为JSON字符串
        String argsJsonStr = JsonUtils.toJsonString(args);
//        注解备注信息
        String description = getApiOperationLogDescription(joinPoint);
        // 打印请求相关参数
        log.info("====== 请求开始: [{}], 入参: {}, 请求类: {}, 请求方法: {} =================================== ",
                description, argsJsonStr, className, methodName);
        Object result = joinPoint.proceed(args);
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        // 打印出参等相关信息
        log.info("====== 请求结束: [{}], 耗时: {}ms, 出参: {} =================================== ",
                description, executeTime, JsonUtils.toJsonString(result));
        return result;

    }

    /**
     * 获取详细描述信息
     *
     * @param joinPoint
     * @return 描述信息
     */
    private String getApiOperationLogDescription(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        获得反射类
        Method method = methodSignature.getMethod();
//        反射获得注解
        ApiOperationLog annotation = method.getAnnotation(ApiOperationLog.class);
//        判空
        if (annotation != null) {
            return annotation.description();
        }
        return null;
    }
}
