package org.example.firstyearproject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.zip.ZipInputStream;
import javax.xml.stream.*;

import org.example.firstyearproject.DataStructures.EdgeWeightedGraph;
import org.example.firstyearproject.DataTypes.*;
import org.example.firstyearproject.MapObjects.*;
import org.example.firstyearproject.DataStructures.KDTree;
import org.example.firstyearproject.DataStructures.TernaryST;

public class Model implements Serializable {
    TernaryST TST = new TernaryST();
    EdgeWeightedGraph EWG;
    EdgeWeightedGraph cycleEWG;
    ArrayList<KDTree> kdTrees;
    Node tempnode;
    public static int counter;
    static ArrayList<Address> AddressList = new ArrayList<>();
    static ArrayList<String> tempInfo = new ArrayList<>();
    static ArrayList<String> tempCat = new ArrayList<>();

    public float minlat, maxlat, minlon, maxlon;

    public static Model load(String filename) throws IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            System.out.println("opening obj file");
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                System.out.println("loading obj file");
                Model model = (Model) in.readObject();
                System.out.println("Read object done");
                return model;
            }
        }
        return new Model(filename);
    }

    void save(String filename) throws IOException {
        if (filename.endsWith(".obj")) {
            return;
        }
        try (var out = new ObjectOutputStream(new FileOutputStream(filename + ".obj"))) {
            out.writeObject(this);
        }
    }

    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm"))
            parseOSM(filename);
    }

    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);
    }

    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseOSM(new FileInputStream(filename));
    }

    private void parseOSM(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        ArrayList<Relation> relations = new ArrayList<>();

        // TODO: Mulig optimisering med ram her i hashmaps
        HashMap<Long, Node> id2node = new HashMap<>();
        HashMap<Long, Way>  id2way  = new HashMap<>();
        Long nodeID = null;

        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName();
                switch(name) {
                    case "bounds":      parse_maxs(input);                          break;
                    case "node":        nodeID = parse_node(input,id2node);         break;
                    case "way":         parse_way(input,id2way,id2node);            break;
                    case "relation":    parse_relation(input,relations);            break;
                    case "tag":         save_address(input, nodeID, id2node);       break;
                }
            }
        }
        //System.out.println(AddressList.size());
        for(Relation r:relations) {
            for(Long id:r.idOfChildren) {
                Way w = id2way.get(id);
                r.addOuterWays(w,id);
            }
            r.clearID();
        }

        // create array for kdTrees
        ArrayList<MapObject> mapObjects = new ArrayList<>();



        for(Object o :id2way.values().toArray())
            mapObjects.add((MapObject) o);
        for(MapObject o :relations) {
            mapObjects.add(o);
            //if(o.colorGroup.equals(ColorGroup.TREES_GENERIC)) System.out.println((Relation) o);
        }

        ArrayList<ArrayList<MapObject>> categorizedMO = new ArrayList<>();
        for(int j = 0; j<ZoomGroup.zoomLevelThreshHolds.length;j++){
            categorizedMO.add(new ArrayList<>());
        }

        for(MapObject mo: mapObjects){
            if (mo.area==null) continue;
            int zoomlevel = ZoomGroup.getZoomGroup(mo.colorGroup);
            //if( mo.colorGroup.equals(ColorGroup.NATURAL_COASTLINE_FILL)|| mo.colorGroup.equals(ColorGroup.NATURAL_COASTLINE_LINE))
            categorizedMO.get(zoomlevel).add(mo);
        }


        // create kdTrees
        kdTrees = new ArrayList<>();
        for(int k = 0; k<ZoomGroup.zoomLevelThreshHolds.length;k++){
            kdTrees.add(new KDTree(categorizedMO.get(k)));
        }
    }

    private void parse_way(XMLStreamReader input,HashMap<Long, Way> id2way,HashMap<Long,Node> id2node) throws XMLStreamException {
        Long id = Long.parseLong(input.getAttributeValue(null, "id"));
        ArrayList<Node> way = new ArrayList<>();
        ColorGroup type = ColorGroup.NO_KEY;
        ArrayList<Long> nodeIDs = new ArrayList<>();
        float speed = 0;
        float bikespeed = 0;
        boolean twoway = true;
        boolean cycleTwoWay = true;
        boolean highway = false;
        boolean cycleWay = false;
        if (EWG == null) EWG = new EdgeWeightedGraph(id2node.size()+1);
        if (cycleEWG == null) cycleEWG = new EdgeWeightedGraph(id2node.size()+1);

        // Search all children of the way tag
        var tagKind = input.next();
        boolean isInScope = true;
        while(isInScope) {
            if(tagKind==XMLStreamConstants.END_ELEMENT)
                if(input.getLocalName().equals("way")) {
                    if (twoway && highway) {
                        for (int i = nodeIDs.size() - 1; i > 0; i--) {
                            Node node = id2node.get(nodeIDs.get(i));
                            Node node1 = id2node.get(nodeIDs.get(i - 1));
                            Edge edge = new Edge(node, node1, node.counter, node1.counter);
                            edge.calculateWeight(speed);
                            EWG.addEdge(edge);
                        }
                    }
                    if (cycleWay) {
                        ArrayList<Node> cycleWayNodes = new ArrayList<>();
                        for (int i = 0; i < nodeIDs.size() - 1; i++) {
                            Node node = id2node.get(nodeIDs.get(i));
                            Node node1 = id2node.get(nodeIDs.get(i+1));
                            Edge edge = new Edge(node, node1, node.counter, node1.counter);
                            edge.calculateWeight(bikespeed);
                            cycleEWG.addEdge(edge);
                            if (cycleTwoWay) {
                                Edge edge1 = new Edge(node1, node, node1.counter, node.counter);
                                edge1.calculateWeight(bikespeed);
                                cycleEWG.addEdge(edge1);
                            }
                            cycleWayNodes.add(node); cycleWayNodes.add(node1);
                        }
                        cycleEWG.getWays().add(new Way(cycleWayNodes, ColorGroup.GRAPH));
                    }
                    isInScope = false;
                    nodeIDs.clear();
                }

            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                switch (input.getLocalName()) {
                    case "nd":
                        parse_nd(input, id2node, way);
                        if (input.getLocalName().equals("nd")) {
                            long ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                            nodeIDs.add(ref);
                        }
                        break;
                    case "tag":
                        type = parse_tag(input, type);
                        String key = input.getAttributeValue(null, "k");
                        //set speed to the matching type, and add edges to graph
                        if (key.equals("highway")) {
                            String value = input.getAttributeValue(null, "v"); //to parse the speed
                            speed = parse_wayTag(input).getSpeed();
                            if (value.equals("cycleway")) {
                                cycleWay = true;
                                bikespeed = 35;
                            }

                            if (EdgeType.isRoad(value)) {
                                highway = true;
                                ArrayList<Node> wayNodes = new ArrayList<>();
                                for (int i = 0; i < nodeIDs.size() - 1; i++) {
                                    Node node = id2node.get(nodeIDs.get(i));
                                    Node node1 = id2node.get(nodeIDs.get(i+1));
                                    Edge edge = new Edge(node, node1, node.counter, node1.counter);
                                    edge.calculateWeight(speed);
                                    EWG.addEdge(edge);
                                    wayNodes.add(node);
                                    wayNodes.add(node1);
                                }
                                EWG.getWays().add(new Way(wayNodes, ColorGroup.GRAPH));
                            }
                            if (!value.equals("motorway")) {
                                cycleWay = true;
                                if (bikespeed == 0) bikespeed = parse_BikeTag(input).getSpeed();
                            }
                        }
                        if (key.equals("cycleway")) {
                            cycleWay = true;
                            speed = parse_BikeTag(input).getSpeed();
                        }
                        if (key.equals("bicycle")) {
                            String value = input.getAttributeValue(null, "v"); //to also parse cycleways
                            if (value.equals("yes")) cycleWay = true;
                        }
                        if (key.equals("cycleway:right")) {
                            cycleWay = true;
                            cycleTwoWay = false;
                        }
                        if (key.equals("oneway")) {
                            String value = input.getAttributeValue(null, "v"); //to parse oneway roads
                            if (value.equals("yes")) twoway = false;
                        }
                        if (key.equals("ferry")) cycleWay = false;

                        if (key.equals("junction")) {
                            String value = input.getAttributeValue(null, "v"); //to parse roundabouts
                            if (value.equals("roundabout")) { twoway = false; cycleTwoWay = false; }
                        }
                        //speed updated if maxspeed is found
                        if (key.equals("maxspeed")) {
                            try
                            {
                                String value = input.getAttributeValue(null, "v"); //to parse the speed
                                if (!value.equals("DK:urban")) speed = Integer.parseInt(value);
                                else speed = 50;
                            } catch (NumberFormatException e)
                            {
                                speed = 50;
                            }

                        }
                        break;
                }
            }
            tagKind = input.next();
        }
        if(type==ColorGroup.INDUSTRIAL_PORT)
            return;

        id2way.put(id,new Way(way,type));
    }

    private ColorGroup parse_tag(XMLStreamReader input, ColorGroup cg) {
        String key = input.getAttributeValue(null, "k");
        String value = input.getAttributeValue(null, "v");
        ColorGroup t =  ColorGroup.getType(key,value);
        if(t.compareTo(cg)>0)
            return t;
        return cg;
    }

    private EdgeWeight parse_wayTag(XMLStreamReader input) {
        String key = input.getAttributeValue(null, "k");
        if (key.equals("highway")) {
            String value = input.getAttributeValue(null, "v"); //to parse the 'type' of highway
            if (!value.equals("cycleway") && !value.equals("footway") && !value.equals("track") && !value.equals("path") && !value.equals("steps")) {
                return getEdgeType(value);
            }
        }
        return getEdgeType("unclassified");
    }

    private BikeEdgeWeight parse_BikeTag(XMLStreamReader input) {
        String key = input.getAttributeValue(null, "k");
        if (key.equals("cycleway")) {
            String value = input.getAttributeValue(null, "v"); //to parse the 'type' of highway
            return getBikeEdgeType(value);
        } if (key.equals("highway")) {
            String value = input.getAttributeValue(null, "v"); //to parse the 'type' of highway
            if (!value.equals("footway") && !value.equals("track") && !value.equals("path") && !value.equals("steps")) {
                return getBikeEdgeType(value);
            }
        }
        return getBikeEdgeType("unclassified");
    }

    private long parse_node(XMLStreamReader input, HashMap<Long, Node> id2node) {
        var id = Long.parseLong(input.getAttributeValue(null, "id"));
        float lat = Float.parseFloat(input.getAttributeValue(null, "lat"));
        float lon = Float.parseFloat(input.getAttributeValue(null, "lon"));
        id2node.put(id,new Node(lat, lon, counter));
        counter++;

        return id;
    }
    private void parse_maxs(XMLStreamReader input) {
        minlat = Float.parseFloat(input.getAttributeValue(null, "minlat"));
        maxlat = Float.parseFloat(input.getAttributeValue(null, "maxlat"));
        minlon = Float.parseFloat(input.getAttributeValue(null, "minlon"));
        maxlon = Float.parseFloat(input.getAttributeValue(null, "maxlon"));

    }

    /**
     * Parse nd should add the node referenced in nd to the Array way
     * @param input
     * @param id2node
     * @param way
     */
    private void parse_nd(XMLStreamReader input,HashMap<Long, Node> id2node,ArrayList<Node> way) {
        long ref = Long.parseLong(input.getAttributeValue(null, "ref"));
        var node = id2node.get(ref);
        way.add(node);
    }

    private void parse_relation(XMLStreamReader input,ArrayList<Relation> relations) throws XMLStreamException {
        Relation r = new Relation(new ArrayList<>(), new ArrayList<>());
        var tagKind = input.next();
        boolean isInScope = true;
        while(isInScope) {
            if(tagKind==XMLStreamConstants.END_ELEMENT)
                if(input.getLocalName().equals("relation"))
                    isInScope = false;
            if (tagKind == XMLStreamConstants.START_ELEMENT)
                switch (input.getLocalName()) {
                    case "member":
                        parse_member(input,r);
                        break;
                    case "tag":
                        r.parse_tag(input);
                        break;
                }
            tagKind = input.next();
        }
        if(r.colorGroup.compareTo(ColorGroup.NO_KEY)>0) relations.add(r);
    }

    public EdgeWeight getEdgeType(String value) {
        return switch (value) {
            case "motorway" -> EdgeWeight.MOTORWAY;
            case "trunk" -> EdgeWeight.TRUNK;
            case "primary", "secondary", "tertiary" -> EdgeWeight.N_ARY;
            case "residential" -> EdgeWeight.RESIDENTIAL;
            case "living_street" -> EdgeWeight.LIVING_STREET;
            case "service" -> EdgeWeight.SERVICE;
            default -> EdgeWeight.UNCLASSIFIED;
        };
    }
    public BikeEdgeWeight getBikeEdgeType(String value) {
        return switch (value) {
            case "lane" -> BikeEdgeWeight.LANE;
            case "opposite" -> BikeEdgeWeight.OPPOSITE;
            case "track", "opposite_track" -> BikeEdgeWeight.TRACK;
            case "share_busway", "opposite_share_busway" -> BikeEdgeWeight.SHARE_BUSWAY;
            case "shared_lane" -> BikeEdgeWeight.SHARED_LANE;
            case "trunk" -> BikeEdgeWeight.TRUNK;
            case "primary", "secondary", "tertiary" -> BikeEdgeWeight.N_ARY;
            case "residential" -> BikeEdgeWeight.RESIDENTIAL;
            case "living_street" -> BikeEdgeWeight.LIVING_STREET;
            case "service" -> BikeEdgeWeight.SERVICE;
            default -> BikeEdgeWeight.UNCLASSIFIED;
        };
    }

    private void parse_member(XMLStreamReader input, Relation relation) {
        Long ref = Long.parseLong(input.getAttributeValue(null, "ref"));
        String role = input.getAttributeValue(null, "role");
        if(role.equals("outer"))
            relation.addIdOfChildren(ref);
    }

    /**
     * Checks for addr-tag and collects address info in tempArrays.
     * <p>
     * Compiles address info into new Address-object when osak-tag is reached.
     * Compiled address is added to the AddressList
     * */
    void save_address(XMLStreamReader input, Long nodeID, HashMap<Long, Node> id2node) throws XMLStreamException {
        String City = null;
        String Street = null;
        String ZipCode = null;
        String HouseNumber = null;
        String Municipality = null;
        Address a = new Address();
        boolean isAddressSet = false;

        while (input.hasNext()) {
            int tagKind = input.getEventType();

            if (tagKind == XMLStreamConstants.END_ELEMENT) {
                //System.out.println("End Tag");
                //System.out.println(input.getLocalName());
                if (input.getLocalName().equals("node")) {
                    if (City == null && Street == null && ZipCode == null && HouseNumber == null)
                        return;
                    a.setCity(City);
                    //System.out.println("City: " + City + ", Street: " + Street + ", ZipCode: " + ZipCode + ", HouseNumber: " + HouseNumber);
                    a.setStreet(Street.intern());
                    a.setPostcode(ZipCode.intern());
                    a.setNode(id2node.get(nodeID));
                    a.setHouseNumber(HouseNumber.intern());
                    if (Municipality != null)
                        a.setMunicipality(Municipality.intern());
                    //a.setCountry("DK");
                    TST.put(a.toString(), a.node);
                    AddressList.add(a);
                    return;
                }
            } else if (tagKind == XMLStreamConstants.START_ELEMENT && input.getLocalName().equals("tag")) {
                String key = input.getAttributeValue(null, "k");
                String value = input.getAttributeValue(null, "v").intern();

                if (key.split(":")[0].equals("addr")) {
                    switch (key) {
                        case "addr:street": {
                            Street = value;
                            break;
                        }

                        case "addr:city": {
                            City = value;
                            break;

                        }

                        case "addr:postcode": {
                            ZipCode = value;
                            break;
                        }
                        case "addr:housenumber": {
                            HouseNumber = value;
                            break;
                        }
                        case "addr:municipality": {
                            Municipality = value;
                            break;
                        }

                    }
                }
            }
            input.next();
        }
    }
}
