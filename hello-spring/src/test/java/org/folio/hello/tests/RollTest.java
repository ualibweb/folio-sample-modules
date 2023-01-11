package org.folio.hello.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.everyItem;

import java.util.Arrays;

import io.restassured.response.Response;

import org.folio.hello.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class RollTest extends BaseIntegrationTest {
  
  @Test
  void testRollGet() {
    Response response = ra()
      .get(getRequestUrl("/roll"));
    response.then().statusCode(200);

    Integer rollResult = Integer.parseInt(response.getBody().asString());
    assertThat(rollResult, is(both(greaterThan(0)).and(lessThan(7))));
  }

  @ParameterizedTest
  @ValueSource(ints = {5, 1000, 0})
  void testRollPost(Integer numRoll) {
    Response response = ra()
      .header("Content-Type", "application/json")
      .body(numRoll.toString())
      .post(getRequestUrl("/roll"));
    response.then().statusCode(200);

    Integer[] rollResultsArray = response.getBody().as(Integer[].class);
    Iterable<Integer> rollResults = Arrays.asList(rollResultsArray);
    assertThat(rollResults, everyItem(is(both(greaterThan(0)).and(lessThan(7)))));
  }

  @Test
  void testRollPostNegativeInput() {
    Integer numRoll = -1;

    Response response = ra()
      .header("Content-Type", "application/json")
      .body(numRoll.toString())
      .post(getRequestUrl("/roll"));
    response.then().statusCode(400);
  }
}
