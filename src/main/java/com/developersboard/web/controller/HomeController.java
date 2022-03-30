package com.developersboard.web.controller;

import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.HomeConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.shared.util.SecurityUtils;
import com.developersboard.shared.util.UserUtils;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HomeController class managers all incoming request to the home page.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(HomeConstants.INDEX_URL_MAPPING)
public class HomeController {
  private final UserService userService;

  /**
   * Maps index url request.
   *
   * @return index view name
   */
  @GetMapping
  public String home() {
    // if the user is authenticated, redirect to the account overview.
    if (SecurityUtils.isAuthenticated()) {
      return HomeConstants.REDIRECT_TO_ACCOUNT_OVERVIEW;
    }

    return HomeConstants.INDEX_VIEW_NAME;
  }

  /**
   * Maps account overview url request with appropriate attributes.
   *
   * @param principal principal to retrieve current logged in user.
   * @param model model to map appropriate attributes.
   * @return account overview name.
   */
  @GetMapping(HomeConstants.ACCOUNT_OVERVIEW_URL_MAPPING)
  @PreAuthorize("isAuthenticated() and hasAnyRole(T(com.developersboard.enums.RoleType).values())")
  public String accountOverview(Principal principal, Model model) {
    var user = UserUtils.convertToUser(userService.findByUsername(principal.getName()));
    model.addAttribute(UserConstants.USER_MODEL_KEY, user);

    return HomeConstants.ACCOUNT_OVERVIEW_NAME;
  }
}
