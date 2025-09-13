package com.example.taskmanager.task;

import com.example.taskmanager.common.NotFoundException;
import com.example.taskmanager.task.dto.TaskCreateDto;
import com.example.taskmanager.task.dto.TaskReadDto;
import com.example.taskmanager.task.dto.TaskUpdateDto;
import com.example.taskmanager.user.User;
import com.example.taskmanager.user.UserRepository;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskReadDto create(TaskCreateDto dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner not found"));
        Task t = new Task();
        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        t.setStatus(dto.getStatus());
        t.setPriority(dto.getPriority());
        t.setDueDate(dto.getDueDate());
        t.setOwner(owner);
        t.setCreatedAt(OffsetDateTime.now());
        t.setUpdatedAt(OffsetDateTime.now());
        t = taskRepository.save(t);
        return toReadDto(t);
    }

    public TaskReadDto get(Long id) {
        return toReadDto(find(id));
    }

    public Page<TaskReadDto> listByOwner(Long ownerId, Pageable pageable) {
        return taskRepository.findByOwnerId(ownerId, pageable).map(this::toReadDto);
    }

    public TaskReadDto update(Long id, TaskUpdateDto dto) {
        Task t = find(id);
        if (dto.getTitle() != null) t.setTitle(dto.getTitle());
        if (dto.getDescription() != null) t.setDescription(dto.getDescription());
        if (dto.getStatus() != null) t.setStatus(dto.getStatus());
        if (dto.getPriority() != null) t.setPriority(dto.getPriority());
        if (dto.getDueDate() != null) t.setDueDate(dto.getDueDate());
        t.setUpdatedAt(OffsetDateTime.now());
        t = taskRepository.save(t);
        return toReadDto(t);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) throw new NotFoundException("Task not found");
        taskRepository.deleteById(id);
    }

    private Task find(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
    }

    private TaskReadDto toReadDto(Task t) {
        TaskReadDto d = new TaskReadDto();
        d.setId(t.getId());
        d.setTitle(t.getTitle());
        d.setDescription(t.getDescription());
        d.setStatus(t.getStatus());
        d.setPriority(t.getPriority());
        d.setDueDate(t.getDueDate());
        d.setOwnerId(t.getOwner().getId());
        d.setCreatedAt(t.getCreatedAt());
        d.setUpdatedAt(t.getUpdatedAt());
        return d;
    }
}
