package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    final HashMap<Long, Item> items;

    @Override
    public Item addNewItem(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item updItem) {
        Item oldItem = items.get(itemId);
        if (updItem.getDescription() != null && !updItem.getDescription().equals(oldItem.getDescription())) {
            oldItem.setDescription(updItem.getDescription());
        }
        if (updItem.getName() != null && !updItem.getName().equals(oldItem.getName())) {
            oldItem.setName(updItem.getName());
        }
        if (updItem.getAvailable() != null) {
            oldItem.setAvailable(updItem.getAvailable());
        }
        return oldItem;
    }

    @Override
    public void deleteItem(long id) {
        items.remove(id);
    }

    @Override
    public Optional<Item> findById(long id) {
        Item item = items.get(id);
        return Optional.ofNullable(item);
    }

    @Override
    public Collection<Item> findByOwner(long ownerId) {
        Collection<Item> listItem = items.values()
                .stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .toList();
        return listItem;

    }

    @Override
    public Collection<Item> findBySearch(String text) {
        if (text == null || text.isBlank()) { // Check for null, blank, or whitespace-only strings
            return Collections.emptyList(); // Return an empty list if text is not valid
        }
        String textToLower = text.toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable) // No need to compare to `true`
                .filter(item -> item.getName().toLowerCase().contains(textToLower)
                        || item.getDescription().toLowerCase().contains(textToLower))
                .toList();
    }

    private long getId() {
        long lastId = items.values().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}