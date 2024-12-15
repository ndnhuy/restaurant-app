package com.restaurantapp.ndnhuy.restaurantservice;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long customerId;

  private Long orderId;

  private Long restaurantId;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ticket_line_items", joinColumns = {@JoinColumn(name = "ticketId")})
  private List<TicketLineItem> lineItems;

}
