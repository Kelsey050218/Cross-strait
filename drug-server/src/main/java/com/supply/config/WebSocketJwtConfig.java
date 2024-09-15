package com.supply.config;

import com.supply.constant.MessageConstant;
import com.supply.context.WebSocketContext;
import com.supply.exception.WebSocketJwtErrorException;
import com.supply.properties.JwtProperties;
import com.supply.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@Slf4j
public class WebSocketJwtConfig extends ServerEndpointConfig.Configurator {

    @Autowired
    private JwtProperties jwtProperties;

    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        List<String> authHeaders = request.getHeaders().get(jwtProperties.getTokenName());
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String token = authHeaders.get(0).replace("Bearer ", "");
            boolean validToken = validateJwtToken(token);
            if (!validToken) {
                throw new WebSocketJwtErrorException(MessageConstant.JWT_PARSE_FAILED);
            }
        } else {
            throw new WebSocketJwtErrorException(MessageConstant.JWT_LOST);
        }
    }

    private boolean validateJwtToken(String token) {
        try {
            log.info("JWT校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long id = Long.valueOf(claims.get("id").toString());
            log.info("当前用户id：{}", id);
            WebSocketContext.setCurrentId(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
