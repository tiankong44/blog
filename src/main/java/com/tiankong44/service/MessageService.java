package com.tiankong44.service;

import com.tiankong44.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessage();

    void saveMessage(Message Message);

    List<Message> getReplyByMessageId(Long id);

    List<Message> getALLReply();
}
