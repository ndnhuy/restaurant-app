package com.restaurantapp.ndnhuy.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;

@Builder(toBuilder = true)
public class TestHelper {

  private ResultActions resultActions;

  public TestHelper andExpect(ResultMatcher resultMatcher) {
    try {
      this.resultActions.andExpect(resultMatcher);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  public JSONObject thenGetResponseAsJson() {
    try {
      return new JSONObject(this.resultActions.andReturn().getResponse().getContentAsString());
    } catch (JSONException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
