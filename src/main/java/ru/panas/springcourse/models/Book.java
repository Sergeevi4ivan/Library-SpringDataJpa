package ru.panas.springcourse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_id;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название книги должно быть от 2 до 100 символов")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Автор не должен быть пустым")
    @Size(min = 2, max = 100, message = "Имя автора должно быть от 2 до 100 символов")
    @Column(name = "author")
    private String author;

    @Min(value = 1500, message = "Год должен быть больше, чем 1500")
    @Column(name = "year_production")
    private int yearProduction;

    @Column(name = "assign_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignAt;

    @Transient
    private Boolean isLate;

    public Book() {

    }

    public Book(String title, String author, int yearProduction) {
        this.title = title;
        this.author = author;
        this.yearProduction = yearProduction;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearProduction() {
        return yearProduction;
    }

    public void setYearProduction(int year_production) {
        this.yearProduction = year_production;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Boolean getLate() {
        return isLate;
    }

    public void setLate(Boolean late) {
        isLate = late;
    }

    public Date getAssignAt() {
        return assignAt;
    }

    public void setAssignAt(Date assignAt) {
        this.assignAt = assignAt;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearProduction=" + yearProduction +
                '}';
    }
}
