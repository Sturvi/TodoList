package com.example.todolist.views.project;

import com.example.todolist.events.updateevent.ProjectListUpdateCommandEventPublisher;
import com.example.todolist.model.task.Project;
import com.example.todolist.service.ProjectService;
import com.example.todolist.views.MainLayout;
import com.example.todolist.views.NavigationalTools;
import com.example.todolist.views.ViewsEnum;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Hello World")
@Route(value = "project", layout = MainLayout.class)
@PermitAll
public class ProjectView extends HorizontalLayout implements HasUrlParameter<String>, NavigationalTools {

    private ProjectService projectService;
    private Project project;
    private H1 newTitle;
    private TextField titleEditor;
    private ProjectListUpdateCommandEventPublisher updateProjectList;

    public ProjectView(ProjectService projectService, ProjectListUpdateCommandEventPublisher updateProjectList) {
        this.projectService = projectService;
        this.updateProjectList = updateProjectList;
        this.newTitle = new H1(); // Создаем, но не устанавливаем текст
        this.titleEditor = new TextField();
        titleEditor.setVisible(false); // Изначально скрываем поле ввода
        titleEditor.addBlurListener(e -> updateProjectName());
        titleEditor.addKeyDownListener(Key.ENTER, e -> updateProjectName());

        newTitle.addClickListener(e -> {
            titleEditor.setValue(newTitle.getText()); // Устанавливаем текущее название в поле редактирования
            newTitle.setVisible(false); // Скрываем заголовок
            titleEditor.setVisible(true); // Показываем поле редактирования
            titleEditor.focus(); // Фокус на поле ввода
        });

        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH)); // Используем иконку корзины
        deleteButton.addClickListener(e -> deleteProject());

// Применяем классы стилей для кнопки
        deleteButton.addClassNames("text-error", "icon-error", "small-button");

// Добавляем кнопку удаления и текстовый заголовок в горизонтальный макет для выравнивания
        HorizontalLayout header = new HorizontalLayout(newTitle, deleteButton);
        header.setVerticalComponentAlignment(Alignment.CENTER, deleteButton); // Выравнивание по центру

        VerticalLayout verticalLayout = new VerticalLayout();
// Теперь добавляем header вместо отдельных компонентов в вертикальный макет
        verticalLayout.add(header, titleEditor);

HorizontalLayout horizontalLayout = new HorizontalLayout(verticalLayout, deleteButton);
horizontalLayout.addClassName("my-horizontal-layout"); // Применяем ваш CSS класс к HorizontalLayout


        add(horizontalLayout); // Добавляем заголовок и кнопки в компоновку
    }

    private void deleteProject() {

    }

    private void updateProjectName() {
        project.setName(titleEditor.getValue());
        projectService.updateProject(project);
        newTitle.setText(project.getName());
        newTitle.setVisible(true);
        titleEditor.setVisible(false);
        updateProjectList.publishEvent();
    }

/*    private HorizontalLayout getEditAndDeleteButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

// Добавление пользовательского класса для стилей
        // Удаляем обработчик клика для кнопки "Изменить", так как он больше не нужен



        horizontalLayout.add(editButton);

        return horizontalLayout;
    }*/

    @Override
    public void setParameter(BeforeEvent beforeEvent, String projectId) {
        var projectOpt = projectService.getProject(Long.parseLong(projectId));

        projectOpt.ifPresentOrElse(
                project -> {
                    if (project.getUser().getUsername().equals(getCurrentUsername())) {
                        this.project = project;
                        newTitle.setText(project.getName()); // Теперь устанавливаем текст
                    } else {
                        navigateTo(ViewsEnum.PAGE404);
                    }
                },
                () -> navigateTo(ViewsEnum.PAGE404)
        );
    }
}
