package org.folio.hello.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.folio.hello.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

class ExampleSumTest extends BaseIntegrationTest {

  @Test
  void testIgnore() {
    // linting rules complain if there aren't any tests; replace this with
    // actual test code for the sum endpoint
    assertThat(1 + 1, is(2));
  }
  // Sample test for a query parameter sum endpoint
  /*
  @Test
  void testSumQueryEndpoint() {
    // restassured is very flexible:
    //   https://www.javadoc.io/doc/io.rest-assured/rest-assured/3.1.1/io/restassured/specification/RequestSpecification.html
    // other methods, such as .body(...), can be used to provide POST bodies

    Response response = ra()
      .queryParam("a", 12)
      .queryParam("b", -3)
      // getRequestUrl is a helper method that adds http://localhost:1234 to the URL
      // where 1234 is a randomly generated port for testing
      .post(getRequestUrl("/sum/query"));

    // check for a successful HTTP response
    response.then().statusCode(200);

    // cast the response to a SumResponse (depending on how you name it), and check it
    SumResponse sumObj = response.getBody().as(SumResponse.class);
    assertThat(sumObj.getSum(), is(equalTo(9)));
  }
  */
}
