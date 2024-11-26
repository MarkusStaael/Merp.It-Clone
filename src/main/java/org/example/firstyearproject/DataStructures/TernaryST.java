package org.example.firstyearproject.DataStructures;

import org.example.firstyearproject.DataTypes.Address;
import org.example.firstyearproject.MapObjects.Node;

import java.io.Serializable;

/**
 * The Ternary Search Tree is a data structure used for searching addresses within the map.
 */
public class TernaryST implements Serializable {
    Address root; //the root of the tree
    public int n; //size

    public void put(String key, Node value) {
        if (!contains(key)) n++;
        else if (value == null) n--;       // delete existing key
        root = put(root, key, value, 0);
    }
    public boolean contains(String key) {
        return get(key) != null;
    }

    // returns the address' nodeID.
    public Node get(String key) {
        Address address = get(root, key, 0);
        if (address == null) return null;
        return address.node;
    }

    // return subtrie corresponding to given key
    public Address get(Address address, String word, int d) {
        if (address == null) return null;

        char c = word.charAt(d);
        if (c < address.getCharacter()) return get(address.left, word, d);
        else if (c > address.getCharacter()) return get(address.right, word, d);
        else if (d < word.length() - 1) return get(address.middle, word, d+1);
        else { return address; }
    }

    public Address put(Address address, String word, Node val, int d) {
        char c = word.charAt(d); //character at index d
        if (address == null) {
            address = new Address();
            address.character = c;
        }

        if (c < address.getCharacter()) {
            address.left = put(address.left, word, val, d);
        }
        else if (c > address.getCharacter()) address.right = put(address.right, word, val, d);
        else if (d < word.length() - 1)  address.middle   = put(address.middle, word, val, d+1);
        else address.node = val;
        return address;
    }
}
