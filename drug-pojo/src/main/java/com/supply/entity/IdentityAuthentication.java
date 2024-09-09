package com.supply.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdentityAuthentication implements Serializable {

    private Long id;

    private Long userId;

    private String verifierPeople; //审核人

    private Integer workType; //工种

    private String images;

    private String IDNumber;

    private LocalDateTime applicationTime; //认证申请时间

    private LocalDateTime passTime; //审核通过时间


}
