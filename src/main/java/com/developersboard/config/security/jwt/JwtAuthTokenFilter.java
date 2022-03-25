package com.developersboard.config.security.jwt;

import com.developersboard.backend.service.security.EncryptionService;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.shared.util.SecurityUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This is a filter base class that is used to guarantee a single execution per request dispatch.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final EncryptionService encryptionService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    var jwt = jwtService.getJwtToken(request, false);
    if (StringUtils.isNotBlank(jwt)) {
      String decryptedAccessToken = encryptionService.decrypt(jwt);

      if (StringUtils.isNotBlank(decryptedAccessToken)
          && jwtService.isValidJwtToken(decryptedAccessToken)) {

        var username = jwtService.getUsernameFromToken(decryptedAccessToken);
        var userDetails = userDetailsService.loadUserByUsername(username);
        var authorities = userDetails.getAuthorities();
        var authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityUtils.setAuthentication(authentication);
      }
    }
    filterChain.doFilter(request, response);
  }
}
