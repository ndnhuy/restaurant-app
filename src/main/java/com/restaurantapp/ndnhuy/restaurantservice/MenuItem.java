package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ra_restaurant_menu_items")
@Access(AccessType.FIELD)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long restaurantId;

  @NotBlank
  private String name;

  @NotNull
  private BigDecimal price;
}
