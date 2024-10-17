package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    static final String userParamHeader = "X-Sharer-User-Id";

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable long id) {
        log.info("Getting Item by id: {}", id);
        return service.findById(id);
    }

    @GetMapping
    public Collection<ItemDto> getByOwnerId(@RequestHeader(userParamHeader) long userId) {
        log.info("Getting Items by Owner: {}", userId);
        return service.findByOwner(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemBySearch(@RequestParam String text) {
        log.info("Searching Items with: {}", text);
        return service.findBySearch(text);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(userParamHeader) long userId,
                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating Item: {} with owner {}", itemDto, userId);
        return service.addNewItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(userParamHeader) long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Updating Item: {} with owner {}", itemDto, userId);
        return service.updateItem(itemId, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("Deleting Item with id: {}", id);
        service.delete(id);
    }

}