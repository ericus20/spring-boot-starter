package com.developersboard.web.controller;

import com.developersboard.constant.HomeConstants;
import org.springframework.stereotype.Controller;
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
@RequestMapping(HomeConstants.INDEX_URL_MAPPING)
public class HomeController {

  /**
   * Maps index url request.
   *
   * @return index view name
   */
  @GetMapping
  public String index() {
    return HomeConstants.INDEX_VIEW_NAME;
  }
}
