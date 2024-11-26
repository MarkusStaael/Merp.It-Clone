package org.example.firstyearproject.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.example.firstyearproject.DataTypes.DrawManager;
import org.example.firstyearproject.MapObjects.MapObject;
import org.example.firstyearproject.Misc.Box;
import org.example.firstyearproject.Misc.MapObjectComparator;

public class KDTree implements Serializable {
    public KDTree left;
    public KDTree right;
    public MapObject point;
    public Box minMax;
    int d;
    public KDTree(List<MapObject> arr){
        this(arr, 0);
    }

    private KDTree(List<MapObject> arr, int d){
        if(arr.size()==0) return;

        this.d = d;
        arr.sort(new MapObjectComparator(d));
        d = (d + 1) % 2;
        int n = arr.size();
        int m = (int)Math.floor((double)n/2);
        this.point = arr.get(m);
        this.left = null;
        this.right = null;
        if(m > 0){
            this.left = new KDTree(new ArrayList<>(arr.subList(0, m)),d);
        }
        if(n - (m + 1) > 0){
            this.right = new KDTree(new ArrayList<>(arr.subList(m+1, n)),d);
        }

        minMax = point.area;
        updateMinMax();

    }

    public void updateMinMax(){
        if(left!=null) {
            minMax.getMinMax(left.minMax);
        }
        if(right!=null) {
            minMax.getMinMax(right.minMax);
        }
    }


    public boolean inBox(MapObject mapObject, Box searchArea){
        if(mapObject == null) return false;
        return searchArea.intersects(mapObject.area);
    }

    public DrawManager rangesearch(Box box){
        DrawManager drawManager = new DrawManager();
        return rangesearch(box,drawManager);
    }

    public DrawManager rangesearch(Box searchArea, DrawManager drawManager){
        MapObject mapObject = this.point;

        if(inBox(mapObject, searchArea)){
            drawManager.add(mapObject);
        }

        if(left != null)
            if(left.minMax.intersects(searchArea))
                left.rangesearch(searchArea, drawManager);

        if(right != null)
            if(right.minMax.intersects(searchArea))
                right.rangesearch(searchArea, drawManager);

        return drawManager;
    }

    public String toString(){
        return "(node: "+ point+").";
    }
}