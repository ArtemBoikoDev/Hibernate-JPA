package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Person;

@Slf4j
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("MySQL-persistence-unit");

        Person person = createNewPerson(factory);
        findInSession(factory);
        deleteDetached(person, factory); // exception
        deleteAttached(person, factory); // ok


        factory.close();
    }

    public static Person createNewPerson(EntityManagerFactory factory) {
        Person person = new Person();
        person.setFirstName("FirstName");
        person.setLastName("LastName");


        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();


            em.persist(person);
            person = em.find(Person.class, 1L);
            log.info("person = {}", person);


            em.getTransaction().commit();
        } catch (Exception exception) {
            em.getTransaction().rollback();
            log.error("Cannot execute method 'findInSession(EntityManagerFactory factory)'");
        } finally {
            em.close();
        }
        return person;
    }

    /**
     * updates
     */
    private static void findInSession(EntityManagerFactory factory) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();


            Person person = em.find(Person.class, 1L);
            log.info("person before update = {}", person);

            person.setFirstName("NewFirstName");

            person = em.find(Person.class, 1L);
            log.info("person  after update = {}", person);


            em.getTransaction().commit();
        } catch (Exception exception) {
            em.getTransaction().rollback();
            log.error("Cannot execute method 'findInSession(EntityManagerFactory factory)'");
        } finally {
            em.close();
        }
    }

    /**
     * doesn't delete detached ("Removing a detached instance")
     */
    private static void deleteDetached(Person person, EntityManagerFactory factory) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();


            em.remove(person);


            em.getTransaction().commit();
        } catch (Exception exception) {
            em.getTransaction().rollback();
            log.error("Cannot execute method 'deleteDetached(Person person, EntityManagerFactory factory)'");
        } finally {
            em.close();
        }
    }

    /**
     * deletes reAttached
     */
    private static void deleteAttached(Person person, EntityManagerFactory factory) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();


            Person attachedToSessionPerson = em.merge(person);
            em.remove(attachedToSessionPerson);


            em.getTransaction().commit();
        } catch (Exception exception) {
            em.getTransaction().rollback();
            log.error("Cannot execute method 'deleteAttached(Person person, EntityManagerFactory factory)'");
        } finally {
            em.close();
        }
        log.info("person has been deleted");
    }
}
