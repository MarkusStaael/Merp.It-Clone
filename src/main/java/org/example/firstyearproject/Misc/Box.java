package org.example.firstyearproject.Misc;

import org.example.firstyearproject.MapObjects.Node;

import java.io.Serializable;

public class Box implements Serializable {
    public float minX,maxX,minY,maxY;

    public Box(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    public Box(Box box) {
        this.minX = box.minX;
        this.maxX = box.maxX;
        this.minY = box.minY;
        this.maxY = box.maxY;
    }

    public void getMinMax(Box box) {
        if(minX>box.minX)   minX    =   box.minX;
        if(maxX<box.maxX)   maxX    =   box.maxX;
        if(minY>box.minY)   minY    =   box.minY;
        if(maxY<box.maxY)   maxY    =   box.maxY;
    }

    public boolean intersects(Box box){
        float[] A = new float[]{this.minX,this.minY,this.maxX,this.maxY};
        float[] B = new float[]{box.minX,box.minY,box.maxX,box.maxY};
        // True if intersecting
        if(!(A[0] > B[2] || A[2] < B[0] || A[1] > B[3] || A[3] < B[1]))
            return true;
        return false;
    }

    public Node[] getMinMax(){
        return new Node[]{new Node(minY, minX),new Node(maxY, maxX)};
    }

}
