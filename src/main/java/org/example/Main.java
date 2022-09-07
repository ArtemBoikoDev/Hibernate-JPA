package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.example.entity.Person;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(
                "MySQL-persistence-unit");
        EntityManager em = factory.createEntityManager();


        em.getTransaction().begin();


        Person person = getNewPerson();
//        em.persist(person);
        person = em.find(Person.class, 1L);
        System.out.println("person = " + person);

        person = em.find(Person.class, 1L);
        System.out.println("person = " + person);

        em.getTransaction().commit();


        person.setFirstName("NewFirstName");
        System.out.println("person = " + person);


        em.close();
    }

    public static Person getNewPerson() {
        Person person = new Person();
        person.setFirstName("FirstName");
        person.setLastName("LastName");
        return person;
    }
}
