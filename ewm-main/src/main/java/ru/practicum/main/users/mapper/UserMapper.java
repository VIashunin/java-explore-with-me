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
                .subscriptionPermission(userRequest.getSubscriptionPermission())
                .subscriptions(List.of())
                .subscribers(List.of())
                .build();
    }

    public static UserDto mapFromUserToUserDto(User user) {
        List<Integer> subscriptionsIds = new ArrayList<>();
        List<Integer> subscribersIds = new ArrayList<>();
        if (user.getSubscriptions() != null && !user.getSubscriptions().isEmpty()) {
            for (User userFromSubscriptions : user.getSubscriptions()) {
                subscriptionsIds.add(userFromSubscriptions.getId());
            }
        }
        if (user.getSubscribers() != null && !user.getSubscribers().isEmpty()) {
            for (User userFromSubscribers : user.getSubscribers()) {
                subscribersIds.add(userFromSubscribers.getId());
            }
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .subscriptionPermission(user.isSubscriptionPermission())
                .subscriptions(subscriptionsIds)
                .subscribers(subscribersIds)
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