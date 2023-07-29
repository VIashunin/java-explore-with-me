package ru.practicum.main.users.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private String email;
    private int id;
    private String name;
    private Boolean subscriptionPermission;
    private List<Integer> subscriptions;
    private List<Integer> subscribers;
}
