package ru.practicum.explorewithme.ewmservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserDto;
import ru.practicum.explorewithme.ewmservice.user.mapper.UserMapper;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public ResponseUserDto addUser(RequestAddUserDto addUserDto) {
        ResponseUserDto savedUser = userMapper.userToResponseDto(
                userRepository.save(
                        userMapper.addDtoToUser(addUserDto)
                )
        );
        log.info("User saved:  {}", savedUser);
        return savedUser;
    }

    @Override
    public Collection<ResponseUserDto> findUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<User> foundUsers = userRepository.findByIdIn(ids, pageable);
        log.info("Found {} users", foundUsers.size());
        return foundUsers.stream()
                .map(userMapper::userToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            log.info("User id#{} deleted", id);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("User id#%d not found", id);
            log.error(message);
            throw new EntityNotFoundException(message);
        }
    }
}
