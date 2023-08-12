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
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseUserDto addUser(
            @Valid @RequestBody RequestAddUserDto addUserDto
    ) {
        log.info("Add user request: {}", addUserDto);
        return userService.addUser(addUserDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ResponseUserDto> findUsers(
            @RequestParam(required = false) List<Long> ids,
            @PositiveOrZero(message = "From parameter must be positive or zero")
            @RequestParam(defaultValue = "0") Integer from,
            @Positive(message = "Size parameter must be positive")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Get users request by:\n\tids = {}\n\tfrom = {}\n\tsize = {}", ids, from, size);
        return userService.findUsers(ids, from, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(
            @PathVariable Long id
    ) {
        log.info("Delete user id#{} request", id);
        userService.deleteUser(id);
    }
}
