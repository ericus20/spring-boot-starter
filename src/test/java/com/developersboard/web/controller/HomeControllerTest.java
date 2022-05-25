package com.developersboard.web.controller;

import com.developersboard.constant.HomeConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class HomeControllerTest {

  private transient MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(HomeController.class).build();
  }

  @Test
  void testRootPathAndViewName() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(HomeConstants.INDEX_URL_MAPPING))
        .andExpect(MockMvcResultMatchers.view().name(HomeConstants.INDEX_VIEW_NAME))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
