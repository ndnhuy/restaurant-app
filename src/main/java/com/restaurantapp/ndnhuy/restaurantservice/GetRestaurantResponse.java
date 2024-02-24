package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.restaurantservice.RestaurantDTO.MenuItemDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class GetRestaurantResponse {

  private long id;

  private String name;

  private List<MenuItemDTO> menuItems;
}
