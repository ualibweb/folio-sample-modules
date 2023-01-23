package org.folio.hello.domain.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@Table(name = "book")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

  /*
   * The UUID of the book
   */
  @Id
  @GeneratedValue
  @NotNull
  @Column(name = "id")
  private UUID id;

  /*
   * Name of the book
   */
  @NotNull
  @Column(name = "name")
  private String name;

  /*
   * The page count of the book
   */
  @Column(name = "page_count")
  private int pageCount;
}
