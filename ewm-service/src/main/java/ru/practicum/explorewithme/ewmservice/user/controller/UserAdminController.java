package ru.practicum.explorewithme.ewmservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserDto;
import ru.practicum.explorewithme.ewmservice.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseUserDto addUser(
            @Valid @RequestBody RequestAddUserDto addUserDto
    ) {
        log.info("Add user request: {}", addUserDto);
        return userService.addUser(addUserDto);
    }
}
