package com.developersboard.config.security.jwt;

import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.shared.util.UserUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtAuthTokenFilterTest {

  private static final String bearerToken = "Bearer 260bce87-6be9-4897-add7-b3b675952538";
  private static final String token = "260bce87-6be9-4897-add7-b3b675952538";
  private static final String API_AUTH_LOGIN = "/api/auth/login";

  @Mock private transient MockFilterChain filterChain;

  @Mock private transient JwtService jwtService;

  @Mock private transient UserDetailsService userDetailsService;

  @Mock private transient EncryptionService encryptionService;

  @InjectMocks private transient JwtAuthTokenFilter jwtAuthTokenFilter;

  private transient MockHttpServletRequest request;
  private transient MockHttpServletResponse response;

  @BeforeAll
  void beforeAll() throws Exception {
    try (var mocks = MockitoAnnotations.openMocks(this)) {
      Assertions.assertNotNull(mocks);

      var userDetails = UserDetailsBuilder.buildUserDetails(UserUtils.createUser());
      Mockito.when(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString()))
          .thenReturn(userDetails);
      Mockito.when(encryptionService.decrypt(ArgumentMatchers.anyString())).thenReturn(bearerToken);

      request = new MockHttpServletRequest();
      request.setRequestURI(API_AUTH_LOGIN);

      response = new MockHttpServletResponse();
    }
  }

  @Test
  void testDoFilterInternalWhenTokenIsInHeader() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, bearerToken);

    Mockito.when(jwtService.generateJwtToken(ArgumentMatchers.anyString())).thenReturn(bearerToken);
    Mockito.when(jwtService.isValidJwtToken(ArgumentMatchers.anyString())).thenReturn(true);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void testDoFilterInternalWhenBearerTokenIsNotValid() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, bearerToken);

    Mockito.when(jwtService.generateJwtToken(ArgumentMatchers.anyString())).thenReturn(token);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void testDoFilterInternalWhenTokenIsNotWithBearer() throws ServletException, IOException {
    request.addHeader(HttpHeaders.AUTHORIZATION, token);

    Mockito.when(jwtService.generateJwtToken(ArgumentMatchers.anyString())).thenReturn(token);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void testDoFilterInternalWhenTokenIsNotInHeader() throws ServletException, IOException {

    Mockito.when(jwtService.generateJwtToken(ArgumentMatchers.anyString())).thenReturn(token);
    jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
