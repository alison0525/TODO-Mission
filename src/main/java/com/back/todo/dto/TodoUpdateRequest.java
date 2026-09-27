package com.back.todo.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoUpdateRequest {

    @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
    @Pattern(regexp = ".*\\S.*", message = "제목은 공백만 입력할 수 없습니다.")
    private String title;

    private Boolean completed;
}
