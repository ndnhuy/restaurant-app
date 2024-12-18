package com.restaurantapp.ndnhuy.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtils {

  public static Long randomPositiveLong() {
    return ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
  }
}
