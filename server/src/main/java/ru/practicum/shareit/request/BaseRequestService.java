package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BaseRequestService implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemRequestInfoDto> findAllByUserId(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorId(userId);
        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        List<Item> items = itemRepository.findAllByItemRequestIds(itemRequestIds);

        Map<Long, List<Item>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(item -> item.getItemRequest().getId()));

        return itemRequests.stream()
                .map(itemRequest -> {
                    ItemRequestInfoDto itemRequestInfoDto = RequestMapper.toItemRequestDto(itemRequest);
                    itemRequestInfoDto.setItems(ItemMapper.toItemsDtoForRequest(itemsByRequestId.getOrDefault(itemRequest.getId(), Collections.emptyList())));
                    return itemRequestInfoDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestInfoDto create(ItemRequestDto itemRequestRequestDto) {
        User requestor = userRepository.findById(itemRequestRequestDto.getRequestorId()).orElseThrow(() -> {
            throw new NotFoundException("User с id = " + itemRequestRequestDto.getRequestorId() + " не найден!");
        });


        ItemRequest itemRequest = itemRequestRepository.save(
                ItemRequest.builder()
                        .description(itemRequestRequestDto.getDescription())
                        .requestor(requestor)
                        .created(LocalDateTime.now())
                        .build()
        );
        log.info("Запрос создан");
        return RequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestInfoDto findItemRequestById(Long itemRequestId, Long userId) {
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request с id = %d не найден.", itemRequestId)));

        List<Item>  items = itemRepository.findAllByItemRequest(itemRequest);
        ItemRequestInfoDto itemRequestInfoDto = RequestMapper.toItemRequestDto(itemRequest);
        itemRequestInfoDto.setItems(ItemMapper.toItemsDtoForRequest(items));
        return itemRequestInfoDto;
    }

    @Override
    public List<ItemRequestInfoDto> findAllUsersItemRequest() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAll();
        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        List<Item> items = itemRepository.findAllByItemRequestIds(itemRequestIds);

        Map<Long, List<Item>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(item -> item.getItemRequest().getId()));

        return itemRequests.stream()
                .map(itemRequest -> {
                    ItemRequestInfoDto itemRequestInfoDto = RequestMapper.toItemRequestDto(itemRequest);
                    itemRequestInfoDto.setItems(ItemMapper.toItemsDtoForRequest(itemsByRequestId.getOrDefault(itemRequest.getId(), Collections.emptyList())));
                    return itemRequestInfoDto;
                })
                .collect(Collectors.toList());
    }
}