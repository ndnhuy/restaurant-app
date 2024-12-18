package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.orderservice.*;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantDTO.MenuItemDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/restaurants")
@Slf4j
public class RestaurantController {

  private RestaurantService service;

  @PostMapping
  public CreateRestaurantResponse createRestaurant(
      @Valid @RequestBody CreateRestaurantRequest req) {
    var r = service.createRestaurant(req.getName(), req.getMenuItems());
    return CreateRestaurantResponse.builder().id(r.getId()).build();
  }

  @GetMapping(path = "/ticket/order/{orderId}")
  public ResponseEntity<TicketDto> findTicketByOrder(@PathVariable long orderId) {
    return ResponseEntity.ok(service.findTicketByOrderId(orderId));
  }

  @PostMapping("/accept")
  public void acceptOrder(@Valid @RequestBody AcceptOrderRequest acceptOrderRequest) {
    log.info("accept order: {}", acceptOrderRequest);
    service.acceptOrder(acceptOrderRequest.getOrderId());
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
                        .id(it.getId())
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

  @ExceptionHandler(exception = {OrderNotFoundException.class})
  public ResponseEntity<String> handle(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

}
