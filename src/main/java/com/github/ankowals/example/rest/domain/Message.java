package com.github.ankowals.example.rest.domain;

public class Message {

  private int code;
  private String content;

  public Message(int code, String content) {
    this.code = code;
    this.content = content;
  }

  public Message() {}

  public void setCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContent() {
    return this.content;
  }
}
