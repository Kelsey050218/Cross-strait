package com.supply.service;

import com.supply.dto.DrugInputDTO;
import com.supply.dto.RequestDTO;
import com.supply.dto.UserInformationDTO;
import com.supply.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MedicalService {

    UserInformationVO getInformation();

    

    List<RequestVO> getDrugRequestInformation();

    List<DrugShortageVO> shortageWarning();

    List<DrugStatisticVO> drugStatistic();

    DrugInformationVO searchDrugInformation(String drugName);

    void output(Long id);

    void sendRequset(RequestDTO requestDTO);

//    void input(DrugInputDTO drugInputDTO);


    SupplyVO searchSupplyInformation(String firmName);

    List<DrugInformationVO> getDrugsInformation();
}
