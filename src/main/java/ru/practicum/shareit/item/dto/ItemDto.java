package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private  Long id;
    @NotBlank
    private  String name;
    @NotBlank
    private  String description;
    @BooleanFlag
    @NotNull
    private  Boolean available;
    private  Long request;
}