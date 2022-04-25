package com.example.telegramchatbot.settings;

import com.example.telegramchatbot.settings.db.Dictionary;
import com.example.telegramchatbot.settings.db.TelegramUserService;
import com.example.telegramchatbot.settings.db.TgUser;
import com.example.telegramchatbot.settings.messagesender.MessageSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TelegramChatBot extends TelegramLongPollingBot {
  @Value("${telegram.bot.token}")
  private String token;
  @Value("${telegram.bot.username}")
  private String username;

  private MessageSenderService messageSenderService;
  private TelegramUserService telegramUserService;

  private final Dictionary dictionary = new Dictionary();
  private final HashSet<String> dictionarySet = dictionary.dictionary();


  @Override
  public String getBotUsername() {
    return username;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String message = " ";
      if (update.getMessage().getForwardDate() == null) {
        message = update.getMessage().getText();
      }
      String chatId = update.getMessage().getChatId().toString();
      String nickName = update.getMessage().getFrom().getUserName();
      Long userId = update.getMessage().getFrom().getId();
      ArrayList<String> words = new ArrayList<>(
          List.of(message.split("[-!@#$%^&*()_+<>,.\\/?=1234567890;:\" \n\t]")));
      words = (ArrayList<String>) words.stream()
          .map(String::toLowerCase)
          .filter(s -> s.length() > 1)
          .filter(s -> !dictionarySet.contains(s))
          .filter(s -> s.matches("[а-яА-Я]+"))
          .collect(Collectors.toList());
      if ("/start".equals(message)) {
        messageSenderService.text(SendMessage
            .builder()
            .text("Этот бот показывает топ ваших слов\n" +
                "/help - список команд бота")
            .chatId(chatId)
            .build());
      } else if (message.matches("/mystat \\d+|/mystat@msstatbot_bot \\d+|")) {
        int limit = Integer.parseInt(message.split(" ")[1]);
        if (limit > 100) limit = 100;
        else if (limit < 1) limit = 1;
        messageSenderService.text(SendMessage.builder()
            .replyToMessageId(update.getMessage().getMessageId())
            .text("<b>" + telegramUserService.getTopWordsByUserId(userId, limit).toString() + "</b>")
            .parseMode("HTML")
            .chatId(chatId)
            .build());
      } else if (message.matches("/mystat [а-яА-Я]+|/mystat@msstatbot_bot [а-яА-Я]+")) {
        messageSenderService.text(SendMessage.builder()
            .replyToMessageId(update.getMessage().getMessageId())
            .text(telegramUserService.getWordByUserIdAndWord(userId, message.split(" ")[1]))
            .parseMode("HTML")
            .chatId(chatId)
            .build());
      } else if (message.matches("/mystat|/mystat@msstatbot_bot")) {
        messageSenderService.text(SendMessage.builder()
            .replyToMessageId(update.getMessage().getMessageId())
            .text("<b>" + telegramUserService.getTopWordsByUserId(userId, 25).toString() + "</b>")
            .parseMode("HTML")
            .chatId(chatId)
            .build());
      } else if ("/help".equals(message) || "/help@msstatbot_bot".equals(message)) {
        messageSenderService.text(SendMessage
            .builder()
            .text("/mystat - показать статистику по вашим словам" +
                "\n(/mystat 25 - показать по 25 словам)\n" +
                "(/mystat привет - показать статистику по слову привет)\n" +
                "/mytopbylength 5 - показать топ слов, длина которых больше чем 5\n")
            .chatId(chatId)
            .build());
      } else if (message.matches("/mytopbylength \\d+|/mytopbylength@msstatbot_bot \\d+")) {
        messageSenderService.text(SendMessage.builder()
            .replyToMessageId(update.getMessage().getMessageId())
            .text(telegramUserService.getTopWordsByLengthAndUserId(userId, Integer.parseInt(message.split(" ")[1])))
            .parseMode("HTML")
            .chatId(chatId)
            .build());
      } else {
        if (update.getMessage().getChatId() < 0) {
          for (String word : words) {
            if (word.length() < 30) {
              if (!telegramUserService.isWordByUserIdExistsInDB(userId, word)) {
                telegramUserService.add(new TgUser(userId, nickName, word));
              }
              telegramUserService.updateWordCountByWordAndUserId(userId, word);
            }
          }
        }
      }
    }

  }

  @Autowired
  public void setMessageSenderService(MessageSenderService messageSenderService) {
    this.messageSenderService = messageSenderService;
  }

  @Autowired
  public void setTelegramUserService(TelegramUserService telegramUserService) {
    this.telegramUserService = telegramUserService;
  }
}
