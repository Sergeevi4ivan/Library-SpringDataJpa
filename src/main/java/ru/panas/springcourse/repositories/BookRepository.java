package ru.panas.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.panas.springcourse.models.Book;
import ru.panas.springcourse.models.Person;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    List<Book> findBooksByTitleContaining(String request);

}
