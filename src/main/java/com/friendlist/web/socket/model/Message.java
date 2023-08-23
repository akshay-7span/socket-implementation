package com.friendlist.web.socket.model;

public class Message {

    private String text;

    private String from;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    @Override
    public String toString()
    {
        return "Message{" + "text='" + text + '\'' + ", from='" + from + '\'' + '}';
    }
}