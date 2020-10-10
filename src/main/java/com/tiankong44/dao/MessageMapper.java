package com.tiankong44.dao;

import com.tiankong44.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName MessageMapper
 * @Description TODO
 * @Author 12481
 * @Date 22:10
 * @Version 1.0
 **/
@Repository
@Mapper
public interface MessageMapper {
    List<Message> getAllMessage();

    List<Message> getReplyByMessageId(Long id);

    List<Message> getALLReply();

    void saveMessage(Message Message);

    void saveReply(Message Message);

    Message getByParentMessageId(Long parentMessageId);

    Message getById(Long id);
}
