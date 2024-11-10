package ru.practicum.shareit.user.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        User oldUser = getUser(userDto.getId());
        if (userDto.getEmail() != null && !userDto.getEmail().equals(oldUser.getEmail())) {
            oldUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().equals(oldUser.getName())) {
            oldUser.setName(userDto.getName());
        }
        return UserMapper.toUserDto(userRepository.save(oldUser));
    }

    @Override
    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(long id) {
        return UserMapper.toUserDto(getUser(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
    }
}