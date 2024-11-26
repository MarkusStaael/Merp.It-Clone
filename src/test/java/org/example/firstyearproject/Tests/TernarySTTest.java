package org.example.firstyearproject.Tests;

import org.example.firstyearproject.DataStructures.TernaryST;
import org.example.firstyearproject.DataTypes.Address;
import org.example.firstyearproject.MapObjects.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.*;

class TernarySTTest {
    Address root;
    Address address0, address1;
    private Address left, middle, right;
    TernaryST TST = new TernaryST();
    Node node0, node1, node2;

    //—————————————————————————————————————————————————————————————//

    @BeforeEach
    void setUp() {
        address0 = new Address();
        address1 = new Address();

        address0.setHouseNumber("27");
        address0.setStreet("Vejvej");
        address0.setCity("København");
        address0.setPostcode("2300");

        address1 = new Address();
        address1.setHouseNumber("23");
        address1.setStreet("Langgade");
        address1.setCity("København");
        address1.setPostcode("2300");

        node0 = new Node(55.13031F, 14.30310F, 0);
        node1 = new Node(55.3431F, 15.599F, 1);
        node2 = new Node(55.5021123F, 14.100332F, 2);
    }
    //checks if the ternary search tree actually contains the address
    @Test
    public void testPut() {
        TST.put(address0.toString(), node0);
        assertTrue(TST.contains(address0.toString()));
    }
    //checks if it returns its nodeID value
    @Test
    public void testGetNodeID() {
        TST = new TernaryST();
        TST.put(address0.toString(), node0);
        assertEquals(TST.get(address0.toString()), node0);
    }

    //checks if it returns null if the address _is_ null
    @Test
    public void testGetNullAddress() {
        assertNull(TST.get(null, "hej", 3));
    }
    //checks if a new address is put
    @Test
    public void testGetAddress() {
        TST.put(address0.toString(), node0);
        TST.put(address1.toString(), node1);

        TST.put(address0, address1.toString(), node1, 1);
        assertTrue(TST.contains(address1.toString()));
        assertNotNull(address0.right);
        assertEquals(address1.toString(), "Langgade 23, 2300 København");
    }
}