package com.example.todolist.views;

import com.vaadin.flow.component.UI;

public interface NavigationalTools {

    default void reloadPage() {
        UI.getCurrent().access(() -> UI.getCurrent().getPage().reload());
    }

    default void navigateTo(String page){
        UI.getCurrent().access(() -> UI.getCurrent().navigate(page));
    }
}

