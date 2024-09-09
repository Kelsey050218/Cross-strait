package com.supply.interceptor;


import com.supply.context.BaseContext;
import com.supply.properties.JwtProperties;
import com.supply.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());

        //2、校验令牌
        try {
            log.info("JWT校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long id = Long.valueOf(claims.get("id").toString());
            log.info("当前用户id：{}", id);
            BaseContext.setCurrentId(id);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401
            response.setStatus(401);
            return false;
        }
    }
}
