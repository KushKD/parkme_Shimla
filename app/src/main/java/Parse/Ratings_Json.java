package Parse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by kuush on 6/21/2016.
 */
public class Ratings_Json {

    public static String Rating_Parse(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("getRating_JSONResult");
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
