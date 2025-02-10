package com.restaurantapp.ndnhuy.deliveryservice;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long customerId;

  private Long orderId;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus status;

  private Long restaurantId;

}
