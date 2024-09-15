package com.supply.mapper;


import com.supply.entity.Blacklist;
import com.supply.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommonMapper {

    /**
     * 根据用户id查询黑名单
     *
     * @param id 当前用户id
     * @return 黑名单信息
     */
    @Select("select * from blacklist where user_id = #{id}")
    List<Blacklist> getBlacklistInformation(Long id);

    /**
     * 根据黑名单id删除黑名单信息
     *
     * @param id 黑名单id
     */
    @Delete("delete from blacklist where id = #{id}")
    void removeBlacklist(Long id);

    /**
     * 增加黑名单
     *
     * @param currentId 当前用户id
     * @param id        拉黑用户id
     * @param now       当前时间
     */
    @Insert("insert into blacklist(user_id, black_user_id, black_time) VALUES(#{currentId},#{id},#{now}) ")
    void addBlacklist(Long currentId, Long id, LocalDateTime now);

    void updateUserInformation(User user);
}