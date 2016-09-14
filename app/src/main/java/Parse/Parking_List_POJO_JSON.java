package Parse;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Model.Parking_List_Pojo;
import Model.Sending_Object_All_details_Pojo;

/**
 * Created by kuush on 9/14/2016.
 */
public class Parking_List_POJO_JSON {
    public static List<Parking_List_Pojo> parseFeed(String content) {

        try {

            String g_Table = null;
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                //  Log.d("Json ", "Object");
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getParking_JSONResult");
                Log.d("Table===",g_Table);
            }
            //you have an object
            else if (json instanceof JSONArray){
            }

            JSONArray ar = new JSONArray(g_Table);
            List<Parking_List_Pojo>userList = new ArrayList<>();
            

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Parking_List_Pojo parking = new Parking_List_Pojo();
                parking.setParkingPlace( obj.getString("ParkingPlace"));
                parking.setParkingArea(obj.getString("ParkingArea"));
                parking.setRemarks(obj.getString("Remarks"));
                parking.setSutedFor(obj.getString("SutedFor"));
                parking.setThrashholdValue( obj.getString("ThrashholdValue"));
                parking.setMinimumParkingFeeSmallCar( obj.getString("MinimumParkingFeeSmallCar"));
                parking.setMinimumParkingFeebigCar( obj.getString("MinimumParkingFeebigCar"));
                parking.setMinimumParkingTime( obj.getString("MinimumParkingTime"));
                parking.setCapacity(obj.getString("Capacity"));
                parking.setContactNumber1(obj.getString("ContactNumber1"));
                parking.setContactNumber2(obj.getString("ContactNumber2"));
                parking.setContactNumber3(obj.getString("ContactNumber3"));
                parking.setContactPerson1(obj.getString("ContactPerson1"));
                parking.setContactPerson2(obj.getString("ContactPerson2"));
                parking.setContactPerson3(obj.getString("ContactPerson3"));
                parking.setIdentifier( obj.getString("Identifier"));
                parking.setImage( obj.getString("Image"));
                parking.setImage1( obj.getString("Image1"));
                parking.setImage2( obj.getString("Image2"));
                parking.setLatitude(  obj.getDouble("Latitude"));
                parking.setLongitude(  obj.getDouble("Longitude"));
                parking.setParkingId(obj.getString("ParkingId"));
                parking.setPercentage(obj.getString("percentage"));
                parking.setAvailability(obj.getString("Availability"));
                parking.setParkingFullTag(obj.getString("ParkingFullTag").trim());
                parking.setRating( obj.optString("Rating"));
                userList.add(parking);
            }
            return userList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
