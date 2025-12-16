package com.staniskhan.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.staniskhan.library.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByPublicationYear(int publicationYear);

    List<Book> findByCategoryContainingIgnoreCase(String category);
}