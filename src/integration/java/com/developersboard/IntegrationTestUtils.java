package com.developersboard;

import com.developersboard.backend.service.i18n.I18NService;
import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.backend.service.security.AuditService;
import com.developersboard.backend.service.security.CookieService;
import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.backend.service.storage.AmazonS3Service;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.backend.service.user.impl.RoleServiceImpl;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.config.properties.SystemProperties;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.task.UserPruningScheduler;
import com.icegreen.greenmail.util.GreenMail;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import net.datafaker.Faker;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({ProfileTypeConstants.TEST})
public abstract class IntegrationTestUtils {
  protected static final Faker FAKER = new Faker();

  @Autowired protected transient MockMvc mockMvc;
  @Autowired protected transient AuditService auditService;
  @Autowired protected transient AwsProperties awsProperties;
  @Autowired protected transient AmazonS3Service amazonS3Service;
  @Autowired protected transient CookieService cookieService;
  @Autowired protected transient JwtService jwtService;
  @Autowired protected transient EncryptionService encryptionService;
  @Autowired protected transient I18NService i18NService;
  @Autowired protected transient UserService userService;
  @Autowired protected transient RoleServiceImpl roleService;
  @Autowired protected transient UserPruningScheduler userPruningScheduler;
  @Autowired protected transient SystemProperties systemProperties;
  @Autowired protected transient EmailService emailService;
  @Autowired protected transient GreenMail greenMail;

  @Mock protected transient MockMultipartFile multipartFile;

  // We are mocking the entire dateTimeProvider since there is only one method in it.
  @MockBean protected transient DateTimeProvider dateTimeProvider;
  // We want to mock just the dateTimeProvider method within the auditHandler
  @SpyBean protected transient AuditingHandler auditingHandler;

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param username if a custom username is needed
   * @param enabled if the user should be enabled
   * @return persisted user
   */
  protected UserDto createAndAssertUser(String username, boolean enabled) {
    UserDto userDto = UserUtils.createUserDto(username);
    UserDto persistedUser = persistUser(enabled, userDto);
    Assertions.assertNotNull(persistedUser);
    Assertions.assertNotNull(persistedUser.getId());
    return persistedUser;
  }

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param userDto the userDto
   * @return persisted user
   */
  protected UserDto createAndAssertUser(UserDto userDto) {
    UserDto persistUser = persistUser(userDto.isEnabled(), SerializationUtils.clone(userDto));
    Assertions.assertNotNull(persistUser);
    Assertions.assertNotNull(persistUser.getId());
    Assertions.assertFalse(persistUser.getUserRoles().isEmpty());

    return persistUser;
  }

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param userDto the userDto
   * @return persisted user
   */
  protected UserDto createAndAssertAdmin(UserDto userDto) {
    Set<RoleType> roleTypes = new HashSet<>();

    roleTypes.add(RoleType.ROLE_ADMIN);

    if (userDto.isEnabled()) {
      UserUtils.enableUser(userDto);
    }
    UserDto admin = userService.createUser(SerializationUtils.clone(userDto), roleTypes);

    Assertions.assertNotNull(admin);
    Assertions.assertNotNull(admin.getId());
    Assertions.assertFalse(admin.getUserRoles().isEmpty());
    Assertions.assertTrue(
        admin.getUserRoles().stream()
            .anyMatch(role -> role.getRole().getName().equals(RoleType.ROLE_ADMIN.getName())));

    return admin;
  }

  protected UserDto persistUser(boolean enabled, UserDto userDto) {
    Set<RoleType> roleTypes = new HashSet<>();

    roleTypes.add(RoleType.ROLE_USER);

    if (enabled) {
      UserUtils.enableUser(userDto);
    }
    return userService.createUser(userDto, roleTypes);
  }

  protected MockMultipartFile getMultipartFile(String fileName) {
    return getMultipartFile(fileName, false);
  }

  protected MockMultipartFile getMultipartFile(String fileName, boolean empty) {
    return new MockMultipartFile(
        fileName,
        String.format("%s.png", fileName),
        "image",
        empty ? "".getBytes(StandardCharsets.UTF_8) : fileName.getBytes(StandardCharsets.UTF_8));
  }
}
