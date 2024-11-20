package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.booking.services.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    static final String userParmHeader = "X-Sharer-User-Id";

    @PostMapping
    public OutputBookingDto create(@RequestHeader(userParmHeader) long userId,
                                   @RequestBody BookingDto bookingDto) {
        log.info("==>Создание Booking: ", bookingDto);
        OutputBookingDto bookingDtoNew = bookingService.create(bookingDto, userId);
        return bookingDtoNew;
    }

    @PatchMapping("/{bookingId}")
    public OutputBookingDto approve(@RequestHeader(userParmHeader) long ownerId,
                                    @PathVariable(name = "bookingId") long bookingId,
                                    @RequestParam(value = "approved") boolean approved) {
        log.info("==> Подтверждение  Booking: владельцем :userId", bookingId, ownerId);
        BookingApproveDto bookingApproveDto = BookingApproveDto.builder()
                .id(bookingId)
                .approved(approved)
                .build();
        OutputBookingDto bookingDto = bookingService.approve(bookingApproveDto, ownerId);
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public OutputBookingDto getBooking(@PathVariable(name = "bookingId") long bookingId,
                                       @RequestHeader(userParmHeader) long userId) {
        log.info("==> Получение данных о бронировании :bookingId", bookingId);
        OutputBookingDto bookingDto = bookingService.findById(bookingId, userId);
        return bookingDto;
    }

    @GetMapping
    public List<OutputBookingDto> getBookings(@RequestParam(value = "state", defaultValue = "ALL") String status,
                                              @RequestHeader(userParmHeader) long bookerId) {
        log.info("==> Получение бронирований пользователя :bookingId", bookerId);
        List<OutputBookingDto> listBookingDto = bookingService.findByBookerId(bookerId, status);
        return listBookingDto;
    }

    @GetMapping("/owner")
    public List<OutputBookingDto> getOwnerBookings(@RequestParam(value = "state", defaultValue = "ALL") String status,
                                                   @RequestHeader(userParmHeader) long ownerId) {
        log.info("==>  Получение бронирований по владельцу :ownerid", ownerId);

        List<OutputBookingDto> listBookingDto = bookingService.findByOwnerId(ownerId, status);
        return listBookingDto;
    }
}