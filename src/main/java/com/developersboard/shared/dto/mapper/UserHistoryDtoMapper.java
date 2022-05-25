package com.developersboard.shared.dto.mapper;

import com.developersboard.backend.persistent.domain.user.UserHistory;
import com.developersboard.shared.dto.UserHistoryDto;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The UserDtoMapper class outlines the supported conversions between User and other objects.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserHistoryDtoMapper {

  UserHistoryDtoMapper MAPPER = Mappers.getMapper(UserHistoryDtoMapper.class);

  /**
   * Convert and populate a userHistories to userHistoryDto object.
   *
   * @param userHistories the userHistories
   * @return the userHistoryDto
   */
  List<UserHistoryDto> toUserHistoryDto(Set<UserHistory> userHistories);

  /**
   * Convert and populate a userHistory to userHistoryDto object.
   *
   * @param userHistory the userHistory
   * @return the userHistoryDto
   */
  @Mapping(
      target = "timeElapsedDescription",
      expression =
          "java(com.developersboard.shared.util.core.DateUtils.getTimeElapsedDescription(userHistory.getCreatedAt()))")
  @Mapping(
      target = "separateDateFormat",
      expression =
          "java(com.developersboard.shared.util.core.DateUtils.getTimeElapsed(userHistory.getCreatedAt()))")
  UserHistoryDto toUserHistoryDto(UserHistory userHistory);
}
