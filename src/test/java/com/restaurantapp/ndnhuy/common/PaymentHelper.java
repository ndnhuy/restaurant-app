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

import static com.restaurantapp.ndnhuy.common.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PaymentHelper implements EntityTestSupport<CreatePaymentOrderRequest, Long> {

  @Autowired
  private final MockMvc mockMvc;

  private TestHelper testHelper = TestHelper.builder().build();

  @SneakyThrows
  private ResultActions doCreatePaymentOrder(CreatePaymentOrderRequest request) {
    return this.mockMvc.perform(
            post("/payments/createAndPay")
                .contentType("application/json")
                .content(asJsonString(request))
        )
        .andDo(print());
  }

  @Override
  public ResultActions doCreateResource(CreatePaymentOrderRequest request) {
    return doCreatePaymentOrder(request);
  }

  @Override
  public JSONObject getResourceById(Long resourceId, ResultAssert resultAssert) {
    return null;
  }

  @Override
  public CreatePaymentOrderRequest givenValidCreationRequest() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Long getResourceId(JSONObject jsonObject) {
    throw new UnsupportedOperationException();
  }
}
