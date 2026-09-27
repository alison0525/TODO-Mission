package com.back.todo.controller;

import com.back.global.rsdata.RsData;
import com.back.todo.dto.TodoCreateRequest;
import com.back.todo.dto.TodoResponse;
import com.back.todo.dto.TodoUpdateRequest;
import com.back.todo.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<RsData<TodoResponse>> create(
            @Valid @RequestBody TodoCreateRequest request
    ) {
        TodoResponse response = todoService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RsData.success("할 일이 생성되었습니다.", response));
    }

    @GetMapping
    public ResponseEntity<RsData<List<TodoResponse>>> findAll() {
        List<TodoResponse> response = todoService.findAll();

        return ResponseEntity.ok(
                RsData.success("할 일 목록을 조회했습니다.", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RsData<TodoResponse>> findById(
            @PathVariable("id") Integer id
    ) {
        TodoResponse response = todoService.findById(id);

        return ResponseEntity.ok(
                RsData.success("할 일을 조회했습니다.", response)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RsData<TodoResponse>> update(
            @PathVariable("id") Integer id,
            @Valid @RequestBody TodoUpdateRequest request
    ) {
        TodoResponse response = todoService.update(id, request);

        return ResponseEntity.ok(
                RsData.success("할 일이 수정되었습니다.", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<Void>> delete(
            @PathVariable("id") Integer id
    ) {
        todoService.delete(id);

        return ResponseEntity.ok(
                RsData.success("할 일이 삭제되었습니다.", null)
        );
    }
}
