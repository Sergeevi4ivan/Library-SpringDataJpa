package ru.panas.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.panas.springcourse.models.Book;
import ru.panas.springcourse.models.Person;
import ru.panas.springcourse.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    public List<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    public List<Book> findAllSort(String sortColumn) {
        return bookRepository.findAll(Sort.by(sortColumn));
    }

    public List<Book> findBooksByTitleContaining(String title) {
        return bookRepository.findBooksByTitleContaining(title);
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

    @Transactional
    public Optional<Person> getBookOwner(int id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.get().getAssignAt() != null) {
            long timeOfAssign = new Date().getTime() - book.get().getAssignAt().getTime();

            if (timeOfAssign > TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS)) {
                book.get().setLate(true);
            }
        }
        return book.map(value -> Optional.ofNullable(value.getOwner())).orElse(null);
    }

    @Transactional
    public void release(int id) {
        Optional<Book> book = bookRepository.findById(id);

        book.ifPresent(value -> value.setAssignAt(null));
        book.ifPresent(value -> value.setOwner(null));
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Optional<Book> book = bookRepository.findById(id);

        book.ifPresent(value -> value.setAssignAt(new Date()));
        book.ifPresent(value -> value.setOwner(selectedPerson));
    }

    public List<Book> findByOwner(Person owner) {
        return bookRepository.findByOwner(owner);
    }

}
