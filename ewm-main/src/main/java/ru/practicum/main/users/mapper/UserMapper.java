package ru.practicum.main.users.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.main.users.dto.NewUserRequest;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.dto.UserShortDto;
import ru.practicum.main.users.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User mapFromUserRequestToUser(NewUserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }

    public static UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> mapFromUserListToUserDtoList(Page<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(mapFromUserToUserDto(user));
        }
        return userDtoList;
    }

    public static List<UserDto> mapFromUserListToUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(mapFromUserToUserDto(user));
        }
        return userDtoList;
    }

    public static UserShortDto mapFromUserToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
