package com.example.todo.service;

import com.example.todo.dto.TaskRequest;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Task> getByStatus(Task.Status status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Task> getByPriority(Task.Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    @Transactional(readOnly = true)
    public List<Task> search(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Запись

    public Task create(TaskRequest request) {
        Task task = new Task();
        applyRequest(task, request);
        return taskRepository.save(task);
    }


    public Task update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        applyRequest(task, request);
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        // Сначала проверяем существование — чтобы вернуть 404
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }


    private void applyRequest(Task task, TaskRequest req) {
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        if (req.getPriority() != null) task.setPriority(req.getPriority());
        if (req.getDueDate() != null)  task.setDueDate(req.getDueDate());
        if (req.getStatus() != null)   task.setStatus(req.getStatus());
    }
}