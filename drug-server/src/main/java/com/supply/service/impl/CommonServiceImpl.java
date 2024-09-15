package com.supply.service.impl;


import com.supply.context.BaseContext;
import com.supply.dto.UserInformationDTO;
import com.supply.entity.Blacklist;
import com.supply.entity.User;
import com.supply.mapper.CommonMapper;
import com.supply.mapper.UserLoginMapper;
import com.supply.service.CommonService;
import com.supply.vo.BlacklistVO;
import com.supply.vo.UserInformationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final CommonMapper commonMapper;

    private final UserLoginMapper userLoginMapper;

    /**
     * 查询黑名单
     *
     * @return 黑名单信息
     */
    public List<BlacklistVO> getBlacklistInformation() {
        List<Blacklist> blacklists = commonMapper.getBlacklistInformation(BaseContext.getCurrentId());
        List<BlacklistVO> list = new ArrayList<>();
        for (Blacklist blacklist : blacklists) {
            BlacklistVO blacklistVO = new BlacklistVO();
            BeanUtils.copyProperties(blacklist, blacklistVO);
            User blackUserInformation = userLoginMapper.getUserInformationById(blacklist.getBlackUserId());
            blacklistVO.setUsername(blackUserInformation.getUsername());
            blacklistVO.setImage(blackUserInformation.getImage());
            blacklistVO.setFirmName(blackUserInformation.getFirmName());
            list.add(blacklistVO);
        }
        return list;
    }

    /**
     * 根据黑名单id删除黑名单信息
     *
     * @param id 黑名单id
     */
    public void removeBlacklist(Long id) {
        commonMapper.removeBlacklist(id);
    }

    /**
     * 增加黑名单
     *
     * @param id 拉黑的用户id
     */
    public void addBlacklist(Long id) {
        commonMapper.addBlacklist(BaseContext.getCurrentId(),id, LocalDateTime.now());
    }

    /**
     *
     */
    public void report() {

    }


    public UserInformationVO getModificationInformation() {
        User user = userLoginMapper.getUserInformationById(BaseContext.getCurrentId());
        UserInformationVO userInformationVO = new UserInformationVO();
        BeanUtils.copyProperties(user, userInformationVO);
        return userInformationVO;
    }


    public void updateUserInformation(UserInformationDTO userInformationDTO) {
        log.info("修改用户个人信息：{}", userInformationDTO);
        new User();
        User user = User.builder()
                .id(BaseContext.getCurrentId())
                .firmName(userInformationDTO.getFirmName())
                .email(userInformationDTO.getEmail())
                .telephone(userInformationDTO.getTelephone())
                .updateTime(LocalDateTime.now())
                .build();
        commonMapper.updateUserInformation(user);
    }
}