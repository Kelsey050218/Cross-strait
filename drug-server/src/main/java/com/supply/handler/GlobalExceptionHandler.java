package com.supply.handler;

import com.supply.constant.MessageConstant;
import com.supply.exception.*;
import com.supply.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(MessageConstant.ACCOUNT_DUPLICATE);
    }

    @ExceptionHandler(SQLPasswordException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> SQLPasswordExceptionHandler(SQLPasswordException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
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

    @ExceptionHandler(WebSocketJwtErrorException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Object> webSocketJwtErrorExceptionExceptionHandler(WebSocketJwtErrorException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleValidationExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(MessageConstant.INFORMATION_STYLE_ERROR);
    }

    @ExceptionHandler(WebSocketSendException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public Result<Object> webSocketSendExceptionExceptionHandler(WebSocketSendException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(MessageConstant.INFORMATION_STYLE_ERROR);
    }
}
