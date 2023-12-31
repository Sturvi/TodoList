package com.example.todolist.views;

import com.example.todolist.CustomErrorHandler;
import com.example.todolist.events.updateevent.ProjectListUpdateCommandEvent;
import com.example.todolist.model.task.Project;
import com.example.todolist.model.user.User;
import com.example.todolist.service.ProjectService;
import com.example.todolist.service.UserService;
import com.example.todolist.views.about.AboutView;
import com.example.todolist.views.helloworld.HelloWorldView;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main view is a top-level placeholder for other views.
 */
@Component
@UIScope
public class MainLayout extends AppLayout implements NavigationalTools {
    private static final String PROJECT_NAME_PLACEHOLDER = "Enter project name";
    private static final String MENU_TOGGLE_LABEL = "Menu toggle";

    private final UserService userService;
    private final ProjectService projectService;
    private final Button addNewProjectButton;
    private final TextField newProjectField;
    private final H2 viewTitle;
    private final VerticalLayout projectNav;
    private boolean isAddingProject = false;
    private Div itemContainer;

    public MainLayout(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
        itemContainer = new Div();
        VaadinSession.getCurrent().setErrorHandler(new CustomErrorHandler());

        setPrimarySection(Section.DRAWER);

        addNewProjectButton = createAddNewProjectButton();
        newProjectField = createNewProjectField();
        viewTitle = new H2();
        projectNav = new VerticalLayout();
        projectNav.setPadding(false);
        projectNav.setMargin(false);
        projectNav.setSpacing(false);

        addHeaderContent();
        createDrawer();
        updateProjectNav();
    }

    private Button createAddNewProjectButton() {
        Button button = new Button("Add New Project", e -> toggleProjectAdding());
        button.setVisible(!isAddingProject);
        return button;
    }

    private TextField createNewProjectField() {
        TextField textField = new TextField();
        textField.setPlaceholder(PROJECT_NAME_PLACEHOLDER);
        textField.addKeyDownListener(Key.ENTER, e -> addNewProject());
        textField.setVisible(isAddingProject);
        return textField;
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel(MENU_TOGGLE_LABEL);

        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        addToNavbar(true, toggle, viewTitle);
    }

    private void createDrawer() {
        addToDrawer(
                createHeader(),
                new SideNavItem("Hello World", HelloWorldView.class, LineAwesomeIcon.GLOBE_SOLID.create()),
                new SideNavItem("About", AboutView.class, LineAwesomeIcon.FILE.create()),
                createFooter(),
                addEmptySpace(),
                createSeparator(),
                addTitle("Projects"),
                addNewProjectButton, newProjectField,
                new Scroller(projectNav)
        );
    }

    private HtmlContainer addTitle(String title) {
        H3 projectsTitle = new H3(title);
        projectsTitle.getStyle().set("text-align", "center");

        return projectsTitle;
    }

    private HorizontalLayout addEmptySpace() {
        HorizontalLayout space = new HorizontalLayout();
        space.setWidthFull();
        space.setHeight("20px");

        return space;
    }

    private HorizontalLayout createSeparator() {
        HorizontalLayout separator = new HorizontalLayout();
        separator.setWidthFull();
        separator.setHeight("2px");
        separator.getStyle().set("background", "var(--lumo-contrast-20pct)");
        return separator;
    }

    private Header createHeader() {
        H1 appName = new H1("Sturvi TODO LIST");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        return new Header(appName);
    }

    private Footer createFooter() {
        return new Footer();
    }

    private void toggleProjectAdding() {
        isAddingProject = !isAddingProject;
        updateAddingProjectVisibility();
    }

    private void updateAddingProjectVisibility() {
        addNewProjectButton.setVisible(!isAddingProject);
        newProjectField.setVisible(isAddingProject);
    }

    private void addNewProject() {
        String projectName = newProjectField.getValue();
        if (projectName != null && !projectName.isEmpty()) {
            User user = userService.findByUsername(getCurrentUsername());
            Project newProject = Project.builder()
                    .name(projectName)
                    .user(user)
                    .build();
            projectService.save(newProject);
        }
        newProjectField.setValue("");
        updateProjectNav();
        toggleProjectAdding();
    }

    private void updateProjectNav() {
        projectNav.removeAll();
        User user = userService.findByUsername(getCurrentUsername());
        if (user != null) {
            user.getProjects().stream()
                    .sorted(Comparator.comparing(Project::getSortOrder, Comparator.nullsLast(Integer::compareTo)))
                    .forEach(project -> {
                        var item = new SideNavItem(
                                project.getName(),
                                ViewsEnum.PROJECT.getView() + "/" + project.getId(),
                                LineAwesomeIcon.FILE.create());

                        Div itemContainer = new Div();
                        itemContainer.addClassName("project-item");
                        itemContainer.setId("project-" + project.getId());
                        itemContainer.add(item);
                        DragSource<Div> dragSource = DragSource.create(itemContainer);
                        DropTarget<Div> dropTarget = DropTarget.create(itemContainer);

                        dragSource.addDragStartListener(e -> {
                            // Сохраняем идентификатор перетаскиваемого проекта в сессии
                            VaadinSession.getCurrent().setAttribute("draggedProjectId", project.getId());
                        });


                        dropTarget.addDropListener(e -> {
                            Long draggedProjectId = (Long) VaadinSession.getCurrent().getAttribute("draggedProjectId");


                            // Определяем проекты до и после точки сброса
                            Project previousProject = null;
                            Project nextProject = null;

                            List<Project> projects = user.getProjects().stream()
                                    .sorted(Comparator.comparing(Project::getSortOrder))
                                    .collect(Collectors.toList());

                            for (int i = 0; i < projects.size(); i++) {
                                Project currentProject = projects.get(i);
                                if (currentProject.getId().equals(project.getId())) {
                                    if (i > 0) previousProject = projects.get(i - 1);
                                    if (i < projects.size() - 1) nextProject = projects.get(i + 1);
                                    break;
                                }
                            }

                            projectService.updateProjectPosition (previousProject, project);

                            updateProjectNav();
                        });

                        projectNav.add(itemContainer);
                    });
        }
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
        updateNavigation();
    }

    private void updateNavigation() {
        // Implement navigation update logic
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    @EventListener
    public void waitingUpdateCommand(ProjectListUpdateCommandEvent event) {
        updateProjectNav();
    }
}
