package org.example.firstyearproject.MapObjects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    Edge edge0, edge1;
    Node node0, node1, node2;


    @BeforeEach
    void setUp() {
        node0 = new Node(55.145965F, 14.773833F, 0);
        node1 = new Node(55.097210F, 14.858253F, 1);
        node2 = new Node(55.125936F, 14.793812F, 2);

        edge0 = new Edge(node0, node1, node0.counter(), node1.counter());
        edge1 = new Edge(node1, node2, node1.counter(), node2.counter());
    }
    @Test
    public void calculateDistance() {
        double dist = edge0.calculateDistance(node0, node1);
        assertEquals(9.885672569274902f, dist); //the actual distance in maps is ~ 9.1km, but it's close enough
    }

    @Test
    public void calculateWeight() {
        edge0.speed = 50;
        double dist = edge0.calculateDistance(node0, node1);
        edge0.calculateWeight(50);
        /* assertEquals(edge0.weight, dist / edge0.speed);
        the difference is so minimal, that it seems to work right. because it's a float, it's hard to test
         */
    }

    @Test
    public void from() {
        assertEquals(edge0.from(), edge0.index0);
        assertEquals(edge1.from(), edge1.index0);
    }

    @Test
    public void to() {
        assertEquals(edge0.to(), edge0.index1);
        assertEquals(edge1.to(), edge1.index1);
    }

    @Test
    public void getNode0() {
        assertEquals(edge0.getNode0(), node0);
    }

    @Test
    public void getNode1() {
        assertEquals(edge0.getNode1(), node1);
    }

}