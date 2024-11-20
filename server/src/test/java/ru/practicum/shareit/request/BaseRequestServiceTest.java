package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BaseRequestServiceTest {

    Long userRequestor = 3L;
    Long otherUser = 4L;
    Long userRequestorNotValid = 2L;
    Long userRequestorNotExist = 101L;
    Long requestId = 1L;
    private final BaseRequestService service;

    ItemRequestInfoDto createRequest(Long userRequestorId) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("спальник для похода")
                .requestorId(userRequestorId)
                .build();

        return service.create(itemRequestDto);
    }

    @Test
    void createItemRequestTest() {
        ItemRequestInfoDto itemRequestCreatedDto = createRequest(userRequestor);
        ItemRequestInfoDto itemRequestFindDto = service.findItemRequestById(itemRequestCreatedDto.getId(), userRequestor);
        assertEquals(itemRequestCreatedDto, itemRequestFindDto);
    }

    @Test
    void createItemRequestNotValidUserTest() {
        assertThrows(NotFoundException.class, () -> createRequest(userRequestorNotExist),
                "Нет сообщения, что пользователь не найден.");
    }

    @Test
    void findItemRequestNotFoundTest() {
        assertThrows(NotFoundException.class, () -> service.findItemRequestById(requestId, userRequestorNotValid),
                "Нет сообщения, что запрос не найден.");
    }

    @Test
    void findAllByUserIdTest() {
        createRequest(userRequestor);
        assertEquals(1, service.findAllByUserId(userRequestor).size());
    }

    @Test
    void findAllUsersItemRequestTest() {
        createRequest(userRequestor);
        createRequest(otherUser);

        assertEquals(1, service.findAllUsersItemRequest(userRequestor).size());
    }
}
