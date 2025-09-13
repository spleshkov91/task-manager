package com.example.taskmanager.task;

import com.example.taskmanager.task.dto.TaskCreateDto;
import com.example.taskmanager.task.dto.TaskReadDto;
import com.example.taskmanager.task.dto.TaskUpdateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tasks")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskReadDto create(@Valid @RequestBody TaskCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public TaskReadDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public Page<TaskReadDto> listByOwner(@RequestParam Long ownerId, Pageable pageable) {
        return service.listByOwner(ownerId, pageable);
    }

    @PatchMapping("/{id}")
    public TaskReadDto update(@PathVariable Long id, @Valid @RequestBody TaskUpdateDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
