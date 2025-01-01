package com.restaurantapp.ndnhuy.restaurantservice;

import com.restaurantapp.ndnhuy.common.events.TicketAcceptedEvent;
import com.restaurantapp.ndnhuy.orderservice.Order;
import com.restaurantapp.ndnhuy.orderservice.OrderNotFoundException;
import com.restaurantapp.ndnhuy.orderservice.OrderService;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantDTO.MenuItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;

  private final OrderService orderService;

  private final TicketRepository ticketRepository;

  private final ApplicationEventPublisher eventPublisher;

  public Restaurant createRestaurant(String name, List<MenuItem> menuItems) {
    return restaurantRepository.save(Restaurant.builder().name(name).menuItems(menuItems).build());
  }

  @Transactional
  public void acceptOrder(Long orderId) {
    var ticket = ticketRepository.findByOrderId(orderId)
        .map(Ticket::accepted)
        .orElseThrow(() -> new TicketNotFoundException(orderId));

    ticketRepository.save(ticket);

    log.info("ticket accepted: {}", ticket.getId());

    eventPublisher.publishEvent(TicketAcceptedEvent.builder()
        .ticketId(ticket.getId())
        .orderId(ticket.getOrderId())
        .build());
  }

  public List<MenuItem> findMenuItems(List<Long> ids) {
    return restaurantRepository.findMenuItemsIn(ids);
  }

  public Ticket createTicket(CreateTicketRequest request) {
    return ticketRepository.save(Ticket.builder()
        .restaurantId(request.getRestaurantId())
        .customerId(request.getCustomerId())
        .orderId(request.getOrderId())
        .status(TicketStatus.CREATED)
        .lineItems(request.getLineItems().stream().map(item -> TicketLineItem.builder()
                .menuItemId(item.getMenuItemId())
                .quantity(item.getQuantity())
                .build())
            .toList())
        .build());
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

  public TicketDto findTicketByOrderId(Long orderId) {
    return ticketRepository.findByOrderId(orderId)
        .map(ticket -> TicketDto.builder()
            .id(ticket.getId())
            .customerId(ticket.getCustomerId())
            .orderId(ticket.getOrderId())
            .restaurantId(ticket.getRestaurantId())
            .status(ticket.getStatus())
            .customerId(ticket.getCustomerId())
            .lineItems(ticket.getLineItems())
            .build())
        .orElseThrow(() -> new TicketNotFoundException(orderId));
  }
}
