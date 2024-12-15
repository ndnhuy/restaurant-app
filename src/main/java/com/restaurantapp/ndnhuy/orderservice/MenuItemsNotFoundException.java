package com.restaurantapp.ndnhuy.orderservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.swing.*;
import java.util.List;

public class MenuItemsNotFoundException extends RuntimeException {
  public MenuItemsNotFoundException(List<Long> menuIds) {
    super("not found menu ids: " + menuIds);
  }
}
