package com.developersboard.web.rest.v1;

import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.AdminConstants;
import com.developersboard.enums.OperationStatus;
import com.developersboard.shared.dto.UserDto;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles all rest calls for users.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AdminConstants.API_V1_USERS_ROOT_URL)
@PreAuthorize("isFullyAuthenticated() && hasRole(T(com.developersboard.enums.RoleType).ROLE_ADMIN)")
public class UserRestApi {

  private final UserService userService;

  /**
   * Enables the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @PutMapping("/{publicId}/enable")
  public ResponseEntity<OperationStatus> enableUser(@PathVariable String publicId) {
    UserDto userDto = userService.enableUser(publicId);
    return ResponseEntity.ok(
        Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
  }

  /**
   * Disables the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @PutMapping("/{publicId}/disable")
  public ResponseEntity<OperationStatus> disableUser(@PathVariable String publicId) {
    UserDto userDto = userService.disableUser(publicId);
    return ResponseEntity.ok(
        Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
  }
}
