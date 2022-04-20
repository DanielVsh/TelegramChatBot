package com.example.telegramchatbot.settings.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TelegramUserImpl implements TelegramUser {
  private final TelegramUserRepository telegramUserRepository;

  @Autowired
  public TelegramUserImpl(TelegramUserRepository telegramUserRepository) {
    this.telegramUserRepository = telegramUserRepository;
  }

  @Override
  public void add(TgUser tgUser) {
    telegramUserRepository.save(tgUser);
  }

  public ArrayList<String> findWordsByUserId(Long userId) {
    ArrayList<String> allWordsByUser
        = new ArrayList<>(telegramUserRepository.findAllWordsByUser(userId));

    return (ArrayList<String>) allWordsByUser.stream()
        .map(s -> s.split(" "))
        .flatMap(Arrays::stream)
        .collect(Collectors.toList());
  }

  public Integer getWordCountByWordAndUserId(Long userId, String word) {
    System.out.println(telegramUserRepository.findWordCountByUserAndWord(userId, word));
    return telegramUserRepository.findWordCountByUserAndWord(userId, word);
  }

  public void updateWordCountByWordAndUserId(Long userId, String word) {
    telegramUserRepository.updateWordCount(userId, word);
  }

  public boolean isWordByUserIdExistsInDB(Long userId, String word) {
    return telegramUserRepository.isWordByUserIdInDataBase(userId, word).isPresent();
  }

  public ArrayList<String> getTopWordsByUserId(Long userId, int limit) {
    ArrayList<String> user = new ArrayList<>();
    ArrayList<TgUser> users = telegramUserRepository.getTopWordsByUserId(userId, limit);
    for (int u = 0; u < users.size() && u < limit; u++) {
      user.add(users.get(u).getWordCount() + " " + users.get(u).getWord());
    }
    user.add("\n" + "Всего уникальных слов: " + telegramUserRepository.getWordsCountByUserId(userId));
    return user;
  }

  ArrayList<String> getTopWordsByLengthAndUserId(Long userId, int length) {
    ArrayList<String> user = new ArrayList<>();
    ArrayList<TgUser> users
        = telegramUserRepository.getTopWordsByLengthAndUserId(userId, length);
    for (int u = 0; u < users.size() && u < 15; u++) {
      user.add("\n<b>"+users.get(u).getWordCount()+"</b>" + " " + "<b>"+users.get(u).getWord()+"</b>");
    }
    user.add("\n Количество слов, длина которых, больше чем " + length + " - " + "<b>"+ telegramUserRepository.getTopWordsByLengthAndUserId(userId, length).size() +"</b>"
    + "\n Всего уникальных слов: " + "<b>"+telegramUserRepository.getWordsCountByUserId(userId)+"</b>");
    return user;
  }

  public String getWordByUserIdAndWord(Long userId, String word) {
    Optional<TgUser> wordByUserIdAndWord
        = telegramUserRepository.getWordByUserIdAndWord(userId, word);

    return wordByUserIdAndWord.map(tgUser -> "Word: " + "<b>" + tgUser.getWord() + "</b>" +
        " Count: " + "<b>" + tgUser.getWordCount() + "</b>" +
        "\n").orElse("Слова нет в базе");
  }
}
