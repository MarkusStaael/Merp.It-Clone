package org.example.firstyearproject.MapObjects;

import java.io.Serializable;

public class Node implements Serializable {
    public float lat, lon;
    public int counter; //each node has an integer, which is used for indexing in the EdgeWeightedGraph

    public Node(float lat, float lon, int counter) {
        this.lat = lat; // y
        this.lon = lon; // x
        this.counter += counter;
    }

    public Node(float lat, float lon) {
        this.lat = lat; // y
        this.lon = lon; // x
    }

    public int counter() {
        return counter;
    }

    public float getLat() { return lat; }
    public float getLon() { return lon; }

    public String toString() {
        return "Node (lat: "+lat+", lon: "+lon+ ", counter: " + counter + ").";
    }
}