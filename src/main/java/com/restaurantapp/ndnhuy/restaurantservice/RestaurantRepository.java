package com.restaurantapp.ndnhuy.restaurantservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

  @Query("""
      SELECT item from MenuItem item WHERE item.id IN (:menuIds)
      """)
  List<MenuItem> findMenuItemsIn(List<Long> menuIds);
}
