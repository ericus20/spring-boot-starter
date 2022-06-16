package com.developersboard.shared.util.core;

import com.developersboard.constant.user.SignUpConstants;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class WebUtilsTest {

  @Mock private transient HttpServletRequest request;

  @BeforeEach
  public void setup() {
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(WebUtils.class));
  }

  @Test
  void generateGenericUri() {
    // Context path will be relative and in this case it will be empty
    var genericUri = WebUtils.getGenericUri(SignUpConstants.SIGN_UP_MAPPING);
    Assertions.assertEquals(SignUpConstants.SIGN_UP_MAPPING, genericUri);
  }

  @Test
  void generateGenericUriWithPublicId() {
    var publicId = UUID.randomUUID().toString();
    var genericUri = WebUtils.getGenericUri(SignUpConstants.SIGN_UP_VERIFY_MAPPING, publicId);

    var expected =
        String.format("%s?%s=%s", SignUpConstants.SIGN_UP_VERIFY_MAPPING, WebUtils.TOKEN, publicId);
    Assertions.assertEquals(expected, genericUri);
  }
}
