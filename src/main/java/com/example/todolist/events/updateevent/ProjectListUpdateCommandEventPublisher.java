package com.example.todolist.events.updateevent;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectListUpdateCommandEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent() {
        applicationEventPublisher.publishEvent(new ProjectListUpdateCommandEvent());
    }

}
