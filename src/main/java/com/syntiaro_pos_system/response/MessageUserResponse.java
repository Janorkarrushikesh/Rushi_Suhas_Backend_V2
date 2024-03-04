package com.syntiaro_pos_system.response;

public class MessageUserResponse {
  private String message;

  public MessageUserResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
