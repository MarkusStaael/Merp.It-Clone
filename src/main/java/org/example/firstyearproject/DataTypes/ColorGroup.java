package org.example.firstyearproject.DataTypes;

import java.io.Serializable;

public enum ColorGroup implements Serializable {
    NO_KEY,
    NO_VALUE,
    ROADS_GENERIC,
    HIGHWAY_RESIDENTIAL,
    HIGHWAY_TERTIARY,
    HIGHWAY_UNCLASSIFIED,
    HIGHWAY_PATH,
    TREES_GENERIC,
    WATER_GENERIC,
    LAND_GENERIC,
    BUILDINGS_GENERIC,
    SAND_GENERIC,
    LANDUSE_INDUSTRIAL,
    LANDUSE_RESIDENTIAL,
    LANDUSE_RECREATION_GROUND, //landuse	recreation_ground
    AMENDITY_PARKING, // parking
    ROUTE_BYCYCLE,
    NATURAL_SCRUB,
    NATURAL_COASTLINE_FILL,
    NATURAL_COASTLINE_LINE,
    HIGHWAY_CYCLEWAY,
    HIGHWAY_SECONDARY,
    HIGHWAY_MOTORWAY,
    HIGHWAY_PEDESTRIAN,
    HIGHWAY_TRUNK_PRIMARY,
    HIGHWAY_TRACK,
    INDUSTRIAL_PORT,
    HIGHWAY_ROUTE,
    WATERWAY_STREAM,
    GRAPH;

    public static boolean isRoad(ColorGroup colorGroup) {
        switch(colorGroup) {
            case ROADS_GENERIC, HIGHWAY_RESIDENTIAL, HIGHWAY_TERTIARY, HIGHWAY_UNCLASSIFIED, HIGHWAY_PATH,
                 HIGHWAY_TRUNK_PRIMARY, HIGHWAY_CYCLEWAY, HIGHWAY_MOTORWAY, HIGHWAY_SECONDARY, HIGHWAY_PEDESTRIAN,
                    HIGHWAY_TRACK, WATERWAY_STREAM,NATURAL_COASTLINE_LINE: return true;
            default: return false;
        }
    }
    public static double getLineWidth(ColorGroup colorGroup) {
        switch(colorGroup) {
            case HIGHWAY_PATH, HIGHWAY_CYCLEWAY, HIGHWAY_TRACK: return 0.00003;
            case HIGHWAY_PEDESTRIAN: return 0.00004;
            case HIGHWAY_RESIDENTIAL: return 0.00005;
            case HIGHWAY_TERTIARY,HIGHWAY_UNCLASSIFIED: return 0.00010;
            case HIGHWAY_ROUTE: return 0.000127;
            default: return 0.00005d;
        }
    }

    public static ColorGroup getType(String key, String value) {
        switch(key){
            case "water": return ColorGroup.WATER_GENERIC; // 1
            case "waterway":
                switch(value) {
                    case "stream","ditch","river" : return WATERWAY_STREAM;
                    default: return WATERWAY_STREAM;
                }
            case "leisure":
                switch(value) {
                    case "park","pitch": return ColorGroup.LAND_GENERIC;
                    default: return ColorGroup.NO_KEY;
                }
            case "building": return ColorGroup.BUILDINGS_GENERIC;
            case "industrial":
                switch (value) {
                    case "port": return INDUSTRIAL_PORT;
                    default: return NO_KEY;
                }
            case "highway":
                switch(value) {
                    case "residential","living_street", "service":         return HIGHWAY_RESIDENTIAL;
                    case "tertiary":                            return HIGHWAY_TERTIARY;
                    case "unclassified":                        return HIGHWAY_UNCLASSIFIED;
                    case "path", "footway", "steps":            return HIGHWAY_PATH;
                    case "trunk", "primary": return HIGHWAY_TRUNK_PRIMARY;
                    case "cycleway": return HIGHWAY_CYCLEWAY;
                    case "secondary": return HIGHWAY_SECONDARY;
                    case "motorway": return HIGHWAY_MOTORWAY;
                    case "pedestrian": return HIGHWAY_PEDESTRIAN;
                    case "track": return HIGHWAY_TRACK;
                    case "motorway_link", "trunk_link", "primary_link",
                            "secondary_link", "tertiary_link": return ColorGroup.ROADS_GENERIC;
                    default: return ColorGroup.NO_VALUE;

                }
            case "natural":
                switch(value) {
                    case "scrub": return NATURAL_SCRUB;
                    case "bay", "water": return ColorGroup.WATER_GENERIC;
                    case "beach": return ColorGroup.SAND_GENERIC;
                    case "wood","heath": return ColorGroup.TREES_GENERIC;
                    case "coastline": return ColorGroup.NATURAL_COASTLINE_LINE;
                    case "peninsula": return ColorGroup.NATURAL_COASTLINE_FILL;
                    default: return ColorGroup.NO_KEY;
                }
            case "landuse":
                switch (value){
                    case "forest": return ColorGroup.TREES_GENERIC;
                    case "farmland","grass","cemetery","meadow": return ColorGroup.LAND_GENERIC;
                    case "industrial","residential": return ColorGroup.LANDUSE_INDUSTRIAL;
                    case "recreation_ground": return ColorGroup.LANDUSE_RECREATION_GROUND;
                    case "farmyard": return LAND_GENERIC;
                    case "military": return NO_KEY;
                    default: return ColorGroup.NO_KEY;
                }
            case "place":
                switch(value) {
                    case "islet": return ColorGroup.SAND_GENERIC;
                    case "island","archipelago": return NATURAL_COASTLINE_FILL;
                    default: return ColorGroup.NO_KEY;
                }
            case "amendity":
                switch (value) {
                    case "parking": return ColorGroup.AMENDITY_PARKING;
                    default: return ColorGroup.NO_KEY;
                }
            case "route":
                switch (value) {
                    case "bicycle": return ColorGroup.ROUTE_BYCYCLE;
                    case "ferry": return ColorGroup.NO_KEY;
                    default: return ColorGroup.NO_VALUE;
                }
        }
        return ColorGroup.NO_KEY;
    }
}
