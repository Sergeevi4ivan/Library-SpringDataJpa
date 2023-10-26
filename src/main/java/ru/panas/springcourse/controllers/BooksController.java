package ru.panas.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.panas.springcourse.models.Book;
import ru.panas.springcourse.models.Person;
import ru.panas.springcourse.services.BookService;
import ru.panas.springcourse.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookService.findAll());

        return "books/index";
    }

    @GetMapping(params = "sort")
    public String index(@RequestParam("sort") Optional<Boolean> sort, Model model) {
        if (sort.isPresent() && sort.get()) {
            model.addAttribute("books", bookService.findAllSort("yearProduction"));
        }
        return "books/index";
    }

    @GetMapping(params = {"page", "booksPerPage"})
    public String index(@RequestParam(value = "page") int page,
                        @RequestParam(value = "booksPerPage") int booksPerPage,
                        Model model) {

            model.addAttribute("books", bookService.findAll(PageRequest.of(page, booksPerPage)));

        return "books/index";
    }
    @GetMapping(params = {"page", "booksPerPage", "sort"})
    public String index(@RequestParam(value = "page") int page,
                        @RequestParam(value = "booksPerPage") int booksPerPage,
                        @RequestParam(value = "sort") Optional<Boolean> sort,
                        Model model) {

        if (sort.isPresent() && sort.get()) {
            model.addAttribute("books", bookService.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearProduction"))));
        }

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOne(id));

        Optional<Person> bookOwner = bookService.getBookOwner(id);

        if (bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "title", required = false) String title,
                         @ModelAttribute("book") Book book,
                         Model model) {
        model.addAttribute("books", bookService.findBooksByTitleContaining(title));

        return "books/search";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        // У selectedPerson назначено только поле id, остальные поля - null
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

}
