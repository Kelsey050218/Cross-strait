package com.supply.service.impl;

import com.supply.constant.JwtClaimsConstant;
import com.supply.constant.MessageConstant;
import com.supply.dto.UserInformationDTO;
import com.supply.dto.UserLoginDTO;
import com.supply.entity.IdentityAuthentication;
import com.supply.entity.User;
import com.supply.exception.AccountStatusException;
import com.supply.exception.SQLPasswordException;
import com.supply.exception.VerificationCodeErrorException;
import com.supply.mapper.SupplyMapper;
import com.supply.mapper.UserLoginMapper;
import com.supply.properties.JwtProperties;
import com.supply.service.LoginService;
import com.supply.service.SupplyService;
import com.supply.utils.EmailUtil;
import com.supply.utils.JwtUtil;
import com.supply.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

    private final SupplyMapper supplyMapper;

}
