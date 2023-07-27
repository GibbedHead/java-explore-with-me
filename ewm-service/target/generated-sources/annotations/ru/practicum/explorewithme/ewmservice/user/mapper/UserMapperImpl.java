package ru.practicum.explorewithme.ewmservice.user.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.ewmservice.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserDto;
import ru.practicum.explorewithme.ewmservice.user.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-27T23:42:55+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User addDtoToUser(RequestAddUserDto addUserDto) {
        if ( addUserDto == null ) {
            return null;
        }

        User user = new User();

        user.setName( addUserDto.getName() );
        user.setEmail( addUserDto.getEmail() );

        return user;
    }

    @Override
    public ResponseUserDto userToResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;

        id = user.getId();
        name = user.getName();
        email = user.getEmail();

        ResponseUserDto responseUserDto = new ResponseUserDto( id, name, email );

        return responseUserDto;
    }
}
