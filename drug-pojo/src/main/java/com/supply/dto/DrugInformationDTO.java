package com.supply.dto;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugInformationDTO implements Serializable {

    private String drugName; //新药品名

    private Integer inventoryNumber; // 新药品库存数量

}
