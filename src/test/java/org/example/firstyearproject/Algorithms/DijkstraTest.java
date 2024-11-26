package org.example.firstyearproject.Algorithms;

import org.example.firstyearproject.DataStructures.EdgeWeightedGraph;
import org.example.firstyearproject.MapObjects.Edge;
import org.example.firstyearproject.MapObjects.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DijkstrasTest {
    EdgeWeightedGraph G;
    Edge edge0, edge1, edge2, edge3, edge4, edge5, edge6, edge7, edge8, edge9, edge10;
    Node node0, node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11;
    Dijkstra dijkstras;

    @BeforeEach
    public void setUp() {
        G = new EdgeWeightedGraph(120000);
        node0 = new Node(55.1236F, 14.842819F, 89428);
        node1 = new Node(55.131702F, 14.867189F, 89066);
        node2 = new Node(55.134144F, 14.865516F, 89198);
        node3 = new Node(55.231575F, 14.847789F, 90203);
        node4 = new Node(55.092564F, 14.746669F, 111624);
        node5 = new Node(55.107136F, 14.750469F, 110669);
        node6 = new Node(55.149857F, 14.746273F, 100576);
        node7 = new Node(55.1441F, 14.76325F, 100903);
        node8 = new Node(55.1378F, 14.728189F, 101519);
        node9 = new Node(55.133747F, 14.741837F, 102135);
        node10 = new Node(55.13309F, 14.744517F, 102202);
        node11 = new Node(55.13062F, 14.726606F, 101598);

        List<Edge> edges = new ArrayList<>();
        edge0 = new Edge(node0, node1, node0.counter, node1.counter()); edges.add(edge0);
        edge1 = new Edge(node1, node2, node1.counter, node2.counter()); edges.add(edge1);
        edge2 = new Edge(node5, node3, node5.counter, node3.counter()); edges.add(edge2);
        edge3 = new Edge(node9, node4, node9.counter, node4.counter()); edges.add(edge3);
        edge4 = new Edge(node6, node1, node6.counter, node1.counter()); edges.add(edge4);
        edge5 = new Edge(node6, node2, node6.counter, node2.counter()); edges.add(edge5);
        edge6 = new Edge(node1, node3, node1.counter, node3.counter()); edges.add(edge6);
        edge7 = new Edge(node7, node4, node7.counter, node4.counter()); edges.add(edge7);
        edge8 = new Edge(node7, node3, node7.counter, node3.counter()); edges.add(edge8);
        edge9 = new Edge(node8, node4, node8.counter, node4.counter()); edges.add(edge9);
        edge10 = new Edge(node5, node9, node5.counter, node9.counter); edges.add(edge10);

        for (Edge edge : edges) {
            edge.calculateDistance(edge.getNode0(), edge.getNode1());
            edge.calculateWeight(50);
            System.out.println(edge.weight);
            G.addEdge(edge);
        }
        dijkstras = new Dijkstra(G, node0.counter);

    }
    @Test
    public void relax_and_distTo() {
        dijkstras.relax(edge1);
        assertEquals((double) edge0.weight+edge1.weight, dijkstras.distTo(edge1.index1));
    }

    @Test
    public void hasPathTo() {
        dijkstras.relax(edge3);
        assertTrue(dijkstras.hasPathTo(node3.counter));
    }

    @Test
    public void pathTo() {
        assertNotNull(dijkstras.pathTo(node2));
    }
}