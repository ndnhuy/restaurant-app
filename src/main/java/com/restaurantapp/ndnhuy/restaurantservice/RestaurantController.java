package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.orderservice.CreateOrderRequest;
import com.restaurantapp.ndnhuy.orderservice.CreateOrderResponse;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantDTO.MenuItemDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/restaurants")
public class RestaurantController {

  private RestaurantService service;

  @PostMapping
  public CreateRestaurantResponse createRestaurant(
      @Valid @RequestBody CreateRestaurantRequest req) {
    var r = service.createRestaurant(req.getName(), req.getMenuItems());
    return CreateRestaurantResponse.builder().restaurantId(r.getId()).build();
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<GetRestaurantResponse> getRestaurant(@PathVariable long id) {
    return service
        .getRestaurantById(id)
        .map(e -> GetRestaurantResponse
            .builder()
            .id(e.getId())
            .name(e.getName())
            .menuItems(
                e
                    .getMenuItems()
                    .stream()
                    .map(it -> MenuItemDTO
                        .builder()
                        .name(it.getName())
                        .price(it.getPrice())
                        .build())
                    .toList())
            .build())
        .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // return service
    // .getRestaurantById(id)
    // .map(r ->
    // GetRestaurantResponse
    // .builder()
    // .id(r.getId())
    // .name(r.getName())
    // .menuItems(r.getMenuItems())
    // .build()
    // )
    // .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
    // .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
