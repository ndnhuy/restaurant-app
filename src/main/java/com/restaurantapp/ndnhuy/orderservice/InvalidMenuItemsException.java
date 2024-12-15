package com.restaurantapp.ndnhuy.orderservice;

import com.restaurantapp.ndnhuy.restaurantservice.MenuItem;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid menus: {menus}; {msg}")
public class InvalidMenuItemsException extends RuntimeException {
  public InvalidMenuItemsException(List<RestaurantDTO.MenuItemDTO> menus, String msg) {
    super("invalid menus: " + menus + "; " + msg);
  }
}
