package com.example.demo.controllers;

/**
 * Message
 */
public class Message {

    String text;

    public Message() {}

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message[text=" + text + "]";
    }
}