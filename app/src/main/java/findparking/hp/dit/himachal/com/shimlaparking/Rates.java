package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Rates extends Activity {

    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb ;
    String ID_Server = null;
    ProgressBar pb;
    ListView listv;
    Context context;
    List<Get_Rates_Data_Server> tasks;
    List<Rates_POJO> Rates_Server;
    RatesAdapter adapter;
    TextView header;
    Button back;

    Button smallcars , bigcars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        Bundle bundle = getIntent().getExtras();
        ID_Server = bundle.getString("ID");

        listv = (ListView) findViewById(R.id.list);
        smallcars = (Button)findViewById(R.id.smallcars);
        bigcars = (Button)findViewById(R.id.bigcars);
        header = (TextView) findViewById(R.id.header);
        back = (Button)findViewById(R.id.back);

        context = this;
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();

        if (isOnline()) {
            try {
                Get_Rates_Data_Server GPD = new Get_Rates_Data_Server();
                GPD.execute(Econstants.URL_Rates_Small , "Small Car");
            }catch(Exception e){
                Log.e("CAUGHT",e.getMessage().toString());
            }

        } else {
            Toast.makeText(this,"Unable to Connect to Internet. Please check your Network connection.", Toast.LENGTH_LONG).show();
        }


        smallcars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isOnline()) {
                    try {
                            Rates_Server = null;
                        Get_Rates_Data_Server GPD = new Get_Rates_Data_Server();
                        GPD.execute(Econstants.URL_Rates_Small , "Small Car");
                        header.setText("Small Car");
                    }catch(Exception e){
                        Log.e("CAUGHT",e.getMessage().toString());
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Unable to Connect to Internet. Please check your Network connection.", Toast.LENGTH_LONG).show();
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rates.this.finish();
            }
        });


        bigcars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                header.setText("Big Car");
                if (isOnline()) {
                    try {
                        Rates_Server = null;
                        Get_Rates_Data_Server GPD = new Get_Rates_Data_Server();
                        GPD.execute(Econstants.URL_Rates_Big , "Big Car");
                    }catch(Exception e){
                        Log.e("CAUGHT",e.getMessage().toString());
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Unable to Connect to Internet. Please check your Network connection.", Toast.LENGTH_LONG).show();
                }


            }
        });


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

    protected void updateDisplay() {



          if(Rates_Server.size()>0){
             // adapter.clear();
              adapter = new RatesAdapter(this, R.layout.item_rates_small, Rates_Server);
              //adapter.notifyDataSetChanged();
             // listv.invalidateViews();
              listv.setAdapter(adapter);


          }else{
              adapter = new RatesAdapter(this, R.layout.item_rates_small, Rates_Server);
              listv.setAdapter(adapter);
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
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);

        }


        @Override
        protected String doInBackground(String... params) {

            String ServiceType = null;
            url_ = null;
            //http://192.168.0.171/HPParking/HPParking.svc/getParkingFeeParkingId_JSON/1  pass the Parking ID
            try {


                ServiceType = params[1];

                  if(ServiceType.equalsIgnoreCase("Small Car")){
                      url_ = new URL(params[0]+ID_Server+"/1");
                      System.out.print(url_);

                  }
                else{
                    url_ = new URL(params[0]+ID_Server+"/2");
                      System.out.print(url_);
                }



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
                    sb = null;
                    while ((line = br.readLine()) != null) {
                        sb = new StringBuilder();
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
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
             //JSON Parsing Here

            Rates_Server = Fee_JSON_Parse_Small.parseFeed(s);
            if(Rates_Server.isEmpty()){
                Toast.makeText(getApplicationContext(),"List Empty",Toast.LENGTH_LONG).show();
            }else
            {
                updateDisplay();
            }
            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
        }
    }

}
