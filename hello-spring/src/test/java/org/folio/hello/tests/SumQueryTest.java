package org.folio.hello.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import org.folio.hello.BaseIntegrationTest;
import org.folio.hello.model.SumResponse;
import org.junit.jupiter.api.Test;

public class SumQueryTest extends BaseIntegrationTest {

  @Test
  public void testSumQueryEndpoint() {
    Response response = ra()
      .queryParam("a", 12)
      .queryParam("b", -3)
      .get(getRequestUrl("/sum/query"));
    response.then().statusCode(200);

    SumResponse sumObj = response.getBody().as(SumResponse.class);
    assertThat(sumObj.getSum(), is(equalTo(9)));
  }
}
