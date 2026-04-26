package com.example.todo.repository;

import com.example.todo.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


//
 // @DataJpaTest — поднимает только JPA слой + H2 in-memory БД.
 // @ActiveProfiles("test") — загружает application-test.properties.
 // Реальный PostgreSQL не нужен.

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TaskRepository integration tests")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    private Task createTask(String title, Task.Status status, Task.Priority priority) {
        Task task = new Task();
        task.setTitle(title);
        task.setStatus(status);
        task.setPriority(priority);
        return taskRepository.save(task);
    }

    @Test
    @DisplayName("save: сохраняет задачу и назначает id")
    void saveAssignsId() {
        Task saved = createTask("Task 1", Task.Status.TODO, Task.Priority.LOW);

        assertNotNull(saved.getId());
        assertEquals("Task 1", saved.getTitle());
    }

    @Test
    @DisplayName("findAll: возвращает все сохранённые задачи")
    void findAllReturnsAllTasks() {
        createTask("Task A", Task.Status.TODO, Task.Priority.LOW);
        createTask("Task B", Task.Status.DONE, Task.Priority.HIGH);

        List<Task> tasks = taskRepository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("findById: существующий id — возвращает задачу")
    void findByIdExistingReturnsTask() {
        Task saved = createTask("Find me", Task.Status.TODO, Task.Priority.MEDIUM);

        Optional<Task> found = taskRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Find me", found.get().getTitle());
    }

    @Test
    @DisplayName("findById: несуществующий id — возвращает empty")
    void findByIdNotExistingReturnsEmpty() {
        Optional<Task> found = taskRepository.findById(9999L);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("findByStatus: возвращает только задачи с нужным статусом")
    void findByStatusReturnsFiltered() {
        createTask("Done task", Task.Status.DONE, Task.Priority.LOW);
        createTask("Todo task", Task.Status.TODO, Task.Priority.LOW);

        List<Task> doneTasks = taskRepository.findByStatus(Task.Status.DONE);

        assertEquals(1, doneTasks.size());
        assertEquals(Task.Status.DONE, doneTasks.get(0).getStatus());
    }

    @Test
    @DisplayName("deleteById: задача удаляется")
    void deleteByIdRemovesTask() {
        Task saved = createTask("To delete", Task.Status.TODO, Task.Priority.LOW);
        Long id = saved.getId();

        taskRepository.deleteById(id);

        assertFalse(taskRepository.findById(id).isPresent());
    }

    @Test
    @DisplayName("findByTitleContainingIgnoreCase: ищет по части названия")
    void findByTitleSearchWorks() {
        createTask("Docker setup", Task.Status.TODO, Task.Priority.HIGH);
        createTask("Write tests", Task.Status.TODO, Task.Priority.MEDIUM);

        List<Task> result = taskRepository.findByTitleContainingIgnoreCase("docker");

        assertEquals(1, result.size());
        assertEquals("Docker setup", result.get(0).getTitle());
    }
}