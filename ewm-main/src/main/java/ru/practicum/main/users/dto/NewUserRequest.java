package ru.practicum.main.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    @Email
    @Length(min = 6, max = 254)
    private String email;
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
    @Builder.Default
    private Boolean subscriptionPermission = true;
    @Builder.Default
    private List<Integer> subscriptionsIds = new ArrayList<>();
    @Builder.Default
    private List<Integer> subscribersIds = new ArrayList<>();
}