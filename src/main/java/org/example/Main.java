package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.example.entity.Person;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(
                "MySQL-persistence-unit");


        Person person = createNewPerson(factory);
        findInSession(factory);
//        deleteDetached(person, factory); // exception
        deleteAttached(person, factory); // ok


        factory.close();
    }

    /**
     * deletes reAttached
     */
    private static void deleteAttached(Person person, EntityManagerFactory factory) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();


        Person attachedToSessionPerson = em.merge(person);
        em.remove(attachedToSessionPerson);


        em.getTransaction().commit();
        em.close();
    }

    /**
     * doesn't delete detached ("Removing a detached instance")
     */
    private static void deleteDetached(Person person, EntityManagerFactory factory) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();


        em.remove(person);


        em.getTransaction().commit();
        em.close();
    }

    /**
     * updates
     */
    private static void findInSession(EntityManagerFactory factory) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();


        Person person = em.find(Person.class, 1L);
        System.out.println("person = " + person);

        person.setFirstName("NewFirstName");

        person = em.find(Person.class, 1L);
        System.out.println("person = " + person);


        em.getTransaction().commit();
        em.close();
    }

    public static Person createNewPerson(EntityManagerFactory factory) {
        Person person = new Person();
        person.setFirstName("FirstName");
        person.setLastName("LastName");


        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();


        em.persist(person);
        person = em.find(Person.class, 1L);
        System.out.println("person = " + person);


        em.getTransaction().commit();
        em.close();
        return person;
    }
}
