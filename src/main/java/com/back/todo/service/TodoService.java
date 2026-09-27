package com.back.todo.service;

import com.back.todo.dto.TodoCreateRequest;
import com.back.todo.dto.TodoResponse;
import com.back.todo.dto.TodoUpdateRequest;
import com.back.todo.entity.Todo;
import com.back.todo.exception.TodoException;
import com.back.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    @Transactional
    public TodoResponse create(TodoCreateRequest request) {
        Todo todo = new Todo(request.getTitle());
        Todo savedTodo = todoRepository.save(todo);

        return TodoResponse.from(savedTodo);
    }

    public List<TodoResponse> findAll() {
        return todoRepository.findAll()
                .stream()
                .map(TodoResponse::from)
                .toList();
    }

    public TodoResponse findById(Integer id) {
        return TodoResponse.from(findTodo(id));
    }

    @Transactional
    public TodoResponse update(Integer id, TodoUpdateRequest request) {
        Todo todo = findTodo(id);
        todo.update(request.getTitle(), request.getCompleted());

        return TodoResponse.from(todo);
    }

    @Transactional
    public void delete(Integer id) {
        todoRepository.delete(findTodo(id));
    }

    private Todo findTodo(Integer id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoException(
                        "404-1",
                        "해당 할 일을 찾을 수 없습니다. id=" + id
                ));
    }
}
