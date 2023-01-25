package org.folio.hello.domain.mapper;

import org.folio.hello.domain.entity.Book;
import org.folio.hello.model.BookWithId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
  BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

  BookWithId bookToBookWithId(Book book);
  Book bookWithIdToBook(BookWithId bookWithId);
}
