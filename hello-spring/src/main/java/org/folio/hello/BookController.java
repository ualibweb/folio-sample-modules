package org.folio.hello;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.folio.hello.api.BookApi;
import org.folio.hello.domain.entity.Book;
import org.folio.hello.model.BookInput;
import org.folio.hello.model.BookObject;
import org.folio.hello.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class BookController implements BookApi {

  @Autowired
  private BookRepository bookRepository;

  /**
   * Method to create response object
   *
   * @param response The response object to modify
   * @param book The book object that holds the info
   */
  static void createResponse(BookObject response, Book book) {
    response.setId(book.getId().toString());
    response.setName(book.getName());
    response.setPageCount(book.getPageCount());
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<BookObject>> bookGet() {
    List<Book> allBooks = bookRepository.findAll();
    List<BookObject> response = new ArrayList<>();

    // Add books to the response object
    for (Book book : allBooks) {
      BookObject bookInfo = new BookObject();
      createResponse(bookInfo, book);
      response.add(bookInfo);
    }

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<BookObject> bookPost(BookInput newBook) {
    Book book = Book
      .builder()
      .name(newBook.getName())
      .pageCount(newBook.getPageCount())
      .build();

    // Save the new book to the db
    bookRepository.save(book);

    // Create the response to response
    BookObject response = new BookObject();
    createResponse(response, book);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Void> bookIdDelete(String id) {
    // Delete the book by id
    if (bookRepository.existsById(UUID.fromString(id))) {
      bookRepository.deleteById(UUID.fromString(id));
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<BookObject> bookIdPut(
    String id,
    BookInput updatedBook
  ) {
    Optional<Book> bookOptional = bookRepository.findById(UUID.fromString(id));

    BookObject response = new BookObject();

    // Check if the book with the the given UUID exists
    if (bookOptional.isPresent()) {
      Book bookToUpdate = bookOptional.get();
      bookToUpdate.setName(updatedBook.getName());
      bookToUpdate.setPageCount(updatedBook.getPageCount());
      bookRepository.save(bookToUpdate);

      createResponse(response, bookToUpdate);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
