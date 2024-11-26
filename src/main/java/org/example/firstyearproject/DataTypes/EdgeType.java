package org.example.firstyearproject.DataTypes;

import java.io.Serializable;

public class EdgeType implements Serializable {
    public static boolean isRoad(String value) {
        return switch (value) {
            case "motorway", "trunk", "primary", "secondary", "tertiary", "motorway_link", "trunk_link", "primary_link",
                 "secondary_link", "tertiary_link", "service", "living_street", "residential", "road", "unclassified" -> true;
            default -> false;
        };
    }
    /* "motorway", "trunk", "primary", "secondary", "tertiary", "motorway_link", "trunk_link", "primary_link",
            "secondary_link", "tertiary_link", "service", "living_street", "residential", "road"

     */
}
