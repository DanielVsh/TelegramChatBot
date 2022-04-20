package com.example.telegramchatbot.settings.messagesender;

import com.example.telegramchatbot.settings.TelegramChatBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MessageSenderImpl implements MessageSender {
    private TelegramChatBot telegramChatBot;

    @Autowired
    public void setTelegramChatBot(TelegramChatBot telegramChatBot) {
        this.telegramChatBot = telegramChatBot;
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            telegramChatBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
