package ru.practicum.explorewithme.ewmservice.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestAddUserDto {
    @NotBlank
    @Size(min = 2, max = 250, message = "Name length must be between 2 and 250")
    String name;
    @NotBlank
    @Email
    @Size(min = 6, max = 254, message = "Email length must be between 6 and 254")
    String email;
}
