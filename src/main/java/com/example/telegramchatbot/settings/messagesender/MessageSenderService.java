package com.example.telegramchatbot.settings.messagesender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class MessageSenderService {
    private MessageSender messageSender;

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void text(SendMessage sendMessage) {
        messageSender.sendMessage(sendMessage);
    }
}
