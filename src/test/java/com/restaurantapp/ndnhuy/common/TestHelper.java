package com.restaurantapp.ndnhuy.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

@Builder(toBuilder = true)
public class TestHelper {

  private ResultActions resultActions;

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
