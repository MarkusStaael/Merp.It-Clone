package org.example.firstyearproject.DataTypes;

import org.example.firstyearproject.MapObjects.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    char character;
    short HouseNumber;
    char Houseletter;
    String street;
    String city;
    short Postcode;
    String Municipality;
    Node node;
    Address a, a1, a2;

    @BeforeEach
    void setUp() {
        a = new Address();
        a.character = 'C';
        a.setHouseNumber("13");
        a.setStreet("Langgade");
        a.setCity("København S");
        a.setMunicipality("København");
        a.setNode(new Node(55.55f, 14.95f, 0));

        a1 = new Address();
        a1.character = 'L';
        a1.setHouseNumber("17B");
        a1.setStreet("Langgadevej");
        a1.setCity("København S");
        a1.setPostcode("2300");
        a1.setMunicipality("København");

        a2 = new Address();
        a2.character = 'M';
        a2.setHouseNumber("196");
        a2.setStreet("Langgadevej");
        a2.setCity("København S");
        a2.setPostcode("2300");
        a2.setMunicipality("København");
    }

    @Test
    public void getCharacter() {
        assertEquals(a.character, a.getCharacter());
    }

    @Test
    public void setHouseNumber() {
        assertInstanceOf(Short.class, a.HouseNumber);
    }

    @Test
    public void setHouseletter() { assertInstanceOf(Character.class, a.HouseLetter);}

    @Test
    public void setStreet() { assertInstanceOf(String.class, a.Street); }

    @Test
    public void setCity() { assertInstanceOf(String.class, a.City); }

    @Test
    public void isGreater() {
        assertTrue(Address.isGreater(a2, a1));
    }
}