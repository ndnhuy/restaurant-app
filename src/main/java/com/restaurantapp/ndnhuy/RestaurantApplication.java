package com.restaurantapp.ndnhuy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RestaurantApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestaurantApplication.class, args);
  }

  @GetMapping("/hello")
  public String hello(@RequestParam String name) {
    return "Hello " + name;
  }
}
