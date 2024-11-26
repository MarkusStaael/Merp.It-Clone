package org.example.firstyearproject.DataTypes;

import java.io.Serializable;

public enum ZoomGroup implements Serializable {
    ;
    //ZOOM_STANDARD, // 0
    //ZOOM_BACKYARD, // 1
    //ZOOM_CITY,     // 2
    //ZOOM_REGION,   // 3
    //ZOOM_COUNTRY;  // 4

    public static int getZoomGroup(ColorGroup colorGroup) {
        switch(colorGroup) {

            case HIGHWAY_RESIDENTIAL, BUILDINGS_GENERIC, HIGHWAY_UNCLASSIFIED, HIGHWAY_PATH, NATURAL_SCRUB,
                 HIGHWAY_CYCLEWAY, HIGHWAY_SECONDARY, HIGHWAY_PEDESTRIAN, HIGHWAY_TRACK,ROUTE_BYCYCLE,AMENDITY_PARKING:
                return 2;
            case HIGHWAY_TERTIARY,LANDUSE_RESIDENTIAL,LANDUSE_INDUSTRIAL,ROADS_GENERIC,WATERWAY_STREAM,TREES_GENERIC:
                return 3;
            case WATER_GENERIC, LAND_GENERIC, SAND_GENERIC, LANDUSE_RECREATION_GROUND,HIGHWAY_MOTORWAY,HIGHWAY_TRUNK_PRIMARY: return 4;
            default: return 0;
        }
    }

    public static double[] zoomLevelThreshHolds = new double[]{0d,50_000d,30_000d,5_000d,1_000d};
}
