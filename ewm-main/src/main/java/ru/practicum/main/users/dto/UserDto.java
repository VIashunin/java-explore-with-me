package ru.practicum.main.users.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
