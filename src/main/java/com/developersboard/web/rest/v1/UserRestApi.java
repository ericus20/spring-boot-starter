package com.developersboard.web.rest.v1;

import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.AdminConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.enums.OperationStatus;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.web.payload.request.SignUpRequest;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
public class UserRestApi {

  private final UserService userService;
  private final JwtService jwtService;
  private final EmailService emailService;
  private final EncryptionService encryptionService;

  /**
   * Enables the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @PreAuthorize(
      "isFullyAuthenticated() && hasRole(T(com.developersboard.enums.RoleType).ROLE_ADMIN)")
  @PutMapping(value = "/{publicId}/enable", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> enableUser(@PathVariable String publicId) {
    var userDto = userService.enableUser(publicId);

    return ResponseEntity.ok(
        Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
  }

  /**
   * Disables the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @PreAuthorize(
      "isFullyAuthenticated() && hasRole(T(com.developersboard.enums.RoleType).ROLE_ADMIN)")
  @PutMapping(value = "/{publicId}/disable", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> disableUser(@PathVariable String publicId) {
    var userDto = userService.disableUser(publicId);

    return ResponseEntity.ok(
        Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
  }

  /**
   * Deletes the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @PreAuthorize(
      "isFullyAuthenticated() && hasRole(T(com.developersboard.enums.RoleType).ROLE_ADMIN)")
  @DeleteMapping(value = "/{publicId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> deleteUser(@PathVariable String publicId) {
    userService.deleteUser(publicId);

    return ResponseEntity.ok(OperationStatus.SUCCESS);
  }

  @PostMapping
  public ResponseEntity<String> createUser(
      @Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
    var userDto = UserUtils.convertToUserDto(signUpRequest);

    if (userService.existsByUsernameOrEmailAndEnabled(userDto.getUsername(), userDto.getEmail())) {
      LOG.warn(UserConstants.USERNAME_OR_EMAIL_EXITS);
      return ResponseEntity.badRequest().body(UserConstants.USERNAME_OR_EMAIL_EXITS);
    }

    var verificationToken = jwtService.generateJwtToken(userDto.getUsername());
    userDto.setVerificationToken(verificationToken);

    var savedUserDto = userService.createUser(userDto);
    if (Objects.isNull(savedUserDto)) {
      LOG.error(UserConstants.COULD_NOT_CREATE_USER);
      return ResponseEntity.badRequest().body(UserConstants.USERNAME_OR_EMAIL_EXITS);
    } else {
      var encryptedToken = encryptionService.encrypt(verificationToken);
      var encodedToken = encryptionService.encode(encryptedToken);

      emailService.sendAccountVerificationEmail(savedUserDto, encodedToken);
      var location =
          ServletUriComponentsBuilder.fromCurrentRequest()
              .path("/{publicId}")
              .buildAndExpand(savedUserDto.getId())
              .toUriString();

      return ResponseEntity.status(HttpStatus.CREATED)
          .header(HttpHeaders.LOCATION, location)
          .build();
    }
  }
}
