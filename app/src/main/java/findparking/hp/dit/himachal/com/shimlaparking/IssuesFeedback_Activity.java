package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Parse.JsonParser;
import Utilities.Econstants;

public class IssuesFeedback_Activity extends Activity {

   public String Parking_ID = null;
    public String Latitude = null;
    public String Longitude = null;
    String[] issue_type,Complaint_Type;
    Button cancel , submit;
    LinearLayout complainttype_lay;
    Spinner IssueType,Complaint;
    EditText desc;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues__feedback);

        Bundle bundle = getIntent().getExtras();
        Parking_ID = bundle.getString("ID");
        Latitude = bundle.getString("LATITUDE");
        Longitude = bundle.getString("LONGITUDE");

       // Toast.makeText(getApplicationContext(),Parking_ID,Toast.LENGTH_LONG).show();

        issue_type = getResources().getStringArray(R.array.Issue_Type);
        Complaint_Type = getResources().getStringArray(R.array.Complaint_Type);

        cancel = (Button)findViewById(R.id.cancel);
        submit = (Button)findViewById(R.id.submit);
        complainttype_lay = (LinearLayout)findViewById(R.id.complainttype_lay);
        desc = (EditText)findViewById(R.id.desc);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IssuesFeedback_Activity.this.finish();
            }
        });

         IssueType = (Spinner) findViewById(R.id.testspinner);
        Complaint = (Spinner) findViewById(R.id.complainttype);

        IssueType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                String Check = IssueType.getSelectedItem().toString().trim();

                if(Check.equalsIgnoreCase("Complaint")){
                    complainttype_lay.setVisibility(View.VISIBLE);
                }else{
                    complainttype_lay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              String Issue_Type_server =   IssueType.getSelectedItem().toString().trim();
                String Complaint_Type_server = null;





                if(desc.getText().toString().length()>50){
                    if(IssueType.getSelectedItem().toString().trim().equalsIgnoreCase("Complaint")){
                        Complaint_Type_server =   Complaint.getSelectedItem().toString().trim();
                        // Toast.makeText(getApplicationContext(),Issue_Type_server + Complaint_Type_server,Toast.LENGTH_LONG).show();
                        SharedPreferences prfs = getSharedPreferences(Econstants.PREFRANCE_NAME, Context.MODE_PRIVATE);
                        String name  = prfs.getString("Name","");
                        String Mobile = prfs.getString("phonenumber","");
                        System.out.println(name);
                        send_IssuesToServer SIS = new send_IssuesToServer();
                        SIS.execute(Issue_Type_server,Complaint_Type_server,desc.getText().toString(),Parking_ID,Latitude,Longitude,name,Mobile);
                    }else{
                        SharedPreferences prfs = getSharedPreferences(Econstants.PREFRANCE_NAME, Context.MODE_PRIVATE);
                        String name  = prfs.getString("Name","");
                        String Mobile = prfs.getString("phonenumber","");
                        System.out.println(name);
                        Complaint_Type_server ="";
                        if(isOnline()) {
                            send_IssuesToServer SIS = new send_IssuesToServer();
                            SIS.execute(Issue_Type_server, Complaint_Type_server, desc.getText().toString(), Parking_ID, Latitude, Longitude, name, Mobile);
                        }else {
                            Toast.makeText(getApplicationContext(),"Please connect to Internet.",Toast.LENGTH_LONG).show();
                        }
                            // Toast.makeText(getApplicationContext(),Issue_Type_server + Complaint_Type_server,Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Minimum 50 characters required.",Toast.LENGTH_LONG).show();
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

   private class send_IssuesToServer extends AsyncTask<String,String,String>{

       private String Issue_Type = null;
       private String Complaint_Type = null;
       private String Discription_Server = null;
       private String ParkingID = null;
       private String Longitude = null;
       private String Latitude = null;
       private String Name = null;
       private String Mobile = null;
       private ProgressDialog dialog;
       String url = null;


       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           dialog = new ProgressDialog(IssuesFeedback_Activity.this);
           this.dialog.setMessage("Please wait ..");
           this.dialog.show();
           this.dialog.setCancelable(false);
       }


       @Override
        protected String doInBackground(String... params) {
            Issue_Type = params[0];
            Complaint_Type = params[1];
            Discription_Server = params[2];
            ParkingID = params[3];
            Latitude = params[4];
            Longitude = params[5];
            Name = params[6];
            Mobile = params[7];

           try {
               url_ =new URL(Econstants.URL_MAIN+"/getIssueFeedback_JSON");
               conn_ = (HttpURLConnection)url_.openConnection();
               conn_.setDoOutput(true);
               conn_.setRequestMethod("POST");
               conn_.setUseCaches(false);
               conn_.setConnectTimeout(10000);
               conn_.setReadTimeout(10000);
               conn_.setRequestProperty("Content-Type", "application/json");
               conn_.connect();

               JSONStringer userJson = new JSONStringer()
                       .object().key("IssuesFeedback_Activity")
                       .object()
                       .key("NatureofComplaint").value(Issue_Type)
                       .key("TypeofComplaint").value(Complaint_Type)
                       .key("ParkingId").value(ParkingID)
                       .key("Anoanonymous").value("")
                       .key("Name").value(Name)
                       .key("Mobile").value(Mobile)
                       .key("Email").value("")
                       .key("IMEI").value("")
                       .key("Discription").value(Discription_Server)
                       .key("Latitude").value(Latitude)
                       .key("Longitude").value(Longitude)
                       .endObject()
                       .endObject();

               //   {"VehicleDetails":{"Name":"Kush","MobileNumber":"9459619235","vehicelNumber":"HP81A9898","IMI":"000000000000","EMail":"kush@gmail.com "}}

               System.out.println(userJson.toString());
               OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
               out.write(userJson.toString());
               out.close();

               try{
                   int HttpResult =conn_.getResponseCode();
                   if(HttpResult ==HttpURLConnection.HTTP_OK){
                       BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                       String line = null;
                       while ((line = br.readLine()) != null) {
                           sb.append(line + "\n");
                       }
                       br.close();
                       System.out.println(sb.toString());

                   }else{
                       System.out.println("Server Connection failed.");
                   }

               } catch(Exception e){
                   return "Server Connection failed.";
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

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           JsonParser JP;
           String finalResult = null;

           if(s.equalsIgnoreCase("Server Connection failed.")){
               dialog.dismiss();
               Toast.makeText(getApplicationContext(), "Server Connection failed.", Toast.LENGTH_SHORT).show();
           }else{
               JP = new JsonParser();
               finalResult = JP.POST_ISSUE(s);
               if(finalResult.length()>50){
                   // clearData();
                   dialog.dismiss();
                   Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
                   IssuesFeedback_Activity.this.finish();
               }
               else{
                   dialog.dismiss();
                   Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();

               }

           }




       }
   }
}
