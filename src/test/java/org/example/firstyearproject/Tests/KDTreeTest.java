package org.example.firstyearproject.Tests;

import org.example.firstyearproject.DataStructures.KDTree;
import org.example.firstyearproject.DataTypes.ColorGroup;
import org.example.firstyearproject.MapObjects.MapObject;
import org.example.firstyearproject.MapObjects.Node;
import org.example.firstyearproject.MapObjects.Way;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KDTreeTest {
    KDTree tree;
    Node[] box;
    @BeforeEach
    void setup(){

        this.box = new Node[]{new Node(0,1), new Node(5,5)};
        ArrayList<Node> nodes = new ArrayList<>();

        nodes.add(new Node(0,1)); //Lower bound
        nodes.add(new Node(5,5)); //Upper bound
        nodes.add(new Node(5,2)); //Top edge
        nodes.add(new Node(1,2)); //Bottom edge
        nodes.add(new Node(3,5)); //Right edge
        nodes.add(new Node(3,0)); //Left edge
        nodes.add(new Node(3,6)); //Outside box
        nodes.add(new Node(2,1)); //Inside box

        MapObject way = new Way(nodes, ColorGroup.HIGHWAY_PATH);
        List<MapObject> innerWays = new ArrayList<>();
        innerWays.add(way);

        this.tree = new KDTree(innerWays);
    }
/*
    @Test
    public void updateMinMax() {
        tree.updateMinMax();
        assertNotNull(tree.right);
    }

 */
    @Test
    void zeropoint1_INBOX_TEST() {
        //assertTrue(tree.inBox(new Node(0, 1), box)); //Lower bound
    }

    @Test
    void fivepoint5_INBOX_TEST(){
        //assertTrue(tree.inBox(new Node(5,5), box)); //Upper bound
    }

    @Test
    void fivepoint2_INBOX_TEST(){
        //assertTrue(tree.inBox(new Node(5,2), box)); //Top edge
    }

    @Test
    void onepoint2_INBOX_TEST(){
        //assertTrue(tree.inBox(new Node(1,2), box)); //Bottom edge
    }

    @Test
    void threepoint5_INBOX_TEST(){
        //assertTrue(tree.inBox((new Node(3,5)),box)); //Right edge
    }

    @Test
    void threepoint0_INBOX_TEST(){
        //assertFalse(tree.inBox((new Node(3,0)),box)); //Left edge
    }

    @Test
    void threepoint6_INBOX_TEST(){
        //assertFalse(tree.inBox((new Node(3,6)),box)); //Outside box
    }

    @Test
    void twopoint1_INBOX_TEST(){
        //assertTrue(tree.inBox((new Node(2,1)),box)); //Inside box
    }

    @Test
    void rangesearchTest() {
        //Set<Node> insidebox = tree.rangesearch(box);;
        Set<Node> expectedInsideBox = new HashSet<>();
        expectedInsideBox.add(new Node(0,1));
        expectedInsideBox.add(new Node(5,5));
        expectedInsideBox.add(new Node(5,2));
        expectedInsideBox.add(new Node(1,2));
        expectedInsideBox.add(new Node(3,5));
        expectedInsideBox.add(new Node(2,1));

        //assertTrue(insidebox.containsAll(expectedInsideBox));
    }


}

