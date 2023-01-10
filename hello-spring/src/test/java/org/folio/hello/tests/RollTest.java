package org.folio.hello.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.everyItem;


import java.util.Random;
import java.util.Arrays;

import io.restassured.response.Response;

import org.folio.hello.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

public class RollTest extends BaseIntegrationTest {
  
  @Test
  public void testRollGet() {
    Response response = ra()
      .get(getRequestUrl("/roll"));
    response.then().statusCode(200);

    Integer rollResult = Integer.parseInt(response.getBody().asString());
    assertThat(rollResult, is(both(greaterThan(0)).and(lessThan(7))));
  }

  @Test
  public void testRollPost() {
    Random random = new Random();
    Integer numRoll = random.nextInt(10) + 1;

    Response response = ra()
      .header("Content-Type", "application/json")
      .body(numRoll.toString())
      .post(getRequestUrl("/roll"));
    response.then().statusCode(200);

    Integer[] rollResultsArray = response.getBody().as(Integer[].class);
    Iterable<Integer> rollResults = Arrays.asList(rollResultsArray);
    assertThat(rollResults, everyItem(is(both(greaterThan(0)).and(lessThan(7)))));
  }
}
