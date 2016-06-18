package Parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import Model.Rates_Pojo;

/**
 * Created by kuush on 5/30/2016.
 */
public class Fee_Parse_Json {

    public static List<Rates_Pojo> parseFeed(String content) {

        try {

            String g_Table = null;
            Object json = new JSONTokener(content).nextValue();
            if (json instanceof JSONObject){
                JSONObject obj = new JSONObject(content);
                g_Table = obj.optString("getParkingFee_JSONResult");
            }
            //you have an object
            else if (json instanceof JSONArray){
            }

            JSONArray ar = new JSONArray(g_Table);
            List<Rates_Pojo>AdmiCardList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Rates_Pojo pojo_rates = new Rates_Pojo();
                pojo_rates.setFeeAmount(obj.getString("Amount"));
                System.out.println(obj.getString("Amount"));
                pojo_rates.setDuration(obj.getString("Duration"));
                System.out.println(obj.getString("Duration"));

                AdmiCardList.add(pojo_rates);
            }
            return AdmiCardList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
