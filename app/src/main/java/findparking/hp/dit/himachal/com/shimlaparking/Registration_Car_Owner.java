package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration_Car_Owner extends Activity {

    Button register, back;
    EditText et_Mobile , et_Name , et_Vehicle_Number ,et_Email;
   // Helper date_Time = null;
      private String IMEI = null;

    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();

    LinearLayout regdetails_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__car__owner);


        register = (Button) findViewById(R.id.register);
        back = (Button) findViewById(R.id.back);
        et_Name = (EditText) findViewById(R.id.etname);
        et_Mobile = (EditText) findViewById(R.id.etmobile);
        et_Email = (EditText) findViewById(R.id.etemail);
        et_Vehicle_Number = (EditText)findViewById(R.id.etvehiclenumber);
        regdetails_ll = (LinearLayout)findViewById(R.id.rightLayout);

        regdetails_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });



       // TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
       // IMEI = telephonyManager.getDeviceId();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration_Car_Owner.this.finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Check weather Phone number is there or not
                String PhoneNumber_Service = et_Mobile.getText().toString().trim();
                String Name_Service = et_Name.getText().toString().trim();
                String Email_Service = et_Email.getText().toString().trim();
                String Vehicle_Number = et_Vehicle_Number.getText().toString().trim();

              /*  if(emailValidator(Email_Service)){
                    Toast.makeText(getApplicationContext(),"Email Address Valid.",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Please verify your email address.",Toast.LENGTH_LONG).show();
                }*/




if(Name_Service.length()!= 0 && Name_Service!= null){
    if (PhoneNumber_Service.length() == 10 && Integer.parseInt(PhoneNumber_Service.substring(0,1)) > 6) {

        if(Vehicle_Number.length()!=0 && Vehicle_Number!=null){
            if(isOnline()) {
                Registration Register_me = new Registration();
                Register_me.execute(Name_Service, PhoneNumber_Service, Vehicle_Number, "000000000000", Email_Service);
            }else{
                Toast.makeText(getApplicationContext(), "Please connect to Internet. ", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please enter vehicle number", Toast.LENGTH_SHORT).show();
        }



    } else {
        Toast.makeText(getApplicationContext(), "Please enter a valid 10 digit Mobile number", Toast.LENGTH_SHORT).show();
    }

}else{
    Toast.makeText(getApplicationContext(), "Please enter your Name.", Toast.LENGTH_SHORT).show();
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

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    class Registration extends AsyncTask<String, String,String> {

        private String Phone_Service = null;
        private String IMEI_Service = null;
        private String Name_Service = null;
        private String EMAIL_Service = null;
        private String Vehicle_Number_Service = null;
        private String Server_Value = null;
        private ProgressDialog dialog;
        String url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Registration_Car_Owner.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            Name_Service = params[0];
            Phone_Service = params[1];
            Vehicle_Number_Service = params[2];
            IMEI_Service = params[3];
            EMAIL_Service = params[4];

            try {
                url_ =new URL(Econstants.URL_MAIN+"/getVehicleRegistration_JSON");
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                JSONStringer userJson = new JSONStringer()
                        .object().key("VehicleDetails")
                        .object()
                        .key("Name").value(params[0])
                        .key("MobileNumber").value(params[1])
                        .key("vehicelNumber").value(params[2])
                        .key("IMI").value(params[3])
                        .key("EMail").value(params[4])
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
            String finalResult = null;
            JsonParser JP;
            if(s.equalsIgnoreCase("Server Connection failed.")){

                finalResult = s;
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
            }else{
                JP = new JsonParser();
                finalResult = JP.POST(s);
                if(finalResult.equals("Registation completed")){
                    // clearData();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
                    // User has successfully logged in, save this information
                    //  We need an Editor object to make preference changes.
                    SharedPreferences settings = getSharedPreferences(Econstants.PREFRANCE_NAME, 0); // 0 - for private mode
                    SharedPreferences.Editor editor = settings.edit();
                    //Set "hasLoggedIn" to true
                    editor.putBoolean("hasLoggedIn", true);
                    editor.putString("Name",Name_Service);
                    editor.putString("phonenumber",Phone_Service);
                    editor.putString("VehicleNumber",Vehicle_Number_Service);
                    // Commit the edits!
                    editor.commit();
                    Intent i = new Intent(Registration_Car_Owner.this,MainMapsActivity.class);
                    startActivity(i);
                    Registration_Car_Owner.this.finish();
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
                }
            }








        }
    }
}
