package com.example.todolist;

import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void error(com.vaadin.flow.server.ErrorEvent event) {
        // Логирование исключения для отладки
        Throwable t = event.getThrowable();
        while (t.getCause() != null) {
            t = t.getCause();
        }
        t.printStackTrace(); // или используйте логгер

        // Показать уведомление пользователю
        UI.getCurrent().access(() -> {
            Notification.show("Произошла ошибка. Попробуйте повторить операцию позже.", 3000, Notification.Position.MIDDLE);
        });
    }
}

