package com.restaurantapp.ndnhuy.restaurantservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.RestaurantHelper;
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

  @Test
  @SneakyThrows
  public void testGrafana() {
    this.mockMvc.perform(
        post("/orders/create/INIT?waitTimeInMs=0&processTimeInMs=100")
            .contentType("application/json"));
  }

  @Test
  @SneakyThrows
  public void testCreateRestaurant() {
    var restaurantName = "Pizza Hut";
    var menuItems = restaurantHelper.givenMenuItems();
    var rid = restaurantHelper.validRestaurant(restaurantName, menuItems)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").isNotEmpty())
        .thenGetRestaurantId();

    restaurantHelper.getById(rid)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(rid))
        .andExpect(jsonPath("name").value(restaurantName))
        .andExpect(jsonPath("menuItems").hasJsonPath())
        .andExpect(jsonPath("menuItems[0].name").value(menuItems.getFirst().getName()))
        .andExpect(jsonPath("menuItems[0].price").value(menuItems.getFirst().getPrice().setScale(1).toString()))
        .andExpect(jsonPath("menuItems[1].name").value(menuItems.get(1).getName()))
        .andExpect(jsonPath("menuItems[1].price").value(menuItems.get(1).getPrice().setScale(1).toString()));

    assertRestaurantIsPersisted(rid, restaurantName, menuItems);
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
