package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.restaurantservice.RestaurantDTO.MenuItemDTO;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class RestaurantService {

  private RestaurantRepository restaurantRepository;

  private TransactionTemplate transactionTemplate;

  public RestaurantService(RestaurantRepository restaurantRepository, PlatformTransactionManager tnxManager) {
    this.restaurantRepository = restaurantRepository;
    this.transactionTemplate = new TransactionTemplate(tnxManager);
    this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
  }

  public Restaurant createRestaurant(String name, List<Restaurant.MenuItem> menuItems) {
    var r = restaurantRepository.save(Restaurant.builder().name(name).menuItems(menuItems).build());
    return r;
  }

  // public Optional<Restaurant> getRestaurantById2(long id) {
  //   Optional<Restaurant> r = transactionTemplate.execute(status -> {
  //     var restaurant = restaurantRepository.findById(id);
  //     if (!restaurant.isPresent()) {
  //       return Optional.empty();
  //     }
  //     return restaurant;
  //   });
  //   return r;
  // }

  public Optional<RestaurantDTO> getRestaurantById(long id) {
    var restaurant = restaurantRepository.findById(id);
    if (!restaurant.isPresent()) {
      return Optional.empty();
    }

    return restaurant
        .map(
            r -> RestaurantDTO.builder().id(r.getId()).name(r.getName())
                .menuItems(r.getMenuItems().stream()
                    .map(it -> MenuItemDTO
                        .builder()
                        .name(it.getName())
                        .price(it.getPrice())
                        .build())
                    .toList())
                .build());
  }
}
