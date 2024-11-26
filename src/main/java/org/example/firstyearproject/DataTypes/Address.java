package org.example.firstyearproject.DataTypes;

import java.io.Serializable;

import org.example.firstyearproject.MapObjects.Node;

public class Address implements Serializable {
    public char character;
    public Address left, middle, right;
    short HouseNumber;
    char HouseLetter;
    String Street;
    String City;
    short PostCode;
    String Municipality;

    public Node node;
    public Address(){
         this.node = null;
    }
    public Address(String street, String postcode, String city, String HouseNumber, String municipality, Node node)
    {
        this.Street = street;
        this.PostCode = Short.parseShort(postcode);
        this.City = city;
        setHouseNumber(HouseNumber);
        this.Municipality = municipality;
        this.node = node;
    }
    public char getCharacter() {
        return character;
    }
    public boolean isEnd() {
        return node != null;
    }
    public void setHouseNumber(String a){
        char lastchar = a.charAt(a.length() - 1);
        if (Character.isDigit(lastchar))
        {
            HouseNumber = Short.parseShort(a);
            HouseLetter = '\0';
        }
        else
        {
            HouseNumber = Short.parseShort(a.substring(0, a.length() - 1));
            HouseLetter = lastchar;
        }

        //this.address[0]=a;
    }
    public void setStreet(String a){
        this.Street = a;
    }
    public void setCity(String a){
        this.City = a;
        //this.address[2]=a;
    }
    public void setPostcode(String a){
        this.PostCode = Short.parseShort(a);
        }
    public void setMunicipality(String a){
        this.Municipality = a;
    }
    //public void setCountry(String a){ this.address[5]=a;}
    public void setNode(Node a){this.node = a;}

    public String toString(){
        if (HouseLetter=='\0'){
            return Street +" "+ HouseNumber+", "+ PostCode +" "+ City;
        }
        else return Street +" "+ HouseNumber + HouseLetter +", "+ PostCode +" "+ City;
    }
    public String lexiString(){return "DK"+ Municipality + PostCode + City + Street;}

    public Node getNode(){
        return this.node;
    }

    public static boolean isGreater(Address addr1, Address addr2) {
        String addr1street = addr1.lexiString();
        String addr2street = addr2.lexiString();

        if (addr1street.compareTo(addr2street) == 0) { //Same street
            if (addr1.HouseNumber == addr2.HouseNumber) { //same number
                if (addr1.HouseLetter != '\0' && addr2.HouseLetter != '\0'){ //both have letters
                    return Character.compare(addr1.HouseLetter, addr2.HouseLetter) > 0;
                }
                else return addr1.HouseLetter != '\0';

            }else return addr1.HouseNumber > addr2.HouseNumber;
        }
        return false;
    }
}
