package org.example.firstyearproject.MapObjects;

import java.io.Serializable;

public class Edge implements Serializable {
    Node node0, node1;
    public float weight; //weight of edge
    public int index0, index1;
    public float speed;

    public Edge(Node node0, Node node1, int index0, int index1) {
        this.node0 = node0;
        this.node1 = node1;
        this.index0 = index0; //equal to node0.counter()
        this.index1 = index1; //equal to node1.counter()
    }

    //returns the edge's "from" index
    public int from() { return index0; }

    //returns the edge's "to" index
    public int to() { return index1; }

    //calculates the distance between two nodes with the haversine formula
    public float calculateDistance(Node node0, Node node1) {
        double lat1Rad = Math.toRadians(node0.getLat());
        double lat2Rad = Math.toRadians(node1.getLat());
        double lon1Rad = Math.toRadians(node0.getLon());
        double lon2Rad = Math.toRadians(node1.getLon());

        double x = (lat2Rad - lat1Rad) * Math.cos((lat2Rad + lat1Rad) / 2);
        double y = (lon2Rad - lon1Rad);
        return (float)Math.sqrt(x * x + y * y) * 6371;
    }

    //calculates the weight of the edge, which is measured in time
    public void calculateWeight(float speed) {
        float dist = calculateDistance(node0, node1);
        this.speed = speed;
        weight = dist / speed;
    }

    public Node getNode0() { return node0; }

    public Node getNode1() { return node1; }

    //returns the weight of the edge
    public double weight() { return weight; }

    public String toString() { return "from index: " + index0 + " to index: " + index1 + " weight: " + weight; }
}