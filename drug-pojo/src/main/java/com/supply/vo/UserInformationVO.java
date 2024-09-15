package com.supply.vo;


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
public class UserInformationVO implements Serializable {

    private String username; //用户真实姓名

    private String email; //用户邮箱

    private String image; //用户头像

    private String firmName; //用户公司名

    private String telephone; //用户电话

}
