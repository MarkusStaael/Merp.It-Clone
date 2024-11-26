package org.example.firstyearproject.DataStructures;

import org.example.firstyearproject.MapObjects.Edge;
import org.example.firstyearproject.MapObjects.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EdgeWeightedGraphTest {
    EdgeWeightedGraph G;
    Edge edge0, edge1;
    Node node0, node1, node2;
    ArrayList<Edge> edges;

    @BeforeEach
    void setUp() {
        G = new EdgeWeightedGraph(3000);
        node0 = new Node(55.145965F, 14.773833F, 0);
        node1 = new Node(55.097210F, 14.858253F, 1);
        node2 = new Node(55.125936F, 14.793812F, 2);

        edge0 = new Edge(node0, node1, node0.counter, node1.counter());
        edge1 = new Edge(node1, node2, node1.counter, node2.counter());

        G.addEdge(edge0);
        G.addEdge(edge1);

        edges = new ArrayList<>();
        edges.add(edge0); edges.add(edge1);
    }

    @Test
    public void adj() {
        assertEquals(G.adj(0), G.adj(edge0));
        assertEquals(G.adj(1), G.adj(edge1));
    }

    @Test
    public void addEdge() {
        assertEquals(G.adj(0), G.adj(edge0));
        assertEquals(G.adj(1), G.adj(edge1));
        assertNotNull(G);
        assertEquals(2, G.E);

    }

    @Test
    public void V() {
        assertEquals(3000, G.V);
    }

    @Test
    public void edges() {
        assertEquals(edges, G.edges());
    }

    @Test
    public void findNearestWayNode() {
        Node node3 = new Node(56.125936F, 15.793812F, 3);
        Edge edge2 = new Edge(node0, node2, node0.counter, node2.counter());
        G.addEdge(edge2);
        assertEquals(node1, G.findNearestWayNode(node3, G));
    }
}