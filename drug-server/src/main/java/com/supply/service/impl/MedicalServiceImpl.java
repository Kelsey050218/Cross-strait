package com.supply.service.impl;

import com.supply.constant.FormattingConstant;
import com.supply.context.BaseContext;
import com.supply.dto.DrugInputDTO;
import com.supply.dto.RequestDTO;
import com.supply.entity.DoctorDrug;
import com.supply.entity.Request;
import com.supply.entity.SupplyDrug;
import com.supply.entity.User;
import com.supply.mapper.MedicalMapper;
import com.supply.mapper.UserLoginMapper;
import com.supply.service.MedicalService;
import com.supply.vo.*;
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
public class MedicalServiceImpl implements MedicalService {

    private final MedicalMapper medicalMapper;

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
        log.info("当前登录的医护端信息：{}", userInformationVO);
        return userInformationVO;
    }

    /**
     * 查询供应商信息
     *
     * @param firmName
     * @return 供应商信息
     */
    public SupplyVO searchSupplyInformation(String firmName) {
        User user = medicalMapper.searchSupplyInformation(firmName);
        SupplyVO supplyVO = new SupplyVO();
        BeanUtils.copyProperties(user, supplyVO);
        supplyVO.setSupplyDrugs(medicalMapper.getSupplyDrugs(firmName));
        log.info("查询供应商信息：{}", supplyVO);
        return supplyVO;
    }


    /**
     * 药品请求信息查询接口
     * @return 药品请求信息
     */
    public List<RequestVO> getDrugRequestInformation() {
        List<Request> requests = medicalMapper.getDrugRequestInformation(BaseContext.getCurrentId());
        List<RequestVO> list = new ArrayList<>();
        for (Request request : requests) {
            RequestVO requestVO = new RequestVO();
            BeanUtils.copyProperties(request, requestVO);
            requestVO.setUserId(BaseContext.getCurrentId());
            requestVO.setImage(userLoginMapper.getUserInformationById(BaseContext.getCurrentId()).getImage());
            requestVO.setUsername(userLoginMapper.getUserInformationById(BaseContext.getCurrentId()).getUsername());
            requestVO.setRequestTime(request.getRequestTime().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
            requestVO.setRequestStatus(request.getIsAgree());
            log.info("当前请求信息为：{}", requestVO);
            list.add(requestVO);
        }
        return list;
    }

    /**
     * 发送药品请求
     * @param requestDTO
     */
    public void sendRequset(RequestDTO requestDTO) {
        Request request = new Request();
        request.setUserId(requestDTO.getProducerId());
        request.setRequestUserId(BaseContext.getCurrentId());
        request.setRequestContent(requestDTO.getDrugName() + " : " + requestDTO.getRequestWeight() + "g");
        request.setRequestTime(LocalDateTime.now());
        medicalMapper.addRequset(request);
        log.info("发送的请求信息为：{}", request);
    }

    /**
     * 药品库存告警
     * @return 药品库存告警信息
     */
    public List<DrugShortageVO> shortageWarning() {
        List<DrugShortageVO> shortageList = new ArrayList<>();
        List<DrugInformationVO> list = getDrugsInformation();
        for (DrugInformationVO drugInformationVO : list) {
            if (drugInformationVO.getInventoryNumber() < 10) {
                DrugShortageVO drugShortageVO = new DrugShortageVO();
                BeanUtils.copyProperties(drugInformationVO, drugShortageVO);
                shortageList.add(drugShortageVO);
            }
        }
        log.info("药品库存告警信息：{}", shortageList);
        return shortageList;
    }

    /**
     * 药品统计出入库信息
     * @return 药品统计信息
     */
    public List<DrugStatisticVO> drugStatistic() {
        List<DrugStatisticVO> drugStatisticList = new ArrayList<>();
        List<DoctorDrug> drugs = medicalMapper.getDrugs();
        List<String> drugNames = medicalMapper.getDrugNames();
        //索引代表月份
        int[] enter = new int[12];
        int[] output = new int[12];
        //遍历每个名字
        for (String drugName : drugNames) {
            DrugStatisticVO drugStatisticVO = new DrugStatisticVO();
            drugStatisticVO.setDrugName(drugName);
            //遍历每个药品
            for (DoctorDrug doctorDrug : drugs) {
                //如果名字相同
                if (doctorDrug.getDrugName().equals(drugName)) {
                    //加入入库数量
                    int month = doctorDrug.getEnterTime().getMonthValue();
                    enter[month - 1]++;
                    //加入出库数量
                    if (doctorDrug.getDeleteTime() != null) {
                        month = doctorDrug.getDeleteTime().getMonthValue();
                        output[month - 1]++;
                    }
                }
            }
            drugStatisticVO.setEnterInfo(enter);
            drugStatisticVO.setDeleteInfo(output);
            drugStatisticList.add(drugStatisticVO);
        }
        log.info("药品统计信息：{}", drugStatisticList);
        return drugStatisticList;
    }


    /**
     * 搜索单个药品信息
     * @param drugName
     * @return 药品信息
     */
    public DrugInformationVO searchDrugInformation(String drugName) {
        DrugInformationVO drugInformationVO = new DrugInformationVO();
        drugInformationVO.setDrugName(drugName);
        drugInformationVO.setInventoryNumber(medicalMapper.getInventoryNumber(drugName));
        drugInformationVO.setFirmsName(medicalMapper.getFirmsName(drugName));
        log.info("药品信息：{}", drugInformationVO);
        return drugInformationVO;
    }


    /**
     * 药品出库
     * @param id
     */
    public void output(Long id) {
        //只是给药品的deleteTime字段赋值为当前时间，并不删除药品，只是标记为删除状态
        medicalMapper.deleteDrug(id, LocalDateTime.now());
        log.info("删除药品成功:{}", id);
    }


//    public void input(DrugInputDTO drugInputDTO) {
//
//    }



    /**
     * 药品信息查询接口
     * @return 药品信息(包括库存数量,药品名称，供应商名)
     */
    public List<DrugInformationVO> getDrugsInformation() {
        //用名字去重复
        List<String> drugNames = medicalMapper.getDrugNames();
        List<DrugInformationVO> list = new ArrayList<>();
        for (String drugName : drugNames) {
            DrugInformationVO drugInformationVO = new DrugInformationVO();
            drugInformationVO.setDrugName(drugName);
            int inventoryNumber = medicalMapper.getInventoryNumber(drugName);
            drugInformationVO.setInventoryNumber(inventoryNumber);
            List<String> firmsName = medicalMapper.getFirmsName(drugName);
            drugInformationVO.setFirmsName(firmsName);
            list.add(drugInformationVO);
        }
        log.info("药品信息：{}", list);
        return list;
    }

}
