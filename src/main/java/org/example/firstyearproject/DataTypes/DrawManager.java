package org.example.firstyearproject.DataTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.example.firstyearproject.MapObjects.MapObject;
import org.example.firstyearproject.MapObjects.Relation;

public class DrawManager implements Iterable<MapObject>, Serializable {
    ArrayList<ArrayList<MapObject>> storage;
    public DrawManager(){
        storage = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            storage.add(new ArrayList<>());
        }
    }
    // Level 0 - Background
    // Level 1 - Relations
    // Level 2 -
    // Level 3 - Roads
    public int getDrawlevel(MapObject mapObject) {
        if(mapObject instanceof Relation)           return 1;
        if(ColorGroup.isRoad(mapObject.colorGroup)) return 3;

        switch(mapObject.colorGroup){
            case NATURAL_COASTLINE_FILL: return 0;
            case BUILDINGS_GENERIC: return 3;
            default: return 2;
        }
    }

    public void add(MapObject mapObject) {
        int level = getDrawlevel(mapObject);
        ArrayList<MapObject> arr = storage.get(level);
        arr.add(mapObject);
    }

    @Override public Iterator<MapObject> iterator() {
        ArrayList<MapObject> temp = new ArrayList<>();

        for(int i = 0; i < storage.size(); i++)
            temp.addAll(storage.get(i));

        return temp.iterator();
    }
}
