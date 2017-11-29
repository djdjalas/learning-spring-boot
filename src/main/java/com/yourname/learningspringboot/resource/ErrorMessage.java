package com.yourname.learningspringboot.resource;

public class ErrorMessage {
  private final String errorMessage;

  public ErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}