package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.orderservice.Order;
import com.restaurantapp.ndnhuy.orderservice.OrderNotFoundException;
import com.restaurantapp.ndnhuy.orderservice.OrderService;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantDTO.MenuItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;

  private final TransactionTemplate transactionTemplate;

  private final OrderService orderService;

  public Restaurant createRestaurant(String name, List<MenuItem> menuItems) {
    return restaurantRepository.save(Restaurant.builder().name(name).menuItems(menuItems).build());
  }

  public void acceptOrder(Long orderId) {
    orderService.findOrder(orderId)
        .map(Order::accepted)
        .ifPresentOrElse(orderService::save, () -> {
          throw new OrderNotFoundException(orderId);
        });
  }

  public Optional<RestaurantDTO> getRestaurantById(long id) {
    return restaurantRepository.findById(id)
        .map(
            r -> RestaurantDTO.builder().id(r.getId()).name(r.getName())
                .menuItems(r.getMenuItems().stream()
                    .map(it -> MenuItemDTO
                        .builder()
                        .id(it.getId())
                        .name(it.getName())
                        .price(it.getPrice())
                        .build())
                    .toList())
                .build());
  }
}
