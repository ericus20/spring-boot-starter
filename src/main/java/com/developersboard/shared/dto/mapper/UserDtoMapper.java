package com.developersboard.shared.dto.mapper;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.web.payload.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The UserDtoMapper class outlines the supported conversions between User entity and other data
 * transfer objects.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDtoMapper {

  UserDtoMapper MAPPER = Mappers.getMapper(UserDtoMapper.class);

  /**
   * Convert and populate a user to userDto object.
   *
   * @param user the user
   * @return the userDto
   */
  UserDto toUserDto(User user);

  /**
   * Convert and populate a userDto to User object.
   *
   * @param userDetailsBuilder the userDetailsBuilder
   * @return the user
   */
  UserDto toUserDto(UserDetailsBuilder userDetailsBuilder);

  /**
   * Convert and populate a userDto to User object.
   *
   * @param userDto the userDto
   * @return the user
   */
  User toUser(UserDto userDto);

  /**
   * Convert and populate a User to UserResponse object.
   *
   * @param user the user
   * @return the userResponse
   */
  UserResponse toUserResponse(User user);
}
