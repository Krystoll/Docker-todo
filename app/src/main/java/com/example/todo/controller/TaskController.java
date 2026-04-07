package com.example.todo.controller;

import com.example.todo.dto.TaskRequest;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    // GET /api/tasks                - все задачи
    // GET /api/tasks?status=TODO    - фильтр по статусу
    // GET /api/tasks?priority=HIGH  - фильтр по приоритету
    // GET /api/tasks?search=docker  - поиск по названию

    @GetMapping
    public List<Task> getAll(
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority,
            @RequestParam(required = false) String search
    ) {
        if (status != null)   return taskService.getByStatus(status);
        if (priority != null) return taskService.getByPriority(priority);
        if (search != null)   return taskService.search(search);
        return taskService.getAllTasks();
    }


    // GET /api/tasks/{id}

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.getById(id);
    }


    // POST /api/tasks

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }


    // PUT /api/tasks/{id} — полное обновление задачи

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }


    // DELETE /api/tasks/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}