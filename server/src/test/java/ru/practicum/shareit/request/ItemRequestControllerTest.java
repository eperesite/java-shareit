package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.services.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemRequestService requestService;

    public ItemRequestInfoDto getItemRequestInfoDto() {
        return ItemRequestInfoDto.builder()
                .id(1L)
                .description("TestItemRequestDescription")
                .requestor(null)
                .created(LocalDateTime.now())
                .items(List.of())
                .build();
    }

    public ItemRequestDto getItemRequestDto() {
        return ItemRequestDto.builder()
                .description("TestItemRequestDescription")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void create() throws Exception {
        ItemRequestDto itemRequestDto = getItemRequestDto();
        ItemRequestInfoDto itemRequestInfoDto = getItemRequestInfoDto();

        when(requestService.create(any())).thenReturn(itemRequestInfoDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        verify(requestService).create(any(ItemRequestDto.class));
    }

    @Test
    void findAllByUserIdTest() throws Exception {
        ItemRequestInfoDto itemRequestInfoDto = getItemRequestInfoDto();

        when(requestService.findAllByUserId(anyLong())).thenReturn(List.of(itemRequestInfoDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestInfoDto))));
    }

    @Test
    void findItemRequestByIdTest() throws Exception {
        ItemRequestInfoDto itemRequestInfoDto = getItemRequestInfoDto();

        when(requestService.findItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestInfoDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestInfoDto)));
    }

    @Test
    void findAllUsersItemRequestTest() throws Exception {
        ItemRequestInfoDto itemRequestInfoDto = getItemRequestInfoDto();

        when(requestService.findAllUsersItemRequest(anyLong())).thenReturn(List.of(itemRequestInfoDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestInfoDto))));
    }

    @Test
    void findAll() throws Exception {
        when(requestService.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(requestService, times(1)).findAllByUserId(anyLong());
    }
}
