package org.example.firstyearproject.MapObjects;

import javafx.scene.canvas.GraphicsContext;
import org.example.firstyearproject.DataTypes.ColorGroup;

import javax.xml.stream.XMLStreamReader;
import java.io.Serializable;
import java.util.*;

public class Relation extends MapObject implements Serializable {
    List<Way> innerWays;
    public List<Way> outerWays;
    //public String name = "";
    public ArrayList<Long> idOfChildren; // TODO: Remove this when not needed

    public Relation(List<Way> innerWays, List<Way> outerWays) {
        this.innerWays = innerWays;
        this.outerWays = outerWays;
        this.colorGroup = ColorGroup.NO_KEY;
        this.idOfChildren = new ArrayList<>();
    }

    // TODO: Need to check if ways for relation are closed or not

    public void addOuterWays(Way way,Long id){
        if(way==null) return;

        if(area==null) area = way.area;
        else           area.getMinMax(way.area);
        outerWays.add(way);
    }

    public void clearID() {
        idOfChildren = null;
    }

    public void addIdOfChildren(Long l) {
        idOfChildren.add(l);
    }

    public void printOuterWays() {
        System.out.println("Printing outerWays for "+this);
        for(int i = 0; i<outerWays.size();i++) {
            System.out.println(i+": "+outerWays.get(i));
        }
    }

    public void printChildrenIds() {
        System.out.println("Printing idOfChildren for "+this);
        for(int i = 0; i<idOfChildren.size();i++) {
            System.out.println(i+": "+idOfChildren.get(i));
        }

    }

    @Override public void draw(GraphicsContext gc) {

        //printOuterWays();
        if(cantDraw(colorGroup)) return;

        ArrayList<Way> nonclosed = new ArrayList<>();
        for(Way w: outerWays) {
            if(w.isClosed()) {
                w.draw(gc, colorGroup);
            } else {
                nonclosed.add(w);
            }
        }

        if(nonclosed.size()==0) return;
        //if(colorGroup.equals(ColorGroup.NATURAL_COASTLINE_FILL)||colorGroup.equals(ColorGroup.NATURAL_COASTLINE_LINE)) return;

        drawNonclosed(gc,nonclosed);

    }



    public void drawNonclosed(GraphicsContext gc, ArrayList<Way> nonclosed){
        var arr = MapObjectHelper.getNodesInOrder(nonclosed);
        Way grouping = new Way(arr,colorGroup);
        grouping.draw(gc,colorGroup);
    }

    public void parse_tag(XMLStreamReader input) {
        String key = input.getAttributeValue(null, "k");
        String value = input.getAttributeValue(null, "v");

        ColorGroup cg =  ColorGroup.getType(key,value);
        if(cg.equals(ColorGroup.NO_KEY)) {
           // if(key.equals("name")) name = value;

        } else {
            colorGroup = cg;
        }

    }


        public String toString() {
            return "Relation ("+colorGroup+") with: "+outerWays.size()+" members.";
        }


}
