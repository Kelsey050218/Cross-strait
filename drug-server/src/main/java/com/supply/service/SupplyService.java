package com.supply.service;

import com.supply.dto.DrugInformationDTO;
import com.supply.dto.DrugNumberChangeDTO;
import com.supply.vo.BlacklistVO;
import com.supply.vo.RequestVO;
import com.supply.vo.SupplyDrugVO;
import com.supply.vo.UserInformationVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SupplyService {


    UserInformationVO getInformation();

    List<SupplyDrugVO> getDrugsInformation();

    void modifyDrugsInformation(Long id, DrugInformationDTO drugInformationDTO);

    void ModifyDrugsNumber(Long id, DrugNumberChangeDTO drugNumberChangeDTO);

    void addDrugs(DrugInformationDTO drugInformationDTO);

    void deleteDrug(Long id);

    List<RequestVO> getDrugRequestInformation();

    void dealRequest(Long id, Integer drugAgree);
}
