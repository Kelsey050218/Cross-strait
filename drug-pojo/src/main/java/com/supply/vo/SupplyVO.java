package com.supply.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyVO implements Serializable {

    private Long id;              //供应商所有人id

    private String firmName;      //供应商姓名

    private String image;         //供应商头像

    private String address;       //供应商地址

    private List<String> supplyDrugs; //供应商供应药品

}
