package org.example.firstyearproject.MapObjects;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

import org.example.firstyearproject.Misc.Box;
import org.example.firstyearproject.DataTypes.ColorGroup;
import org.example.firstyearproject.Misc.Settings;

public class Way extends MapObject implements Serializable {
    public ArrayList<Node> nodes;

    public Way(ArrayList<Node> nodes, ColorGroup colorGroup) {
        this.nodes      = nodes;
        this.colorGroup = colorGroup;
        findMinMax();
    }

    public void findMinMax() {
        if(nodes.size()==0)
            return;

        // Initial assignment
        area = new Box(nodes.get(0).lon,nodes.get(0).lon,nodes.get(0).lat,nodes.get(0).lat);

        // Real assignment
        for(Node node: nodes) {
            Box nodebox = new Box(node.lon,node.lon,node.lat,node.lat);
            area.getMinMax(nodebox);
        }
    }


    public int connectsTo(Way w) {
        return connectsTo(w.nodes);
    }

    /**
     * return -1 for error
     * return 0 for false
     * return 1 for this.start connects to w.start
     * return 2 for this.start connects to w.end
     * return 3 for this.end connects to w.start
     * return 4 for this.end connects to w.end
     * @param arr
     * @return
     */
    public int connectsTo(ArrayList<Node> arr) {
        if(this.nodes.size()==0) return -1;
        if(arr.size()==0)   return -1;

        Node tStart = nodes.get(0);
        Node tEnd = nodes.get(nodes.size()-1);
        Node wStart = arr.get(0);
        Node wEnd = arr.get(arr.size()-1);


        if(tStart.equals(wStart))   return 1;
        if(tStart.equals(wEnd))     return 2;
        if(tEnd.equals(wStart))     return 3;
        if(tEnd.equals(wEnd))       return 4;

        return 0;
    }

    @Override public void draw(GraphicsContext gc) {
        draw(gc,colorGroup);
    }

    void draw(GraphicsContext gc, ColorGroup cg) {
        if(cantDraw(cg)) return;
        if(cg.isRoad(cg))   simpleDraw(gc,cg);
        else fill(gc,cg);
    }

    public boolean isClosed() {
        if(nodes.size()==0) return true;
        return nodes.get(0).equals(nodes.get(nodes.size()-1));
    }

    public void fill(GraphicsContext gc,ColorGroup colorGroup) {
        gc.setFill(getColor(colorGroup));
        double[] xPoints = new double[nodes.size()], yPoints = new double[nodes.size()];
        for(int i = 0;i<nodes.size();i++) {
            xPoints[i]=(double) nodes.get(i).lon*Settings.getLonOffset();
            yPoints[i]=(double) nodes.get(i).lat*Settings.getLatOffset();
        }
        gc.fillPolygon(xPoints,yPoints,nodes.size());
    }
    public void simpleDraw(GraphicsContext gc,ColorGroup colorGroup) {
        gc.setStroke(getColor(colorGroup));
        gc.beginPath();
        gc.setLineWidth(ColorGroup.getLineWidth(colorGroup));
        gc.moveTo((double) nodes.get(0).lon*Settings.getLonOffset(), (double) nodes.get(0).lat*Settings.getLatOffset());
        for(int i = 1;i<nodes.size();i++) {
            double x = (double) nodes.get(i).lon*Settings.getLonOffset();
            double y = (double) nodes.get(i).lat*Settings.getLatOffset();
            gc.lineTo(x, y);
        }
        gc.stroke();
    }

    public String toString() {
        return "Way ("+colorGroup+") with "+(nodes.size())+" nodes.";
    }

    public void setColorGroup(ColorGroup colorGroup) {
        this.colorGroup = colorGroup;
    }
}