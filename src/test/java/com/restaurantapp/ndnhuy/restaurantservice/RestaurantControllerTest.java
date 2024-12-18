package com.restaurantapp.ndnhuy.restaurantservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.OrderHelper;
import com.restaurantapp.ndnhuy.common.RequestLineItem;
import com.restaurantapp.ndnhuy.common.RestaurantHelper;
import com.restaurantapp.ndnhuy.orderservice.OrderStatus;
import com.restaurantapp.ndnhuy.utils.RandomUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class})
@ActiveProfiles("test")
public class RestaurantControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RestaurantService restaurantService;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private RestaurantHelper restaurantHelper;

  @Autowired
  private OrderHelper orderHelper;

  @Test
  @SneakyThrows
  public void testCreateRestaurant() {
    var rid = restaurantHelper.getResourceId(
        restaurantHelper.createResource(
            restaurantHelper.givenValidCreationRequest(),
            rs -> rs.andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty()),
            restaurantHelper::getReponseAsJson
        )
    );

    var wantRestaurantName = restaurantHelper.givenValidCreationRequest().getName();
    var wantMenuItems = restaurantHelper.givenValidCreationRequest().getMenuItems();
    restaurantHelper.getResourceById(rid, rs ->
        rs.andExpect(status().isOk())
            .andExpect(jsonPath("id").value(rid))
            .andExpect(jsonPath("name").value(wantRestaurantName))
            .andExpect(jsonPath("menuItems").hasJsonPath())
            .andExpect(jsonPath("menuItems[0].name").value(wantMenuItems.getFirst().getName()))
            .andExpect(jsonPath("menuItems[0].price").value(wantMenuItems.getFirst().getPrice().setScale(1).toString()))
            .andExpect(jsonPath("menuItems[1].name").value(wantMenuItems.get(1).getName()))
            .andExpect(jsonPath("menuItems[1].price").value(wantMenuItems.get(1).getPrice().setScale(1).toString()))
    );

    assertRestaurantIsPersisted(rid, wantRestaurantName, wantMenuItems);
  }

  @Test
  @SneakyThrows
  public void testGetTicketByOrder() {
    var orderId = RandomUtils.randomPositiveLong();
    restaurantService.createTicket(CreateTicketRequest.builder()
        .restaurantId(RandomUtils.randomPositiveLong())
        .orderId(orderId)
        .customerId(RandomUtils.randomPositiveLong())
        .lineItems(List.of(RequestLineItem.builder()
            .menuItemId(RandomUtils.randomPositiveLong())
            .quantity(10)
            .build()))
        .build());
    restaurantHelper.findTicketByOrder(orderId)
        .andExpect(status().isOk())
        .andExpect(jsonPath("status").value(TicketStatus.CREATED.toString()));
  }

  @Test
  @SneakyThrows
  public void testAcceptOrder() {
    // given a restaurant
    var rid = restaurantHelper.getResourceId(
        restaurantHelper.createResource(
            restaurantHelper.givenValidCreationRequest(),
            rs -> rs.andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty()),
            restaurantHelper::getReponseAsJson
        )
    );

    // given an order
    var orderId = orderHelper.givenValidOrderId();

    // when restaurant accepts the order
    restaurantHelper.acceptOrder(orderId)
        .andExpect(status().isOk());

    // then the ticket is created with correct menu items and amount

    // order should be accepted
    orderHelper.getResourceById(
        orderId,
        rs -> rs.andExpect(status().isOk())
            .andExpect(jsonPath("status").value(OrderStatus.ACCEPTED.toString()))
    );
  }

  @Transactional
  public void assertRestaurantIsPersisted(long id, String restaurantName, List<MenuItem> menuItems) {
    var obj = restaurantRepository.findById(id);
    assertThat(obj.isPresent()).isTrue();
    var r = obj.get();
    assertThat(r.getId()).isEqualTo(id);
    assertThat(r.getName()).isEqualTo(restaurantName);
    assertThat(r.getMenuItems()).hasSize(2);
    var actualMenuItem1 = r.getMenuItems().get(0);
    var expectMenuItem1 = menuItems.get(0);
    var actualMenuItem2 = r.getMenuItems().get(1);
    var expectMenuItem2 = r.getMenuItems().get(1);
    assertThat(actualMenuItem1).isNotNull();
    assertThat(actualMenuItem1.getName()).isEqualTo(expectMenuItem1.getName());
    assertThat(actualMenuItem2.getPrice())
        .isEqualTo(expectMenuItem2.getPrice());
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
