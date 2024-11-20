package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestorId(Long requestorId);

    @Query("SELECT r FROM ItemRequest r WHERE r.requestor.id <> :userId")
    List<ItemRequest> findAllByOtherUsers(@Param("userId") Long userId);
}
