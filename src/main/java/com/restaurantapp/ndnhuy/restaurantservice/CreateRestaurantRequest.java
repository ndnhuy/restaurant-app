package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateRestaurantRequest {

  @NotBlank(message = "name must not be empty")
  private String name;

  @NotEmpty(message = "menu items must be provided")
  private List<Restaurant.MenuItem> menuItems;
}
