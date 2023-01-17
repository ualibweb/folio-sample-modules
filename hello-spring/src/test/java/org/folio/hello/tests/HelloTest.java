package org.folio.hello.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import org.folio.hello.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

class HelloTest extends BaseIntegrationTest {

  @Test
  void testSumQueryEndpoint() {
    // Make a GET request to /hello
    Response response = ra().get(getRequestUrl("/hello"));

    // check for a successful HTTP response
    response.then().statusCode(200);

    // get the response as a String and check it
    String responseStr = response.getBody().asString();
    assertThat(responseStr, is("Hello, world!\n"));
  }
}
