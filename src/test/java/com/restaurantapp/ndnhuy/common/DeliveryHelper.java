package com.restaurantapp.ndnhuy.common;

import com.restaurantapp.ndnhuy.deliveryservice.CreateDeliveryRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DeliveryHelper implements EntityTestSupport<CreateDeliveryRequest, Long> {

  @Autowired
  private final MockMvc mockMvc;

  @Override
  @SneakyThrows
  public ResultActions doCreateResource(CreateDeliveryRequest request) {
    return this.mockMvc.perform(
            post("/delivery")
                .contentType("application/json")
                .content(asJsonString(request))
        )
        .andDo(print());
  }

  @Override
  @SneakyThrows
  public JSONObject getResourceById(Long resourceId, ResultAssert resultAssert) {
    var r = this.mockMvc
        .perform(get("/delivery/" + resourceId))
        .andDo(print());
    resultAssert.accept(r);
    return getReponseAsJson(r);
  }

  @Override
  public CreateDeliveryRequest givenValidCreationRequest() {
    throw new UnsupportedOperationException();
  }

  @Override
  @SneakyThrows
  public Long getResourceId(JSONObject jsonObject) {
    return jsonObject.getLong("deliveryId");
  }
}
