package Parse;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kuush on 6/9/2016.
 */
public class Parse_Google_API_Json {

    public static String[] parseGoogleJSON(String content) {

        String[] values = new String[2];
        try {

            String g_Table = null;
          /*  Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);


                g_Table = obj.optString("legs");
            }
            //you have an object
            else if (json instanceof JSONArray){
                Log.e("We are","Here");
            }*/
            JSONObject jsonObject = new JSONObject(content);

// routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");
// Grab the first route
            JSONObject route = routesArray.getJSONObject(0);
// Take all legs from the route
            JSONArray legs = route.getJSONArray("legs");
// Grab first leg
            JSONObject leg = legs.getJSONObject(0);

            JSONObject durationObject = leg.getJSONObject("duration");
            JSONObject distanceObject = leg.getJSONObject("distance");
            String duration = durationObject.getString("text");
            String Distance = distanceObject.getString("text");

            Log.e("Duration",duration);
            Log.e("Distance",Distance);

            values[0] = Distance;
            values[1] = duration;


            return values;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
