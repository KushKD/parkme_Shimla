package HTTP;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by kuush on 6/9/2016.
 */
public class HttpManager {

    public static String get_Data(String uri) {

        BufferedReader reader = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

    public String GetData(String url) {
        System.out.print(url);
        BufferedReader reader = null;

        try {
            URL url_ = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url_.openConnection();

            if (con.getResponseCode() != 200) {
                throw new IOException(con.getResponseMessage());
            }


            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            con.disconnect();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Timeout";
        } finally {
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error";
                }
            }
        }
    }

    public String PostData_Rating(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;


        String PhoneNumber = null;
        String Parking_Id = null;
        String Rating = null;
        String Comments = null;
        String Result_to_Show = null;
        String URL = null;

        try {

            URL = params[1];
            PhoneNumber = params[2];
            Parking_Id = params[3];
            Rating = params[4];
            Comments = params[5];
           // OUT_Time = params[6];


            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object().key("getRating")
                    .object()
                    .key("RegId").value(PhoneNumber)
                    .key("ParkingId").value(Parking_Id)
                    .key("Stars").value(Rating)
                    .key("Remarks").value(Comments)
                    .endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
            //  Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    public String PostData_Park_Me(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;


         String EstimatedTime = null;
         String ParkingId = null;
         String PhoneNumber = null;
         String VehicleNo = null;
         String VehicleType = null;

        String URL = null;

        try {

            URL = params[1];
            EstimatedTime = params[2];
            ParkingId = params[3];
            PhoneNumber = params[4];
            VehicleNo = params[5];
            VehicleType = params[6];


            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object().key("ParkMeRequst")
                    .object()
                    .key("EstimatedTime").value(EstimatedTime)
                    .key("InTime").value(DateTime.GetDateAndTime())
                    .key("ParkingId").value(ParkingId)
                    .key("PhoneNumber").value(PhoneNumber)
                    .key("RegisterId").value("0")
                    .key("RequestStatus").value("Pending")
                    .key("RequestTime").value(DateTime.GetDateAndTime())
                    .key("VehicleNo").value(VehicleNo)
                    .key("VehicleType").value(VehicleType)
                    .endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
              Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }


    public String PostData_Park_OUT(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;


        String EstimatedTime = null;
        String ParkingId = null;
        String PhoneNumber = null;
        String VehicleNo = null;
        String VehicleType = null;

        String URL = null;

        try {

            URL = params[1];
            EstimatedTime = params[2];
            ParkingId = params[3];
            PhoneNumber = params[4];
            VehicleNo = params[5];
            VehicleType = params[6];


            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object().key("ParkOutRequst")
                    .object()
                    .key("EstimatedTime").value(EstimatedTime)
                    .key("InTime").value(DateTime.GetDateAndTime())
                    .key("ParkingId").value(ParkingId)
                    .key("PhoneNumber").value(PhoneNumber)
                    .key("RegisterId").value("0")
                    .key("RequestStatus").value("Pending")
                    .key("RequestTime").value(DateTime.GetDateAndTime())
                    .key("VehicleNo").value(VehicleNo)
                    .key("VehicleType").value(VehicleType)
                    .endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

}
