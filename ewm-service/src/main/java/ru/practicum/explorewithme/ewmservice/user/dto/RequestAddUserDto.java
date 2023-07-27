package ru.practicum.explorewithme.ewmservice.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestAddUserDto {
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
}
