package ru.panas.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.panas.springcourse.models.Book;
import ru.panas.springcourse.models.Person;
import ru.panas.springcourse.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setBook_id(id);
        bookRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(value -> Optional.ofNullable(value.getOwner())).orElse(null);
    }

    @Transactional
    public void release(int id) {
        Optional<Book> book = bookRepository.findById(id);

        book.ifPresent(value -> value.setOwner(null));
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Optional<Book> book = bookRepository.findById(id);
        book.ifPresent(value -> value.setOwner(selectedPerson));
    }

    public List<Book> findByOwner(Person owner) {
        return bookRepository.findByOwner(owner);
    }

}
