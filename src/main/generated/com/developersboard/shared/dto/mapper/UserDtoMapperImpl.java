package com.developersboard.shared.dto.mapper;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.persistent.domain.user.UserHistory;
import com.developersboard.backend.persistent.domain.user.UserRole;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.web.payload.response.UserResponse;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-27T23:31:46-0400",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
public class UserDtoMapperImpl implements UserDtoMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setCreatedAt( user.getCreatedAt() );
        userDto.setUpdatedAt( user.getUpdatedAt() );
        userDto.setId( user.getId() );
        if ( user.getVersion() != null ) {
            userDto.setVersion( user.getVersion().intValue() );
        }
        userDto.setCreatedBy( user.getCreatedBy() );
        userDto.setUpdatedBy( user.getUpdatedBy() );
        userDto.setPublicId( user.getPublicId() );
        userDto.setUsername( user.getUsername() );
        userDto.setPassword( user.getPassword() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setMiddleName( user.getMiddleName() );
        userDto.setLastName( user.getLastName() );
        userDto.setEmail( user.getEmail() );
        userDto.setPhone( user.getPhone() );
        userDto.setEnabled( user.isEnabled() );
        userDto.setVerificationToken( user.getVerificationToken() );
        Set<UserRole> set = user.getUserRoles();
        if ( set != null ) {
            userDto.setUserRoles( new HashSet<UserRole>( set ) );
        }
        Set<UserHistory> set1 = user.getUserHistories();
        if ( set1 != null ) {
            userDto.setUserHistories( new HashSet<UserHistory>( set1 ) );
        }

        return userDto;
    }

    @Override
    public UserDto toUserDto(UserDetailsBuilder userDetailsBuilder) {
        if ( userDetailsBuilder == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( userDetailsBuilder.getId() );
        userDto.setPublicId( userDetailsBuilder.getPublicId() );
        userDto.setUsername( userDetailsBuilder.getUsername() );
        userDto.setPassword( userDetailsBuilder.getPassword() );
        userDto.setFirstName( userDetailsBuilder.getFirstName() );
        userDto.setLastName( userDetailsBuilder.getLastName() );
        userDto.setEmail( userDetailsBuilder.getEmail() );
        userDto.setPhone( userDetailsBuilder.getPhone() );
        userDto.setEnabled( userDetailsBuilder.isEnabled() );

        return userDto;
    }

    @Override
    public User toUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setPublicId( userDto.getPublicId() );
        user.setVersion( (long) userDto.getVersion() );
        user.setCreatedAt( userDto.getCreatedAt() );
        user.setCreatedBy( userDto.getCreatedBy() );
        user.setUpdatedAt( userDto.getUpdatedAt() );
        user.setUpdatedBy( userDto.getUpdatedBy() );
        user.setUsername( userDto.getUsername() );
        user.setEmail( userDto.getEmail() );
        user.setPassword( userDto.getPassword() );
        user.setFirstName( userDto.getFirstName() );
        user.setMiddleName( userDto.getMiddleName() );
        user.setLastName( userDto.getLastName() );
        user.setPhone( userDto.getPhone() );
        user.setVerificationToken( userDto.getVerificationToken() );
        user.setEnabled( userDto.isEnabled() );
        Set<UserRole> set = userDto.getUserRoles();
        if ( set != null ) {
            user.setUserRoles( new HashSet<UserRole>( set ) );
        }
        Set<UserHistory> set1 = userDto.getUserHistories();
        if ( set1 != null ) {
            user.setUserHistories( new HashSet<UserHistory>( set1 ) );
        }

        return user;
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setPublicId( user.getPublicId() );
        userResponse.setUsername( user.getUsername() );
        userResponse.setFirstName( user.getFirstName() );
        userResponse.setLastName( user.getLastName() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setPhone( user.getPhone() );
        userResponse.setEnabled( user.isEnabled() );

        return userResponse;
    }
}
