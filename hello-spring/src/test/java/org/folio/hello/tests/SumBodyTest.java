package org.folio.hello.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import net.minidev.json.JSONObject;

import org.folio.hello.BaseIntegrationTest;
import org.folio.hello.model.SumResponse;
import org.junit.jupiter.api.Test;

public class SumBodyTest extends BaseIntegrationTest{

  @Test
  public void testSumBodyEndpoint() {
    JSONObject body = new JSONObject(); 
    body.put("a", 20);
    body.put("b", -12);
    
    Response response = ra()
      .header("Content-Type", "application/json")
      .body(body.toJSONString())
      .post(getRequestUrl("/sum/body"));
    response.then().statusCode(200);

    SumResponse sumObj = response.getBody().as(SumResponse.class);
    assertThat(sumObj.getSum(), is(equalTo(8)));
  }
}
