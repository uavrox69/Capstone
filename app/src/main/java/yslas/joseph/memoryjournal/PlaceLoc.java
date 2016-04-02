package yslas.joseph.memoryjournal;
import java.io.Serializable;


/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class PlaceLoc implements Serializable {

    public String id;

    public String name;

    public String reference;

    public String icon;

    public String vicinity;

    public Geometry geometry;

    public String formatted_address;

    public String formatted_phone_number;

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }

    public static class Geometry implements Serializable
    {

        public Location location;
    }

    public static class Location implements Serializable
    {

        public double lat;

        public double lng;
    }

}
