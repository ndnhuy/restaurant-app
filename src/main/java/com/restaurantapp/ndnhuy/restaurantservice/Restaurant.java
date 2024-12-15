package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ra_restaurant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "name must not be empty")
  private String name;

  //   @Embedded
//   @ElementCollection(fetch = FetchType.EAGER)
//   @CollectionTable(
//     name = "ra_restaurant_menu_items",
//     // if not specify this, default joinColumn would be concatenation of entity name and "_" and primary column
//     joinColumns = { @JoinColumn(name = "restaurant_id") }
//   )
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "restaurantId")
  @NotEmpty(message = "menu items must be provided")
  private List<MenuItem> menuItems;

}
