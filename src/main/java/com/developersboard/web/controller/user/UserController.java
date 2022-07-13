package com.developersboard.web.controller.user;

import com.developersboard.annotation.Loggable;
import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.service.security.AuditService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.web.payload.response.UserResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This controller handles all requests from the browser relating to user profile.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("isAuthenticated() and hasAnyRole(T(com.developersboard.enums.RoleType).ROLE_ADMIN)")
public class UserController {

  private final UserService userService;
  private final AuditService auditService;

  /** View user's page. */
  @Loggable
  @GetMapping
  public String users() {
    return "user/index";
  }

  /**
   * Retrieves the users in the application.
   *
   * @return list of users page
   */
  @Loggable
  @PostMapping("/datatables")
  public @ResponseBody DataTablesOutput<UserResponse> getUsers(
      @RequestBody @Valid DataTablesInput input) {
    return userService.getUsers(input);
  }

  @GetMapping("/audits")
  public String auditHistory(Model model) {
    var auditLogs = auditService.getAuditLogs(User.class, true, true, true);

    model.addAttribute("auditLogs", auditLogs);
    return "user/index";
  }
}
