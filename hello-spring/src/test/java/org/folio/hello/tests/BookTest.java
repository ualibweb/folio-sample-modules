package org.folio.hello.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.folio.hello.BaseIntegrationTest;
import org.folio.hello.model.BookWithId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Log4j2
public class BookTest extends BaseIntegrationTest {

  private String[] names = { "test1", "test2", "test3", "test4", "test5" };
  private Integer[] pageCounts = { 123, 284, 352, 989, 31 };

  private List<UUID> ids;

  @BeforeEach
  void addBooksToDatabase() {
    ids = new ArrayList<>();
    for (int i = 0; i < names.length; ++i) {
      // Make a post request to add books to db
      Response response = ra()
        .header("Content-Type", "application/json")
        .body(
          String.format(
            "{\"name\": \"%s\", \"page_count\": %d}",
            names[i],
            pageCounts[i]
          )
        )
        .post(getRequestUrl("/book"));
      ids.add(response.getBody().as(BookWithId.class).getId());
    }
  }

  @AfterEach
  void deleteBooksFromDatabase() {
    for (UUID id : ids) {
      ra().pathParam("id", id.toString()).delete(getRequestUrl("/book/{id}"));
    }
    ids.clear();
  }

  @Test
  void testBookGet() {
    Response response = ra().get(getRequestUrl("/book"));
    response.then().statusCode(200);

    BookWithId[] bookArray = response.getBody().as(BookWithId[].class);
    Iterable<BookWithId> bookList = Arrays.asList(bookArray);

    assertThat(bookList, everyItem(is(instanceOf(BookWithId.class))));
    for (int i = 0; i < names.length; ++i) {
      log.info(bookArray[i].getName());
      log.info(names[i]);
    }

    for (int i = 0; i < names.length; ++i) {
      assertThat(bookArray[i].getName(), is(equalTo(names[i])));
      assertThat(bookArray[i].getPageCount(), is(equalTo(pageCounts[i])));
    }
  }

  @Test
  void testBookPost() {
    Response response = ra()
      .header("Content-Type", "application/json")
      .body("{\"name\": \"testPost\", \"page_count\": 190}")
      .post(getRequestUrl("/book"));
    response.then().statusCode(201);

    BookWithId postedBook = response.getBody().as(BookWithId.class);
    ids.add(postedBook.getId());

    assertThat(postedBook.getName(), is(equalTo("testPost")));
    assertThat(postedBook.getPageCount(), is(equalTo(190)));
  }

  @Test
  void testBookIdDelete() {
    Response response = ra()
      .pathParam("id", ids.get(0).toString())
      .delete(getRequestUrl("/book/{id}"));
    response.then().statusCode(204);
  }

  @Test
  void testBookIdPut() {
    Response response = ra()
      .header("Content-Type", "application/json")
      .pathParam("id", ids.get(2).toString())
      .body("{\"name\": \"test3_2\", \"page_count\": 312}")
      .put(getRequestUrl("/book/{id}"));
    response.then().statusCode(200);

    BookWithId updatedBook = response.getBody().as(BookWithId.class);
    assertThat(updatedBook.getId(), is(equalTo(ids.get(2))));
    assertThat(updatedBook.getName(), is(equalTo("test3_2")));
    assertThat(updatedBook.getPageCount(), is(equalTo(312)));
  }
}
