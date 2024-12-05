package com.supply.mapper;

import com.supply.entity.ChatInformation;
import com.supply.entity.ChatQueue;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChatMapper {

    /**
     * 创建聊天队列
     * @param currentId 当前发起聊天用户id
     * @param id 对方id
     * @param now 创建时间
     */
    @Insert("insert into chat_queue(user_id1, user_id2, createTime) VALUES(#{currentId},#{id},#{now}) ")
    void createChatQueue(Long currentId, Long id, LocalDateTime now);

    /**
     * 根据id获取聊天队列信息
     * @param id 队列id
     * @return 队列信息
     */
    @Select("select * from chat_queue where id = #{id}")
    ChatQueue getQueue(Long id);

    /**
     * 存储聊天数据
     * @param chatInformation 聊天信息
     */
    @Insert("insert into chat_information(queue_id,information,image,send_user_id,receive_user_id,send_time) VALUES(#{queueId},#{information},#{image},#{sendUserId},#{receiveUserId},#{sendTime}) ")
    void storeChatInformation(ChatInformation chatInformation);

    /**
     * 获取当前用户参与的所有聊天队列
     * @param currentId 当前用户id
     * @return 所有队列
     */
    @Select("select * from chat_queue where user_id1 = #{currentId} or user_id2 = #{currentId}")
    List<ChatQueue> getAllQueueByUserId(Long currentId);

    /**
     * 根据队列id获取一些聊天信息
     * @param id 队列id
     * @return 所有聊天信息
     */
    @Select("select * from chat_information where queue_id = #{id} limit 5")
    List<ChatInformation> getChatInformationByQueueId(Long id);

    /**
     * 根据队列id删除队列
     * @param id 队列id
     */
    @Delete("delete from chat_queue where id = #{id}")
    void deleteQueueById(Long id);

    /**
     * 根据队列id获取100条聊天信息
     * @param id 当前用户id
     * @return 聊天信息
     */
    @Select("select * from chat_information where queue_id = #{id} order by send_time desc limit 100")
    List<ChatInformation> getSomeChatInformationByQueueId(Long id);
}
