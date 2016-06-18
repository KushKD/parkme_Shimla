package Parse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by kuush on 6/9/2016.
 */
public class Manager_Json {

    public static String Parse_PArkME(String content) {

        try {

            String g_Table = null;
            //  JSONObject sys  = reader.getJSONObject("sys");
            //  country = sys.getString("country");
            JSONObject json= (JSONObject) new JSONTokener(content).nextValue();
            g_Table = (String) json.get("getParkMeRequest_JSONResult");

            return g_Table;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String Parse_ParkOut(String content) {

        try {

            String g_Table = null;
            //  JSONObject sys  = reader.getJSONObject("sys");
            //  country = sys.getString("country");
            JSONObject json= (JSONObject) new JSONTokener(content).nextValue();
            g_Table = (String) json.get("getParkOutRequest_JSONResult");

            return g_Table;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
