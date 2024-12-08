package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.restaurantapp.ndnhuy.common.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CustomerHelper implements EntityTestSupport<CreateCustomerRequest, Long> {

  @Autowired
  private final MockMvc mockMvc;

  @Override
  @SneakyThrows
  public ResultActions doCreateResource(CreateCustomerRequest request) {
    return mockMvc.perform(
            post("/customers")
                .contentType("application/json")
                .content(
                    asJsonString(request)))
        .andDo(print());
  }

  @Override
  public JSONObject getResourceById(Long resourceId, ResultAssert resultAssert) {
    return null;
  }

  @Override
  public CreateCustomerRequest givenValidCreationRequest() {
    return CreateCustomerRequest
        .builder()
        .firstName("John")
        .lastName("Wick")
        .build();
  }

  @Override
  @SneakyThrows
  public Long getResourceId(JSONObject jsonObject) {
    return jsonObject.getLong("customerId");
  }

  public Long givenValidCustomerId() {
    return getResourceId(givenValidResource());
  }

}
