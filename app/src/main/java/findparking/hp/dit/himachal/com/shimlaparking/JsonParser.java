package findparking.hp.dit.himachal.com.shimlaparking;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by kuush on 6/1/2016.
 */
public class JsonParser {

   /* public String ParseString(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString(Constants.JSON_LOGIN);
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/



    public String POST(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString("getVehicelRegister_JSONResult");
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



   /* public String ParseStringOTP(String s) {

        String g_Table = null;
        try {
            Object json = new JSONTokener(s).nextValue();
            if (json instanceof JSONObject) {
                JSONObject obj = new JSONObject(s);
                g_Table = obj.optString(Constants.JSON_OTP);
                return g_Table;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
