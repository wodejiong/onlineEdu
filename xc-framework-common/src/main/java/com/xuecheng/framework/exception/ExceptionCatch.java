package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常捕获类
 */
@ControllerAdvice//控制器增强
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //使用EXCEPTIONS来存放异常类及ResultCode
    private static  ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    private static  ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> BUILDER = ImmutableMap.builder();

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customeException(CustomException customException) {
        LOGGER.error("catch Exception:{}", customException.getMessage(), customException);
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception) {
        LOGGER.error("catch Exception:{}", exception.getMessage(), exception);
        if (EXCEPTIONS == null) {
            EXCEPTIONS = BUILDER.build();
        }
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        ResponseResult responseResult = null;
        if (resultCode != null) {
            responseResult = new ResponseResult(resultCode);
        }else{
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;
    }

    static {
        BUILDER.put(HttpMessageNotReadableException.class, CommonCode.INVALIDIDATE_PARAM);
    }
}
