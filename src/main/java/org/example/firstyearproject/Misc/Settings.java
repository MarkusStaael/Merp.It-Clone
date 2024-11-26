package org.example.firstyearproject.Misc;

import javafx.scene.paint.Color;

import org.example.firstyearproject.DataTypes.ColorGroup;

import java.io.Serializable;

public class Settings implements Serializable {

    static Color coastlineFill, coastlineLine, mainRoads, path, water,land,building,sand,trees, urban,parking,trunk,
            bicycle,secondary,motorway,pedestrian,track,route,graph;

    public static void iniColor(){
        coastlineFill = Color.BEIGE;    coastlineLine = Color.BLACK;    mainRoads = Color.BLACK;
        path = Color.RED;               water = Color.LIGHTBLUE;        land = Color.LIGHTGREEN;
        building = Color.LIGHTGREY;     sand = Color.LIGHTYELLOW;       trees = Color.DARKOLIVEGREEN;

        urban = Color.web("#e6e6e6");                           parking = Color.web("#969696");

        trunk = Color.ORANGE;           bicycle = Color.BLUE;           secondary = Color.YELLOW;
        motorway = Color.RED;           pedestrian = Color.GREY;        track = Color.web("#964B00");
        route = Color.AQUA;             graph = Color.AQUAMARINE;

    }

    public static void darkModeColor() {
        coastlineFill = Color.DARKGREEN;    coastlineLine = Color.BROWN;    mainRoads = Color.LIGHTGREY;
        path = Color.BEIGE;                 water = Color.MIDNIGHTBLUE;       land = Color.OLIVEDRAB;
        building = Color.DARKGREY;          sand = Color.PERU;       trees = Color.FORESTGREEN;

        urban = Color.DIMGREY;             parking = Color.PAPAYAWHIP;

        trunk = Color.MOCCASIN;           bicycle = Color.SKYBLUE;           secondary = Color.LIGHTYELLOW;
        motorway = Color.LIGHTSALMON;     pedestrian = Color.GREY;       track = Color.INDIANRED;
        route = Color.ORANGE;               graph = Color.LIGHTCYAN;
    }

    public static Color getColor(ColorGroup colorGroup) {
        switch(colorGroup) {
            case NATURAL_COASTLINE_FILL: return coastlineFill;
            case NATURAL_COASTLINE_LINE: return coastlineLine;
            case ROADS_GENERIC,ROUTE_BYCYCLE,HIGHWAY_RESIDENTIAL,HIGHWAY_TERTIARY,HIGHWAY_UNCLASSIFIED: return mainRoads;
            case HIGHWAY_PATH: return path;
            case WATER_GENERIC,WATERWAY_STREAM: return water;
            case LAND_GENERIC,LANDUSE_RECREATION_GROUND: return land;
            case BUILDINGS_GENERIC: return building;
            case SAND_GENERIC,NATURAL_SCRUB: return sand;
            case TREES_GENERIC: return trees;
            case LANDUSE_INDUSTRIAL,LANDUSE_RESIDENTIAL: return urban;
            case AMENDITY_PARKING: return parking;
            case HIGHWAY_TRUNK_PRIMARY: return trunk;
            case HIGHWAY_CYCLEWAY: return bicycle;
            case HIGHWAY_SECONDARY: return secondary;
            case HIGHWAY_MOTORWAY: return motorway;
            case HIGHWAY_PEDESTRIAN: return pedestrian;
            case HIGHWAY_TRACK: return track;
            case HIGHWAY_ROUTE: return route;
            case GRAPH: return graph;
        }
        return Color.WHITE;
    }


    // NEVER SET TO 0!!!
    public static float getLatOffset() { return -1f; } // -1f
    public static float getLonOffset() { return 0.56f; } // 0.56f
}
