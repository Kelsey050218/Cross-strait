package com.supply.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugStatisticVO implements Serializable {


    // 药品名称
    private String drugName;

    // 药品出库信息（模糊到月）
    private int[] deleteInfo;

    // 药品入库信息（模糊到月）
    private int[] enterInfo;



}
