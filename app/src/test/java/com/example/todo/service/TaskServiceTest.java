package com.example.todo.service;

import com.example.todo.dto.TaskRequest;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;


 // @ExtendWith(MockitoExtension.class) — инициализирует @Mock и @InjectMocks.

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService unit tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskRequest request;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Test task");
        task.setStatus(Task.Status.TODO);
        task.setPriority(Task.Priority.MEDIUM);

        request = new TaskRequest();
        request.setTitle("Test task");
        request.setStatus(Task.Status.TODO);
        request.setPriority(Task.Priority.MEDIUM);
    }

    @Test
    @DisplayName("getAllTasks: возвращает список из репозитория")
    void getAllTasksReturnsList() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Test task", result.get(0).getTitle());
        verify(taskRepository).findAll();
    }

//    @Test
//    @DisplayName("getById: существующая задача — возвращает задачу")
//    void getByIdExistingTaskReturnsTask() {
//        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
//
//        Task result = taskService.getById(1L);
//
//        assertEquals("Test task", result.getTitle());
//    }
//
//    @Test
//    @DisplayName("getById: задача не найдена — бросает TaskNotFoundException")
//    void getByIdNotFoundThrowsException() {
//        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThrows(
//                TaskNotFoundException.class,
//                () -> taskService.getById(99L)
//        );
//    }
//
//    @Test
//    @DisplayName("create: сохраняет и возвращает задачу")
//    void createSavesAndReturnsTask() {
//        when(taskRepository.save(any(Task.class))).thenReturn(task);
//
//        Task result = taskService.create(request);
//
//        assertEquals("Test task", result.getTitle());
//        verify(taskRepository).save(any(Task.class));
//    }
//
//    @Test
//    @DisplayName("update: существующая задача — обновляет поля")
//    void updateExistingTaskUpdatesFields() {
//        TaskRequest updateRequest = new TaskRequest();
//        updateRequest.setTitle("Updated title");
//        updateRequest.setStatus(Task.Status.DONE);
//        updateRequest.setPriority(Task.Priority.HIGH);
//
//        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
//        when(taskRepository.save(any(Task.class))).thenReturn(task);
//
//        Task result = taskService.update(1L, updateRequest);
//
//        assertNotNull(result);
//        verify(taskRepository).save(any(Task.class));
//    }
//
//    @Test
//    @DisplayName("update: задача не найдена — бросает TaskNotFoundException")
//    void updateNotFoundThrowsException() {
//        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThrows(
//                TaskNotFoundException.class,
//                () -> taskService.update(99L, request)
//        );
//    }
//
//    @Test
//    @DisplayName("delete: существующая задача — удаляет её")
//    void deleteExistingTaskDeletesIt() {
//        when(taskRepository.existsById(1L)).thenReturn(true);
//        doNothing().when(taskRepository).deleteById(1L);
//
//        assertDoesNotThrow(() -> taskService.delete(1L));
//        verify(taskRepository).deleteById(1L);
//    }
//
//    @Test
//    @DisplayName("delete: задача не найдена — бросает TaskNotFoundException")
//    void deleteNotFoundThrowsException() {
//        when(taskRepository.existsById(99L)).thenReturn(false);
//
//        assertThrows(
//                TaskNotFoundException.class,
//                () -> taskService.delete(99L)
//        );
//    }
//
//    @Test
//    @DisplayName("getByStatus: возвращает задачи с нужным статусом")
//    void getByStatusReturnsFilteredList() {
//        when(taskRepository.findByStatus(Task.Status.TODO))
//                .thenReturn(List.of(task));
//
//        List<Task> result = taskService.getByStatus(Task.Status.TODO);
//
//        assertEquals(1, result.size());
//        assertEquals(Task.Status.TODO, result.get(0).getStatus());
//    }
}