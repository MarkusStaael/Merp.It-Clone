package org.example.firstyearproject.DataStructures;

import org.example.firstyearproject.MapObjects.Edge;
import org.example.firstyearproject.MapObjects.Node;
import org.example.firstyearproject.MapObjects.Way;

import java.io.Serializable;
import java.util.*;

//a class that constructs a graph of all the weighted edges
public class EdgeWeightedGraph implements Serializable{
    int[] dist;
    int[] in_degree; //the amount of vertices the current vertex points towards
    public int V, E; //amount of vertices and edges
    Bag<Edge>[] adj; //the adjacent nodes of the current node
    ArrayList<Way> ways;

    public EdgeWeightedGraph(int V)  {
        ways = new ArrayList<>();
        this.V = V;
        this.E = 0;
        dist = new int[V];
        this.in_degree = new int[V];

        adj = (Bag<Edge>[]) new Bag[V]; //each index of the array 'adj' contains a 'Bag' to keep track of adjacent tiles
    }

    public int V() { return V; }

    //returns the edges that are adjacent to the node equal to the index v
    public Iterable<Edge> adj(int v) { return adj[v]; }

    //same as the function adj(int v), only with edge as parameter instead. used only for testing purposes
    public Iterable<Edge> adj(Edge edge) { return adj[edge.from()]; }


    public List<Edge> edges() {
        List<Edge> edges = new ArrayList<>();
        for(Bag<Edge> b: adj){
            if(b != null){
                for(Edge e: b){
                    edges.add(e);
                }
            }
        }
        return edges;
    }

    public void addEdge(Edge edge) {
        if (adj[edge.from()] == null) adj[edge.from()] = new Bag<>(); //a bag is created at the given edge index
        adj[edge.from()].add(edge); //an edge is added to v, as there is an adjacent vertex, w
        in_degree[edge.to()]++; //the in_degree of w is increased
//        ways.add(edge.getNode0());
//        ways.add(edge.getNode1());
        E++;

    }
    public Node findNearestWayNode(Node addressNode, EdgeWeightedGraph G) {
        Node nearestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Edge edge : G.edges()) {
            double distance = calculateDistance(addressNode, edge.getNode0());
            double distance1 = calculateDistance(addressNode, edge.getNode1());
            if (distance < minDistance) {
                minDistance = distance;
                nearestNode = edge.getNode0();
            }
            if (distance1 < minDistance) {
                minDistance = distance1;
                nearestNode = edge.getNode1();
            }
        }
        return nearestNode;
    }

    public double calculateDistance(Node node0, Node node1) {
        double lat1Rad = Math.toRadians(node0.getLat());
        double lat2Rad = Math.toRadians(node1.getLat());
        double lon1Rad = Math.toRadians(node0.getLon());
        double lon2Rad = Math.toRadians(node1.getLon());

        double x = (lat1Rad - lat2Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lon1Rad - lon2Rad);

        return Math.sqrt(x * x + y * y) * 6371;
    }

    public ArrayList<Way> getWays() {
        return ways;
    }

    /**
     * The Bag class represents a bag (or multiset) of generic items. It supports insertion and iterating over the items in arbitrary order.
     * This implementation uses a singly linked list with a static nested class 'Node'.
     * @param <Item>
     */
    public class Bag<Item> implements Iterable<Item>, Serializable{
        private Vertex<Item> first;    // beginning of bag
        private int n;               // number of elements in bag

        // helper linked list class
        private static class Vertex<Item> implements Serializable {
            private Item item;
            private Vertex<Item> next;
        }

        public Bag() { //initialises empty bag
            first = null;
            n = 0;
        }

        public boolean isEmpty() {
            return first == null;
        }

        public int size() {
            return n;
        }

        public void add(Item item) {
            Vertex<Item> oldfirst = first;
            first = new Vertex<>();
            first.item = item;
            first.next = oldfirst;
            n++;
        }


        public Iterator<Item> iterator() {
            return new LinkedIterator(first);
        }

        private class LinkedIterator implements Iterator<Item>, Serializable{
            private Vertex<Item> current;

            public LinkedIterator(Vertex<Item> first) {
                current = first;
            }

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }
        }
    }
}