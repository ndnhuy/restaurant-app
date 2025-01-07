package com.restaurantapp.ndnhuy.common.mocks;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class MockEventPublisher implements ApplicationEventPublisher {

  private List<Object> publishedEvents = new LinkedList<>();

  private final ApplicationEventPublisher origin;

  @Override
  public void publishEvent(Object event) {
    try {
      origin.publishEvent(event);
    } finally {
      publishedEvents.add(event);
    }
  }

  public void assertEventsPublished(Predicate<Object> eventFilter, Consumer<Object> eventAssert) {
    this.publishedEvents.stream().filter(eventFilter).forEach(eventAssert);
  }
}
