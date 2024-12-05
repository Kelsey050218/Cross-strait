package com.supply.mapper;

import com.supply.dto.UserInformationDTO;
import com.supply.entity.DoctorDrug;
import com.supply.entity.Request;
import com.supply.entity.SupplyDrug;
import com.supply.entity.User;
import com.supply.vo.DrugInformationVO;
import com.supply.vo.DrugStatisticVO;
import com.supply.vo.RequestVO;
import com.supply.vo.SupplyVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MedicalMapper {


    @Select("select id, username, password, email, telephone, firm_name, image, address from user where firm_name = #{firmName}")
    User searchSupplyInformation(String firmName);


    @Select("select * from request where request_user_id = #{id}")
    List<Request> getDrugRequestInformation(Long userId);

    @Insert("insert into request (user_id, request_user_id, request_content, request_time)" +
            "values (#{userId}, #{requestUserId}, #{requestContent}, #{requestTime})")
    void addRequset(Request request);

    @Select("select drug_name,enter_time,delete_time,id,shelf_life,drug_code,produce_Time,batchNo,barCode,drugStatus from doctor_drug")
    List<DoctorDrug> getDrugs();

    @Select("select distinct drug_name from doctor_drug")
    List<String> getDrugNames();

    @Select("select count(*) from doctor_drug where drug_name = #{drugName} and delete_time is null")
    int getInventoryNumber(String drugName);

    //根据药品名字查关联的供应商
    @Select("select u.firm_name from supply_drug sd join user u on sd.user_id = u.user_id where sd.drug_name = #{drugName}")
    List<String> getFirmsName(String drugName);

    @Update("update doctor_drug set delete_time = #{now} where drug_code = #{id}")
    void deleteDrug(Long id, LocalDateTime now);

    //根据供应商名字查相关药品
    @Select("select sd.drug_name from supply_drug sd join user u on sd.user_id = u.user_id where sd.firm_name = #{firmName}")
    List<String> getSupplyDrugs(String firmName);


}
