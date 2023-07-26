package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.user.dto.ResponseUserDto;
import ru.practicum.explorewithme.user.mapper.UserMapper;
import ru.practicum.explorewithme.user.repository.UserRepository;

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
}
