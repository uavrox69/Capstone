package yslas.joseph.memoryjournal;

/**
 * Created by JYsla_000 on 3/26/2016.
 */
import java.io.Serializable;


/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class PlaceDetails implements Serializable {

    public String status;

    public PlaceLoc result;

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}
