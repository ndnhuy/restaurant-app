package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.paymentservice.CreatePaymentOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.restaurantapp.ndnhuy.common.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PaymentHelper {

  @Autowired
  private final MockMvc mockMvc;

  private TestHelper testHelper = TestHelper.builder().build();

  private PaymentHelper resultActions(ResultActions resultActions) {
    testHelper = testHelper.toBuilder().resultActions(resultActions).build();
    return this;
  }

  public PaymentHelper createPaymentOrder(CreatePaymentOrderRequest request) {
    return this.resultActions(doCreatePaymentOrder(request));
  }

  @SneakyThrows
  private ResultActions doCreatePaymentOrder(CreatePaymentOrderRequest request) {
    return this.mockMvc.perform(
            post("/payment")
                .contentType("application/json")
                .content(asJsonString(request))
        )
        .andDo(print());
  }

  public PaymentHelper andExpect(ResultMatcher resultMatcher) {
    testHelper.andExpect(resultMatcher);
    return this;
  }

  public JSONObject thenGetResponseAsJson() {
    return testHelper.thenGetResponseAsJson();
  }

}
