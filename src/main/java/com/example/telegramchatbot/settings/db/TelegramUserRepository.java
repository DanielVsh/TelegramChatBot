package com.example.telegramchatbot.settings.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Repository
@Transactional
public interface TelegramUserRepository extends JpaRepository<TgUser, Long> {
  @Query(value = "select e.word from TgUser e where e.userId = ?1")
  ArrayList<String> findAllWordsByUser(Long userId);

  @Query(value = "select e.wordCount from TgUser e where e.userId = ?1 and e.word = ?2")
  Integer findWordCountByUserAndWord(Long userId, String word);

  @Modifying
  @Transactional
  @Query(value = "update TgUser e set e.wordCount = e.wordCount + 1 where e.userId = ?1 and e.word = ?2")
  void updateWordCount(Long userId, String word);

  @Query(value = "select distinct true from TgUser e where e.userId = ?1 and e.word = ?2")
  Optional<Boolean> isWordByUserIdInDataBase(Long userId, String word);


  @Query(value = "select e from TgUser e where e.userId = ?1 group by e, e.word order by e.wordCount desc")
  ArrayList<TgUser> getTopWordsByUserId(Long userId, int top);

  @Query(value = "select count(e.wordCount) from TgUser e where e.userId = ?1")
  int getWordsCountByUserId(Long userId);

  @Query(value = "select e from TgUser e where e.userId = ?1 and e.word = ?2")
  Optional<TgUser> getWordByUserIdAndWord(Long userId, String word);

  @Query(value = "select e from TgUser e where e.userId = ?1 and length(e.word) > ?2 order by e.wordCount desc")
  ArrayList<TgUser> getTopWordsByLengthAndUserId(Long userId, int length);
}