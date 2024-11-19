package com.restaurantapp.ndnhuy.restaurantservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.example.ServiceLayer;
import com.restaurantapp.ndnhuy.restaurantservice.Restaurant.MenuItem;

import java.math.BigDecimal;
import java.util.List;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @SneakyThrows
    public void testGrafana() {
        this.mockMvc.perform(
                post("/orders/create/INIT?waitTimeInMs=0&processTimeInMs=2000")
                        .contentType("application/json"));
    }

    @Test
    @SneakyThrows
    public void testCreateRestaurant() {
        var restaurantName = "Pizza Hut";
        var menuItems = List.of(
                Restaurant.MenuItem
                        .builder()
                        .name("pizza type 1")
                        .price(new BigDecimal(5))
                        .build(),
                Restaurant.MenuItem
                        .builder()
                        .name("pizza type 2")
                        .price(new BigDecimal(10))
                        .build());
        this.mockMvc.perform(
                        post("/restaurants")
                                .contentType("application/json")
                                .content(
                                        asJsonString(
                                                CreateRestaurantRequest
                                                        .builder()
                                                        .name(restaurantName)
                                                        .menuItems(menuItems)
                                                        .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("restaurantId").value(1L));

        assertRestaurantIsPersisted(1L, restaurantName, menuItems);
    }

    @Transactional
    public void assertRestaurantIsPersisted(long id, String restaurantName, List<MenuItem> menuItems) {
        var obj = restaurantRepository.findById(id);
        assertThat(obj.isPresent()).isTrue();
        var r = obj.get();
        assertThat(r.getId()).isEqualTo(1);
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
