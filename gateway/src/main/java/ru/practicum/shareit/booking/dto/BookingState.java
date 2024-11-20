package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingState {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static BookingState from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + stringState);
    }
}