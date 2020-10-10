package com.tiankong44.service.impl;

import com.tiankong44.dao.MessageMapper;
import com.tiankong44.model.Message;
import com.tiankong44.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName MessageServiceImpl
 * @Description TODO
 * @Author 12481
 * @Date 21:44
 * @Version 1.0
 **/
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<Message> getAllMessage() {
        List<Message> messageList = messageMapper.getAllMessage();
        for (Message message : messageList) {
            Long id = message.getId();
            List<Message> replyList = messageMapper.getReplyByMessageId(id);
            if (replyList != null) {
                for (Message reply : replyList) {

                    if (reply.getParentCommentId() == id) {
                        reply.setParentComment(messageMapper.getById(reply.getParentCommentId()));
                        message.setReplyComments(replyList);
                    }
                }
            }

        }
        return messageList;
    }

    @Override
    public void saveMessage(Message message) {
        Long parentCommentId = message.getParentCommentId();
        if (parentCommentId != -1) {
            messageMapper.saveReply(message);
        } else {
            message.setParentComment(null);
            message.setParentCommentId(null);
            messageMapper.saveMessage(message);
        }
    }

    @Override
    public List<Message> getReplyByMessageId(Long id) {
        return messageMapper.getReplyByMessageId(id);
    }

    @Override
    public List<Message> getALLReply() {
        return messageMapper.getALLReply();
    }
}
