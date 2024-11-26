package org.example.firstyearproject.Misc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Scanner;

public class PropertyHandler implements Serializable {

    public boolean hasLoaded = false;

    public void load() throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader("test.json"));

        System.out.println(sc.next());

    }

}
