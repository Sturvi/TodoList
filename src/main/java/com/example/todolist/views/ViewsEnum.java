package com.example.todolist.views;

public enum ViewsEnum {
    LOGIN("login"),
    ABOUT("about"),
    HELLO("hello"),
    REGISTRATION("registration");

    private String view;

    ViewsEnum(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }
}