package com.supply.service;



import com.supply.dto.UserInformationDTO;
import com.supply.vo.BlacklistVO;
import com.supply.vo.UserInformationVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommonService {

    List<BlacklistVO> getBlacklistInformation();

    void removeBlacklist(Long id);

    void addBlacklist(Long id);

    void report();

    UserInformationVO getModificationInformation();

    void updateUserInformation(UserInformationDTO userInformationDTO);
}