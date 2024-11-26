package org.example.firstyearproject.MapObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class MapObjectHelper implements Serializable {

    /**
     * Given an array of ways, return the nodes in a "stringed" together maner
     * @param ways
     * @return
     */
    public static ArrayList<Node> getNodesInOrder(ArrayList<Way> ways) {
        // Failstates
        if(ways.size()==0){
            System.out.println("ERROR: getNodesInOrder condition 1 not satisfied!");
            return null;
        }
        if(ways.get(0).nodes.size()==0) {
            System.out.println("ERROR: getNodesInOrder condition 2 not satisfied!");
            return null;
        }

        ArrayList<Node> RNodes = new ArrayList<>();
        ArrayList<Boolean> alreadyAdded = new ArrayList<>();
        RNodes.addAll(ways.get(0).nodes);
        alreadyAdded.add(true);
        for(int i = 1; i < ways.size();i++)
            alreadyAdded.add(false);

        // Main
        Boolean completed = false;
        for(int j = 0; j < ways.size() && !completed; j++) {
            completed = true;
            for (int i = 1; i < ways.size(); i++) {
                if(!alreadyAdded.get(i)) {
                    Way w = ways.get(i);
                    completed = GNIO_SWITCH(RNodes, w, alreadyAdded,i,completed);
                }
            }
        }
        ArrayList<Way> redo = new ArrayList<>();
        for(int i = 0; i< ways.size();i++) {
            if(!alreadyAdded.get(i)) {
                redo.add(ways.get(i));
            }
        }
        if(redo.size()!=0) RNodes.addAll(getNodesInOrder(redo));

        return RNodes;
    }

    private static Boolean GNIO_SWITCH(ArrayList<Node> RNodes, Way w, ArrayList<Boolean> ARA,int number,boolean completed) {
        switch (w.connectsTo(RNodes)) {
            default: return false;
            case -1: new RuntimeException("Error in MapObjectHelper getNodesInOrder"); break;
            case 1: GNIO_1(RNodes, w); ARA.set(number,true); break;
            case 2: GNIO_2(RNodes, w); ARA.set(number,true); break;
            case 3: GNIO_3(RNodes, w); ARA.set(number,true); break;
            case 4: GNIO_4(RNodes, w); ARA.set(number,true); break;
        }
        return completed;
    }

    private static void GNIO_1(ArrayList<Node> arr, Way w) {
        Collections.reverse(arr);
        arr.remove(arr.size()-1);
        arr.addAll(w.nodes);
    }
    private static void GNIO_2(ArrayList<Node> arr, Way w) {
        arr.remove(arr.size()-1);
        arr.addAll(w.nodes);
    }

    private static void GNIO_3(ArrayList<Node> arr, Way w) {
        Collections.reverse(arr);

        ArrayList<Node> NArr = new ArrayList<>();
        NArr.addAll(w.nodes);
        Collections.reverse(NArr);

        arr.remove(arr.size()-1);
        arr.addAll(NArr);
    }
    private static void GNIO_4(ArrayList<Node> arr, Way w) {
        ArrayList<Node> NArr = new ArrayList<>();
        NArr.addAll(w.nodes);
        Collections.reverse(NArr);

        arr.remove(arr.size()-1);
        arr.addAll(NArr);
    }


}
