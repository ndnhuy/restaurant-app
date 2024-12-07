package com.restaurantapp.ndnhuy.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CustomerHelper {

  @Autowired
  private final MockMvc mockMvc;

  private ResultActions resultActions;

  private CustomerHelper cloneInstance() {
    var obj = new CustomerHelper(mockMvc);
    obj.resultActions = this.resultActions;
    return obj;
  }

  private CustomerHelper resultActions(ResultActions resultActions) {
    var obj = cloneInstance();
    obj.resultActions = resultActions;
    return obj;
  }

  @SneakyThrows
  public CustomerHelper createCustomer(CreateCustomerRequest request) {
    return cloneInstance().resultActions(doCreateCustomer(request));
  }

  public CustomerHelper validCustomer() {
    return createCustomer(CreateCustomerRequest
        .builder()
        .firstName("John")
        .lastName("Wick")
        .build());
  }

  @SneakyThrows
  private ResultActions doCreateCustomer(CreateCustomerRequest request) {
    return mockMvc.perform(
            post("/customers")
                .contentType("application/json")
                .content(
                    asJsonString(request)))
        .andDo(print());
  }

  public CustomerHelper andExpect(ResultMatcher resultMatcher) {
    var obj = cloneInstance();
    try {
      obj.resultActions.andExpect(resultMatcher);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  public JSONObject thenGetResponseAsJson() {
    try {
      return new JSONObject(this.resultActions.andReturn().getResponse().getContentAsString());
    } catch (JSONException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @SneakyThrows
  public Long thenGetCustomerId() {
    return this.thenGetResponseAsJson().getLong("customerId");
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
