package com.restaurantapp.ndnhuy.common;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.function.Function;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface EntityTestSupport<CreationRequest, ID> {

  ResultActions doCreateResource(CreationRequest request);

  JSONObject getResourceById(ID resourceId, ResultAssert resultAssert);

  CreationRequest givenValidCreationRequest();

  ID getResourceId(JSONObject jsonObject);

  default JSONObject givenValidResource() {
    return createResource(givenValidCreationRequest(), rs -> rs.andExpect(status().isOk()), result -> {
      try {
        return new JSONObject(result.andReturn().getResponse().getContentAsString());
      } catch (JSONException | UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    });
  }

  default <T> T createResource(CreationRequest request, ResultAssert assertion, Function<ResultActions, T> mapper) {
    try {
      var result = doCreateResource(request);
      assertion.accept(result);
      return mapper.apply(result);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  default JSONObject getReponseAsJson(ResultActions resultActions) {
    try {
      return new JSONObject(resultActions.andReturn().getResponse().getContentAsString());
    } catch (JSONException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  interface ResultAssert {
    void accept(ResultActions t) throws Exception;
  }

  class NoopResultAssert implements ResultAssert {

    @Override
    public void accept(ResultActions t) throws Exception {
    }
  }

}
