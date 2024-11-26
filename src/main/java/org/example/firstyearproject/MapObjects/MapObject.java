package org.example.firstyearproject.MapObjects;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import org.example.firstyearproject.DataTypes.ColorGroup;
import org.example.firstyearproject.Misc.Box;
import org.example.firstyearproject.Misc.Settings;

public abstract class MapObject implements Serializable {
    public ColorGroup colorGroup;
    public Box area;

    public float getSize() {
        return area.minX* area.minY;
    }

    public abstract void draw(GraphicsContext gc);
    public float getCenterLat() {
        return area.minY+(area.maxY-area.minY)/2;
    }
    public float getCenterLon() {
        return area.minX+(area.maxX-area.minX)/2;
    }

    public boolean cantDraw(ColorGroup cg) {
        return cg.compareTo(ColorGroup.NO_VALUE)<0;
    }
    Color getColor(ColorGroup cg) {
        return Settings.getColor(cg);
    }

}
