package com.restaurantapp.ndnhuy.deliveryservice;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

  private LocalDateTime readyBy;

  private Long assignedCourierId;

  public void schedule(LocalDateTime readyBy, long assignedCourierId) {
    this.readyBy = readyBy;
    this.assignedCourierId = assignedCourierId;
    this.status = DeliveryStatus.SCHEDULED;
  }
}
