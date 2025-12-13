package com.library.managementservice.repository;

import com.library.managementservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") Long id);


    @Query("""
        select b from Book b
        where (:title is null or lower(b.title) like lower(concat('%', :title, '%')))
          and (:author is null or lower(b.author) like lower(concat('%', :author, '%')))
    """)
    List<Book> search(
            @Param("title") String title,
            @Param("author") String author
    );
}
