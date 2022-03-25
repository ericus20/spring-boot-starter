package com.developersboard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class SpringBootStarterApplicationTests {

  @Mock private transient ConfigurableApplicationContext applicationContext;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testClassConstructor() {
    Assertions.assertDoesNotThrow(SpringBootStarterApplication::new);
  }

  /** Test the main method with mocked application context. */
  @Test
  /* default */ void contextLoads() {
    var mockStatic = Mockito.mockStatic(SpringApplication.class);
    SpringBootStarterApplication.main(new String[] {});
    mockStatic
        .when(() -> SpringApplication.run(SpringBootStarterApplication.class))
        .thenReturn(applicationContext);
    mockStatic.verify(() -> SpringApplication.run(SpringBootStarterApplication.class));
  }
}
