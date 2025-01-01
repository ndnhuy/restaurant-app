package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.common.mocks.MockEventPublisher;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestFactory {

  @Bean
  public ApplicationEventPublisher eventPublisher(ApplicationEventPublisher origin) {
    return new MockEventPublisher(origin);
  }

}
