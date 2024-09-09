package com.supply.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyInformationVO implements Serializable {

    private String supplyAddress; //供应商地址

    private String[] supplyDrugs; //供应商供应药品

    private String supplyImage; //供应商头像

    private String firmName; //供应商公司名称
}
