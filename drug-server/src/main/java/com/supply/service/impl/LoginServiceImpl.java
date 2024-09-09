package com.supply.service.impl;

import com.supply.constant.JwtClaimsConstant;
import com.supply.constant.MessageConstant;
import com.supply.dto.UserInformationDTO;
import com.supply.dto.UserLoginDTO;
import com.supply.entity.IdentityAuthentication;
import com.supply.exception.AccountStatusException;
import com.supply.exception.SQLPasswordException;
import com.supply.exception.VerificationCodeErrorException;
import com.supply.mapper.UserLoginMapper;
import com.supply.properties.JwtProperties;
import com.supply.service.LoginService;
import com.supply.utils.EmailUtil;
import com.supply.utils.JwtUtil;
import com.supply.entity.User;
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
public class LoginServiceImpl implements LoginService {

    private final UserLoginMapper userLoginMapper;

    private final EmailUtil emailUtil;

    private final JwtProperties jwtProperties;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 用户预注册
     *
     * @param userInformationDTO 用户注册信息
     */
    public void register(UserInformationDTO userInformationDTO) {
        String code = redisTemplate.opsForValue().get(userInformationDTO.getEmail());
        if (!Objects.equals(code, userInformationDTO.getVerifyCode())) {
            throw new VerificationCodeErrorException(MessageConstant.VERIFICATION_CODE_ERROR);
        }
        //进行User实体类的封装
        User user = new User();
        BeanUtils.copyProperties(userInformationDTO, user);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        if (userInformationDTO.getIdentity() == 1) {
            user.setWorkType(1);
            user.setImage("https://pro11.oss-cn-hangzhou.aliyuncs.com/DALL%C2%B7E%202024-09-01%2021.42.24%20-%20A%20cartoon%20illustration%20of%20a%20healthcare%20professional.%20The%20character%20is%20wearing%20a%20white%20lab%20coat%2C%20a%20stethoscope%20around%20their%20neck%2C%20and%20a%20friendly%20smile.webp");
        } else {
            user.setWorkType(2);
            user.setImage("https://pro11.oss-cn-hangzhou.aliyuncs.com/DALL%C2%B7E%202024-09-01%2021.50.49%20-%20A%20cartoon%20illustration%20of%20a%20pharmaceutical%20supplier.%20The%20character%20is%20wearing%20a%20white%20lab%20coat%2C%20glasses%2C%20and%20is%20holding%20a%20box%20labeled%20%27Medicines.%27%20The.webp");
        }
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        log.info("用户信息：{}", user);
        userLoginMapper.register(user);
        //将验证信息放入管理员数据库中
        List<String> verificationImages = userInformationDTO.getVerificationImages();
        String images = String.join(",", verificationImages);
        IdentityAuthentication identityAuthentication = IdentityAuthentication.builder()
                .applicationTime(LocalDateTime.now())
                .images(images)
                .IDNumber(user.getIDNumber())
                .userId(user.getId())
                .workType(user.getWorkType())
                .build();
        userLoginMapper.sendVerificationMessage(identityAuthentication);
    }

    /**
     * 邮箱验证
     *
     * @param email 注册的邮箱
     */
    public void sendCode(String email) {
        String code = emailUtil.codeMail(email).toString();
        log.info("用户{}的验证码为：{}", email, code);
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    /**
     * 重置密码
     *
     * @param userInformationDTO 用户信息
     */
    public void resetPassword(UserInformationDTO userInformationDTO) {
        String code = redisTemplate.opsForValue().get(userInformationDTO.getEmail());
        if (!Objects.equals(code, userInformationDTO.getVerifyCode())) {
            throw new VerificationCodeErrorException(MessageConstant.VERIFICATION_CODE_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userInformationDTO, user);
        log.info("用户更改的信息：{}", user);
        userLoginMapper.resetPassword(user);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     */
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        log.info("当前登录用户：{}", userLoginDTO.getUsernameOrEmail());
        User user;
        if (!userLoginDTO.getUsernameOrEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")) {
            user = User.builder()
                    .username(userLoginDTO.getUsernameOrEmail())
                    .password(DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes()))
                    .build();
        } else {
            user = User.builder()
                    .email(userLoginDTO.getUsernameOrEmail())
                    .password(DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes()))
                    .build();
        }
        if (validateLogin(userLoginDTO.getPassword())) {
            User u = userLoginMapper.login(user);
            if (u != null && u.getAccountStatus() == 1) {
                UserLoginVO userLoginVO = UserLoginVO.builder()
                        .id(u.getId())
                        .username(u.getUsername()).build();
                Map<String, Object> claims = new HashMap<>();
                claims.put(JwtClaimsConstant.ID, userLoginVO.getId());
                String jwt = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
                userLoginVO.setToken(jwt);
                return userLoginVO;
            } else if (u != null && u.getAccountStatus() == 2) {
                throw new AccountStatusException(MessageConstant.ACCOUNT_LOCKED);
            } else if (u != null && u.getAccountStatus() == 3) {
                throw new AccountStatusException(MessageConstant.ACCOUNT_PREPARING);
            } else if (u != null && u.getAccountStatus() == 4) {
                throw new AccountStatusException(MessageConstant.ACCOUNT_PREPARE_FAILED);
            } else {
                return null;
            }
        } else {
            log.info("可能存在SQL注入风险：{}", userLoginDTO.getPassword());
            throw new SQLPasswordException(MessageConstant.DANGEROUS_PASSWORD);
        }
    }

    //以下两个方法用于检查SQL注入风险
    public boolean containsSqlInjection(String password) {
        String regex = "('.+--)|(--)|(%7C)|(/\\*(?:.|[\\n\\r])*?\\*/)|(;)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public boolean validateLogin(String password) {
        return !containsSqlInjection(password);
    }

}
