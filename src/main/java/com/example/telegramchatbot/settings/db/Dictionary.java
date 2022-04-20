package com.example.telegramchatbot.settings.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class Dictionary {
  public HashSet<String> dictionary() {
    HashSet<String> dictionary = new HashSet<>();

    try (BufferedReader in = new BufferedReader(
        new InputStreamReader(getFileAsIOStream(), StandardCharsets.UTF_8))) {
      String s;
      while ((s = in.readLine()) != null) {
        dictionary.add(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dictionary;
  }

  private InputStream getFileAsIOStream() {
    InputStream ioStream = this.getClass()
        .getClassLoader()
        .getResourceAsStream("dictionary.txt");
    if (ioStream == null) {
      throw new IllegalArgumentException("dictionary.txt" + " is not found");
    }
    return ioStream;
  }
}
