package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.booking.services.BookingService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.services.UserService;

import java.util.List;

import static ru.practicum.shareit.constans.Constants.USER_PARAM_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public OutputBookingDto create(@RequestHeader(USER_PARAM_HEADER) long userId,
                                   @RequestBody @Valid BookingDto bookingDto) {
        log.info("==> Создание Booking: {}", bookingDto);
        OutputBookingDto bookingDtoNew = bookingService.create(bookingDto, userId);
        log.info("<== Создан Booking");
        return bookingDtoNew;
    }

    @PatchMapping("/{bookingId}")
    public OutputBookingDto approve(@PathVariable(name = "bookingId") long bookingId,
                                    @RequestParam(value = "approved") boolean approved,
                                    @RequestHeader(USER_PARAM_HEADER) long ownerId) {
        log.info("==> Подтверждение Booking владельцем: {}", ownerId);
        OutputBookingDto bookingDto = bookingService.approve(bookingId, approved, ownerId);
        log.info("<== Booking подтвержден/отклонен: {}", bookingId);
        return bookingDto;
    }


    @GetMapping("/{bookingId}")
    public OutputBookingDto getBooking(@PathVariable(name = "bookingId") long bookingId,
                                       @RequestHeader(USER_PARAM_HEADER) long userId) {
        log.info("==> Получение данных о бронировании :bookingId", bookingId);
        OutputBookingDto bookingDto = bookingService.findById(bookingId, userId);
        log.info("<== Получены данных о бронировании :bookingId", bookingId);
        return bookingDto;
    }

    @GetMapping
    public List<OutputBookingDto> getBookings(@RequestParam(value = "state", defaultValue = "ALL") String status,
                                              @RequestHeader(USER_PARAM_HEADER) long bookerId) {
        log.info("==> Получение бронирований пользователя :bookingId", bookerId);
        List<OutputBookingDto> listBookingDto = bookingService.findByBookerId(bookerId, status);
        log.info("<== Получены бронирования пользователя :bookingId", bookerId);
        return listBookingDto;
    }

    @GetMapping("/owner")
    public List<OutputBookingDto> getOwnerBookings(@RequestParam(value = "state", defaultValue = "ALL") String status,
                                                   @RequestHeader(USER_PARAM_HEADER) long ownerId) {
        log.info("==>  Получение бронирований по владельцу :ownerid", ownerId);

        List<OutputBookingDto> listBookingDto = bookingService.findByOwnerId(ownerId, status);
        log.info("Получены бронирования владельца :ownerid", ownerId);
        return listBookingDto;
    }
}
