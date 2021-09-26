package com.ding.dingitems.util.exception;

import com.ding.dingitems.sysbiz.dto.ApiReturnResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@ControllerAdvice
public class BizException {

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ApiReturnResult bizExc(BusinessException e){
        return ApiReturnResult.fail(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiReturnResult bizExc(Exception e){
        log.error("系统异常",e);
        return ApiReturnResult.fail("服务器内部错误",e);
    }

    /**
     * 校验错误拦截处理
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiReturnResult validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        String message = "";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                log.error("Data check failure : object{" + fieldError.getObjectName() + "},field{" + fieldError.getField() +
                        "},errorMessage{" + fieldError.getDefaultMessage() + "}");

            });
            if (!errors.isEmpty()) {
                FieldError fieldError = (FieldError) errors.get(0);
                message = fieldError.getDefaultMessage();
            }
        }
        return ApiReturnResult.fail("".equals(message) ? "请填写正确信息" : message);
    }

    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageConversionException.class)
    public ApiReturnResult parameterTypeException(HttpMessageConversionException exception) {
        log.error(exception.getCause().getLocalizedMessage());
        return ApiReturnResult.fail("类型转换错误");

    }

}
