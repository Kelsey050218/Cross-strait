package com.supply.mapper;

import com.supply.entity.IdentityAuthentication;
import com.supply.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserLoginMapper {

    /**
     * 用户注册
     *
     * @param user 用户注册信息
     */
    void register(User user);

    /**
     * 用户重置密码
     *
     * @param user 用户信息
     */
    @Update("update user set password = #{password} where email = #{email}")
    void resetPassword(User user);

    /**
     * 用户登录
     *
     * @param user 用户登录信息
     * @return 比对后的用户信息
     */
    User login(User user);

    /**
     * 将审核信息放入申请表中
     *
     * @param identityAuthentication 申请信息
     */
    @Insert("insert into identity_authentication(user_id, work_type,ID_number,images,application_time) values (#{userId},#{workType},#{IDNumber},#{images},#{applicationTime})")
    void sendVerificationMessage(IdentityAuthentication identityAuthentication);

    /**
     * 根据用户id获取用户全部信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @Select("select * from user where id = #{id};")
    User getUserInformationById(Long id);

    /**
     * 将账户状态更改为可使用
     *
     * @param id 用户id
     */
    @Update("update user set account_status = 1 where id = #{id}")
    void changeStatusToNormal(Long id);

    /**
     * 将账户状态更改为审核失败
     *
     * @param id 用户id
     */
    @Update("update user set account_status = 4 where id = #{id}")
    void changeStatusToCheckFailed(Long id);

    /**
     * 封禁用户账号
     *
     * @param id 用户id
     */
    @Update("update user set account_status = 2 where id = #{id}")
    void blockAccount(Long id);


}
