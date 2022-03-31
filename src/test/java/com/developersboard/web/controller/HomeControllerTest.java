package com.developersboard.web.controller;

import com.developersboard.constant.HomeConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit Test HomeController.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

  @InjectMocks private transient HomeController homeController;

  private transient MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
  }

  @Test
  void shouldReturnIndexViewName() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(HomeConstants.INDEX_URL_MAPPING))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name(HomeConstants.INDEX_VIEW_NAME));
  }
}
