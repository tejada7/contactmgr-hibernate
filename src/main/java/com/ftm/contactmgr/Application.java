package com.ftm.contactmgr;

import com.ftm.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


public class Application {
    // Hold a reusable reference to a SessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = builSessionFactory();

    private static SessionFactory builSessionFactory() {
        // Create a standardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        // Using builder design pattern to create the object
        Contact contact = new Contact.ContactBuilder("May", "Tejada")
                .withEmail("faviotejada7@gmail.com")
                .withPhone(72526696L)
                .build();
        int id = save(contact);

        System.out.println("------ Before the update ------");
        // Display a list of contacts before the update
        fetchAllContacts().stream()
                .forEach(System.out::println);

        // Get the persistent contact
        Contact c = findContactById(id);

        // Update the contact
        c.setFirstname("Alexandre");

        // Persists the changes
        update(c);

        System.out.println("------ After the update ------");

        // Display the list of contacts after the update
        fetchAllContacts().stream()
                .forEach(System.out::println);

        delete(c);

        System.out.println("------ After the delete ------");

        // Display the list of contacts after the update
        fetchAllContacts().stream()
                .forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class, id);

        // Close the session
        session.close();

        // Return the object
        return contact;
    }

    private static int save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int) session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

        return id;
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create a criteria object
        Criteria criteria = session.createCriteria(Contact.class);

        // Get a list of contact objetcs according to the criteria object
        List<Contact> contacts = criteria.list();

        // Close the session
        session.close();

        // Return the list
        return contacts;
    }

    private static void update(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the transaction
        session.update(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    private static void delete(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(contact);
        session.getTransaction().commit();
        session.close();
    }
}
