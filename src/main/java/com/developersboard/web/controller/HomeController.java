package com.developersboard.web.controller;

import com.developersboard.constant.HomeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HomeController class managers all incoming request to the home page.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(HomeConstants.INDEX_URL_MAPPING)
public class HomeController {

  @Value("${admin.username}")
  private String adminUsername;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.password}")
  private String adminPassword;

  /**
   * Maps index url request.
   *
   * @return index view name
   */
  @GetMapping
  public String home(Model model) {
    model.addAttribute("adminUsername", adminUsername);
    model.addAttribute("adminEmail", adminEmail);
    model.addAttribute("adminPassword", adminPassword);

    return HomeConstants.INDEX_VIEW_NAME;
  }
}
