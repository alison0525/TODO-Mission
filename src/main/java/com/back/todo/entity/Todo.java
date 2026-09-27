package com.back.todo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Todo {

    private static final int TITLE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(nullable = false)
    private boolean completed = false;

    @CreatedDate
    private LocalDateTime createdAt;

    public Todo(String title) {
        this.title = title;
    }

    public void update(String title, Boolean completed) {
        if (title != null) {
            this.title = title;
        }
        if (completed != null) {
            this.completed = completed;
        }
    }
}
