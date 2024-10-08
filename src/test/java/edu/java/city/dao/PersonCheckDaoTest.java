package edu.java.city.dao;

import edu.java.city.domain.PersonRequest;
import edu.java.city.domain.PersonResponse;
import edu.java.city.exception.PersonCheckException;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PersonCheckDaoTest {

    @Test
    public void checkPerson() throws PersonCheckException {
        PersonRequest pr = new PersonRequest();
        pr.setSurName("Veluchko");
        pr.setGivenName("Pavlo");
        pr.setPatronymic("Mukolayovuch");
        pr.setDateOfBirth(LocalDate.of(1995,3,18));
        pr.setStreetCode(1);
        pr.setBuilding("10");
        pr.setExtension("2");
        pr.setApartment("121");

        PersonCheckDao dao = new PersonCheckDao();
        dao.setConnectionBuilder(new DirectConnectionBuilder());
        PersonResponse personResponse = dao.checkPerson(pr);
        Assert.assertTrue(personResponse.isRegistered());
        Assert.assertFalse(personResponse.isTemporal());
    }

    @Test
    public void checkPerson2() throws PersonCheckException {
        PersonRequest pr = new PersonRequest();
        pr.setSurName("Veluchko");
        pr.setGivenName("Iruna");
        pr.setPatronymic("Petrivna");
        pr.setDateOfBirth(LocalDate.of(1997,8,21));
        pr.setStreetCode(1);
        pr.setBuilding("10");
        pr.setExtension("2");
        pr.setApartment("121");

        PersonCheckDao dao = new PersonCheckDao();
        dao.setConnectionBuilder(new DirectConnectionBuilder());
        PersonResponse personResponse = dao.checkPerson(pr);
        Assert.assertTrue(personResponse.isRegistered());
        Assert.assertFalse(personResponse.isTemporal());
    }
}