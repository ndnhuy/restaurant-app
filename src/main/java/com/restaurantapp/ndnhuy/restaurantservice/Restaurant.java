package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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
  @JoinColumn(name = "restaurant_id")
  @NotEmpty(message = "menu items must be provided")
  private List<MenuItem> menuItems;

  @Entity
  @Table(name = "ra_restaurant_menu_items")
  @Embeddable
  @Access(AccessType.FIELD)
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long restaurant_id;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal price;
  }
}
