package ru.panas.springcourse.dao;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.panas.springcourse.models.Person;

import java.util.HashSet;
import java.util.Set;

// Класс создан для тренировки решения проблемы N + 1
@Component
public class PersonDAO {

    private final EntityManager entityManager;

    @Autowired
    public PersonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public void testNPlus1() {
        Session session = entityManager.unwrap(Session.class);

        Set<Person> people = new HashSet<Person>(session.createQuery("SELECT p FROM Person  p LEFT JOIN FETCH p.books", Person.class)
                .getResultList());

        for (Person person: people) {
            System.out.println("Person " + person.getName() + " has: " +  person.getBooks());
        }
    }
}
