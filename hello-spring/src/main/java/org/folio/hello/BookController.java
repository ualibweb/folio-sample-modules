package org.folio.hello;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.folio.hello.api.BookApi;
import org.folio.hello.domain.entity.Book;
import org.folio.hello.domain.mapper.BookMapper;
import org.folio.hello.model.BookInput;
import org.folio.hello.model.BookWithId;
import org.folio.hello.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public final class BookController implements BookApi {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BookMapper bookMapper;

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<List<BookWithId>> bookGet() {
    List<Book> allBooks = bookRepository.findAll();
    List<BookWithId> response = new ArrayList<>();

    // Add books to the response object
    for (Book book : allBooks) {
      BookWithId bookWithId = bookMapper.bookToBookWithId(book);
      response.add(bookWithId);
    }

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<BookWithId> bookPost(BookInput newBook) {
    log.info(newBook.toString());
    Book book = Book
      .builder()
      .name(newBook.getName())
      .pageCount(newBook.getPageCount())
      .build();

    // Save the new book to the db
    bookRepository.save(book);

    // Create the response to response
    BookWithId response = bookMapper.bookToBookWithId(book);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Void> bookIdDelete(UUID id) {
    // Delete the book by id
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);

      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<BookWithId> bookIdPut(UUID id, BookInput updatedBook) {
    Optional<Book> bookOptional = bookRepository.findById(id);

    // Check if the book with the the given UUID exists
    if (bookOptional.isPresent()) {
      Book bookToUpdate = bookOptional.get();
      bookToUpdate.setName(updatedBook.getName());
      bookToUpdate.setPageCount(updatedBook.getPageCount());
      bookRepository.save(bookToUpdate);

      BookWithId response = bookMapper.bookToBookWithId(bookToUpdate);

      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
