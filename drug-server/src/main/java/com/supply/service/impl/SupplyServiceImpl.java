package com.supply.service.impl;

import com.supply.constant.FormattingConstant;
import com.supply.context.BaseContext;
import com.supply.dto.DrugInformationDTO;
import com.supply.dto.DrugNumberChangeDTO;
import com.supply.entity.Request;
import com.supply.entity.SupplyDrug;
import com.supply.entity.User;
import com.supply.mapper.SupplyMapper;
import com.supply.mapper.UserLoginMapper;
import com.supply.service.SupplyService;
import com.supply.vo.RequestVO;
import com.supply.vo.SupplyDrugVO;
import com.supply.vo.UserInformationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

    private final SupplyMapper supplyMapper;

    private final UserLoginMapper userLoginMapper;

    /**
     * 个人信息回显
     *
     * @return 用户信息
     */
    public UserInformationVO getInformation() {
        User user = userLoginMapper.getUserInformationById(BaseContext.getCurrentId());
        UserInformationVO userInformationVO = new UserInformationVO();
        BeanUtils.copyProperties(user, userInformationVO);
        log.info("当前登录的供应端信息：{}", userInformationVO);
        return userInformationVO;
    }

    /**
     * 药品信息查询
     *
     * @return 药品所有信息
     */
    public List<SupplyDrugVO> getDrugsInformation() {
        List<SupplyDrug> drugs = supplyMapper.getDrugsInformation(BaseContext.getCurrentId());
        List<SupplyDrugVO> list = new ArrayList<>();
        for (SupplyDrug drug : drugs) {
            SupplyDrugVO supplyDrugVO = new SupplyDrugVO();
            BeanUtils.copyProperties(drug, supplyDrugVO);
            supplyDrugVO.setLastModificationTime(drug.getLastModificationTime().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
            log.info("当前药品信息为：{}", supplyDrugVO);
            list.add(supplyDrugVO);
        }
        return list;
    }

    /**
     * 修改药品信息
     *
     * @param id                 药品id
     * @param drugInformationDTO 新药品信息
     */
    public void modifyDrugsInformation(Long id, DrugInformationDTO drugInformationDTO) {
        log.info("供应端修改药品信息：{}",drugInformationDTO);
        supplyMapper.modifyDrugsInformation(id, drugInformationDTO, LocalDateTime.now());
    }

    /**
     * 药品库存数量增减
     *
     * @param id                  药品id
     * @param drugNumberChangeDTO 药品增减信息
     */
    public void ModifyDrugsNumber(Long id, DrugNumberChangeDTO drugNumberChangeDTO) {
        Integer number;
        if (drugNumberChangeDTO.getAddOrSubtraction() == 1) {
            number = drugNumberChangeDTO.getNumber();
            log.info("对药品库存进行增操作，数量为：{}", number);
        } else {
            number = -1 * drugNumberChangeDTO.getNumber();
            log.info("对药品库存进行减操作，数量为：{}", (-1 * number));
        }
        supplyMapper.ModifyDrugsNumber(id, number, LocalDateTime.now());
    }

    /**
     * 药品信息增加
     *
     * @param drugInformationDTO 新药品信息
     */
    public void addDrugs(DrugInformationDTO drugInformationDTO) {
        log.info("当前供应端增加药品信息：{}", drugInformationDTO);
        SupplyDrug supplyDrug = new SupplyDrug();
        BeanUtils.copyProperties(drugInformationDTO, supplyDrug);
        supplyDrug.setLastModificationTime(LocalDateTime.now());
        supplyDrug.setUserId(BaseContext.getCurrentId());
        supplyMapper.addDrugs(supplyDrug);
    }

    /**
     * 药品信息删除
     *
     * @param id 药品id
     */
    public void deleteDrug(Long id) {
        log.info("当前供应端删除药品id：{}", id);
        supplyMapper.deleteDrug(id);
    }

    /**
     * 药品请求信息查询
     *
     * @return 药品请求信息
     */
    public List<RequestVO> getDrugRequestInformation() {
        List<Request> requests = supplyMapper.getDrugRequestInformation(BaseContext.getCurrentId());
        List<RequestVO> list = new ArrayList<>();
        for (Request request : requests) {
            RequestVO requestVO = new RequestVO();
            BeanUtils.copyProperties(request, requestVO);
            requestVO.setUserId(request.getRequestUserId());
            requestVO.setImage(userLoginMapper.getUserInformationById(request.getRequestUserId()).getImage());
            requestVO.setUsername(userLoginMapper.getUserInformationById(request.getRequestUserId()).getUsername());
            requestVO.setRequestTime(request.getRequestTime().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
            log.info("当前请求信息为：{}", requestVO);
            list.add(requestVO);
        }
        return list;
    }

    /**
     * 处理药品请求接口
     * @param id 请求id
     * @param drugAgree 是否同意，1为是，2为否
     */
    public void dealRequest(Long id, Integer drugAgree) {
        log.info("供应端对该次药品请求决定：{}",drugAgree);
        supplyMapper.dealRequest(id,drugAgree,LocalDateTime.now());
    }
}
