package com.supply.service.impl;

import com.supply.context.BaseContext;
import com.supply.entity.User;
import com.supply.mapper.UserLoginMapper;
import com.supply.service.MedicalService;
import com.supply.vo.UserInformationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalServiceImpl implements MedicalService {

    private final UserLoginMapper userLoginMapper;

    @Override
    public UserInformationVO getInformation() {
        User user = userLoginMapper.getUserInformationById(BaseContext.getCurrentId());
        UserInformationVO userInformationVO = new UserInformationVO();
        BeanUtils.copyProperties(user,userInformationVO);
        log.info("当前登录的管理员信息：{}",userInformationVO);
        return userInformationVO;
    }
}
