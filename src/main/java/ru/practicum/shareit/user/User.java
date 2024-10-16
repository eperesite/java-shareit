package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "Наименование должен быть указано")
    private String name;
    @Email(message = "User's email has wrong format")
    @NotBlank(message = "User's email missing")
    private String email;
}
