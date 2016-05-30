package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Rates extends Activity {

    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();
    String ID_Server = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        Bundle bundle = getIntent().getExtras();
        ID_Server = bundle.getString("ID");

        if (isOnline()) {
            try {
                Get_Rates_Data_Server GPD = new Get_Rates_Data_Server();
                GPD.execute(Econstants.URL_Rates);
            }catch(Exception e){
                Log.e("CAUGHT",e.getMessage().toString());
            }

        } else {
            Toast.makeText(this,"Unable to Connect to Internet. Please check your Network connection.", Toast.LENGTH_LONG).show();
        }



    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Rates.this.finish();

    }


    public class Get_Rates_Data_Server extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... params) {

            //http://192.168.0.171/HPParking/HPParking.svc/getParkingFeeParkingId_JSON/1  pass the Parking ID
            try {

                url_ = new URL(params[0]+ID_Server);
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setRequestMethod("GET");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(20000);
                conn_.setReadTimeout(20000);
                conn_.connect();

                int HttpResult =conn_.getResponseCode();
                if(HttpResult ==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.print(sb.toString());

                }else{
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(conn_!=null)
                    conn_.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
             //JSON Parsing Here
        }
    }

}
