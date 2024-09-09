package com.supply.handler;

import com.supply.constant.MessageConstant;
import com.supply.exception.AccountStatusException;
import com.supply.exception.SQLPasswordException;
import com.supply.exception.VerificationCodeErrorException;
import com.supply.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


//    @ExceptionHandler
//    public Result SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex){
//        log.error("异常信息：{}", ex.getMessage());
//        return Result.error(MessageConstant.ACCOUNT_DUPLICATE);
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> SQLPasswordExceptionHandler(SQLPasswordException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> accountStatusExceptionHandler(AccountStatusException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(VerificationCodeErrorException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Object> verificationCodeErrorExceptionHandler(VerificationCodeErrorException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(MessageConstant.INFORMATION_STYLE_ERROR);
    }
}
