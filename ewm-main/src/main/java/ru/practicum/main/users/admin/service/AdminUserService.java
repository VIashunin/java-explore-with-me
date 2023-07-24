package ru.practicum.main.users.admin.service;

import ru.practicum.main.users.dto.NewUserRequest;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.model.User;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUser(int userId);

    User getUserById(int userId);
}
