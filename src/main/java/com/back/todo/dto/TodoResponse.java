package com.back.todo.dto;

import com.back.todo.entity.Todo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoResponse {

    private Integer id;
    private String title;
    private boolean completed;
    private LocalDateTime createdAt;

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.isCompleted(),
                todo.getCreatedAt()
        );
    }
}
