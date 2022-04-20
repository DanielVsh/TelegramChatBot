package com.example.telegramchatbot.settings.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TelegramUserService implements TelegramUser {
  private final TelegramUserImpl telegramUser;

  @Autowired
  public TelegramUserService(TelegramUserImpl telegramUser) {
    this.telegramUser = telegramUser;
  }

  @Override
  public void add(TgUser tgUser) {
    telegramUser.add(tgUser);
  }

  public ArrayList<String> getWordsByUserId(Long userId) {
    return telegramUser.findWordsByUserId(userId);
  }

  public Integer getWordCountByWordAndUserId(String word, Long userId) {
    return telegramUser.getWordCountByWordAndUserId(userId, word);
  }

  public void updateWordCountByWordAndUserId(Long userId, String word) {
    telegramUser.updateWordCountByWordAndUserId(userId, word);
  }

  public boolean isWordByUserIdExistsInDB(Long userId, String word) {
    return telegramUser.isWordByUserIdExistsInDB(userId, word);
  }

  public ArrayList<String> getTopWordsByUserId(Long userId, int limit) {
    return telegramUser.getTopWordsByUserId(userId, limit);
  }

  public String getWordByUserIdAndWord(Long userId, String word) {
    return telegramUser.getWordByUserIdAndWord(userId, word);
  }

  public String getTopWordsByLengthAndUserId(Long userId, int limit) {
    return telegramUser.getTopWordsByLengthAndUserId(userId, limit).toString();
  }
}