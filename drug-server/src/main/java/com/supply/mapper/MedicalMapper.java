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


    List<SupplyVO> searchSupplyInformation(String firmName, String drugName);


    @Select("select * from request where request_user_id = #{id}")
    List<Request> getDrugRequestInformation(Long userId);

    @Insert("insert into request (user_id, request_user_id, request_content, request_time)" +
            "values (#{userId}, #{requestUserId}, #{requestContent}, #{requestTime})")
    void addRequset(Request request);

    @Select("select distinct drug_name from doctor_drug where delete_time is null")
    List<DoctorDrug> getDrugsName();

    @Select("select count(*) from doctor_drug where drug_name = #{drugName} and delete_time is null")
    int getInventoryNumber(String drugName);

    @Select("select u.firm_name from supply_drug sd join user u on sd.user_id = u.user_id where sd.drug_name = #{drugName}")
    List<String> getFirmsName(String drugName);

    @Update("update doctor_drug set delete_time = #{now} where id = #{id}")
    void deleteDrug(Long id, LocalDateTime now);

    @Select("select drug_name,DATE_FORMAT(enter_time, '%Y-%m') as enterTime,COUNT(enter_time) as enterAmount," +
            "DATE_FORMAT(delete_time, '%Y-%m') as deleteTime,COUNT(delete_time) as deleteAmount " +
            "from doctor_drug group by drug_name, DATE_FORMAT(enter_time, '%Y-%m'), DATE_FORMAT(delete_time, '%Y-%m')")
    List<DrugStatisticVO> drugStatistic();
}
