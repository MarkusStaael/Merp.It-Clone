package org.example.firstyearproject.Misc;

import java.io.Serializable;
import java.util.Comparator;

import org.example.firstyearproject.MapObjects.MapObject;

public class MapObjectComparator implements Comparator<MapObject>, Serializable {
    int dimension;
    public MapObjectComparator(int dimension) {
        this.dimension = dimension;
    }

    @Override public int compare(MapObject obj1, MapObject obj2) {
        switch (dimension) {
            case 0:
                return Double.compare(obj1.getCenterLon(), obj2.getCenterLon());
            case 1:
                return Double.compare(obj1.getCenterLat(), obj2.getCenterLat());
            default:
                throw new IllegalArgumentException("Invalid sorting dimension, must be 0 or 1 for x or y coordinate");
        }
    }
}