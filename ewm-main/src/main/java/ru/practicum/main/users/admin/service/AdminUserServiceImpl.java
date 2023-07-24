package ru.practicum.main.users.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.users.dto.NewUserRequest;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.model.User;
import ru.practicum.main.users.repository.UserRepository;

import java.util.List;

import static ru.practicum.main.users.mapper.UserMapper.*;
import static ru.practicum.main.utilities.Paginate.paginate;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        Pageable page = paginate(from, size);
        if (ids != null && !ids.isEmpty()) {
            return mapFromUserListToUserDtoList(userRepository.findAllById(ids));
        } else {
            return mapFromUserListToUserDtoList(userRepository.findAll(page));
        }
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return mapFromUserToUserDto(userRepository.save(mapFromUserRequestToUser(newUserRequest)));
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id:" + userId + " is not found."));
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id:" + userId + " is not found."));
    }
}
