package Generic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Abstract.AsyncTaskListener;
import Parse.Manager_Json;
import Parse.Ratings_Json;
import Utilities.AppStatus;
import Utilities.Econstants;
import findparking.hp.dit.himachal.com.shimlaparking.R;
import Enum.TaskType;

/**
 * Created by kuush on 6/16/2016.
 */
public class Custom_Dialog implements AsyncTaskListener {


String rating = "1.0";



        public void showDialog(final Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_custom);

            TextView text = (TextView) dialog.findViewById(R.id.dialog_result);
            text.setText(msg);

            Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);

            dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // activity.finish();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }




    public void show_Rating(final Activity activity, String msg , final String Phonenumber, final String Parking_ID){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_rateme);



        TextView P_NO = (TextView) dialog.findViewById(R.id.phonenumber);
        final RatingBar ratingbar_rateme = (RatingBar) dialog.findViewById(R.id.dialog_ratingbar);

        final EditText et_comments = (EditText)dialog.findViewById(R.id.comments);

        ratingbar_rateme.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
                // TODO Auto-generated method stub
               rating= Float.toString(rateValue);
                Log.d("Rating", "your selected value is :"+rateValue);
            }
        });

        P_NO.setText(Phonenumber);

        Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);
        Button dialog_exit = (Button)dialog.findViewById(R.id.dialog_exit);



        dialog_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("ParkingID", Parking_ID);
                Log.e("Phone", Phonenumber);
                Log.e("Rating", rating);
                Log.e("Comments", et_comments.getText().toString());
                dialog.dismiss();

                if (AppStatus.getInstance(activity).isOnline()) {
                    String URL = Econstants.URL_MAIN + "/getRating_JSON";
                    Log.e("URL",URL);
                    new Generic_Async_Post(activity, Custom_Dialog.this, TaskType.USER_RATING).execute("getRating_JSON", URL, Phonenumber,Parking_ID,rating,et_comments.getText().toString().trim());
                } else

                {
                    Toast.makeText(activity, "Please connect to Internet.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dialog.show();

        }



    public void Show_Park_ME(final Activity activity, String msg , final String Phonenumber, final String Vehicle_Number, final String ParkingId_Server) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_parkme);
        dialog.setTitle("Park Me");
        dialog.setCancelable(false);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);




        Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);
        Button exit = (Button)dialog.findViewById(R.id.dialog_exit);
        final  EditText carnumber_tv = (EditText)dialog.findViewById(R.id.carnumber);
        carnumber_tv.setText(Vehicle_Number);
        final EditText phonenumber_tv  = (EditText)dialog.findViewById(R.id.phonenumber);
        phonenumber_tv.setText(Phonenumber);
        final Spinner typecar_sp = (Spinner)dialog.findViewById(R.id.typecar);
        final Spinner estimatedtime_sp = (Spinner)dialog.findViewById(R.id.estimatedtime);

        final String WSTS = Long.toString( estimatedtime_sp.getSelectedItemId());
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppStatus.getInstance(activity).isOnline()) {
                    String EstimatedTime_Server = null;
                    EstimatedTime_Server  = Long.toString(estimatedtime_sp.getSelectedItemId());

                    String VehicleType_Server = typecar_sp.getSelectedItem().toString().trim();
                    Log.e("EstimatedTime_Server",WSTS);
                    Log.e("VehicleType_Server",VehicleType_Server);
                    Log.e("ParkingId_Server",ParkingId_Server);
                    Log.e("Phone no",phonenumber_tv.getText().toString());
                    Log.e("Car Number",carnumber_tv.getText().toString());

                    dialog.dismiss();

                    String URL = Econstants.URL_MAIN + "/getParkMeRequest_JSON";
                    Log.e("URL",URL);
                    new Generic_Async_Post(activity, Custom_Dialog.this, TaskType.PARK_USER).execute("getParkMeRequest_JSON", URL, EstimatedTime_Server,ParkingId_Server,phonenumber_tv.getText().toString().trim(),carnumber_tv.getText().toString().trim(),VehicleType_Server);
                }else{
                    Toast.makeText(activity, "Network not found" ,Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    //PARK_OUT_USER
    public void Show_Park_OUT(final Activity activity, String msg , final String Phonenumber, final String Vehicle_Number, final String ParkingId_Server) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_parkme);
        dialog.setTitle("Park Me");
        dialog.setCancelable(false);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);




        Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);
        Button exit = (Button)dialog.findViewById(R.id.dialog_exit);
        final  EditText carnumber_tv = (EditText)dialog.findViewById(R.id.carnumber);
        carnumber_tv.setText(Vehicle_Number);
        final EditText phonenumber_tv  = (EditText)dialog.findViewById(R.id.phonenumber);
        phonenumber_tv.setText(Phonenumber);
        final Spinner typecar_sp = (Spinner)dialog.findViewById(R.id.typecar);
        final Spinner estimatedtime_sp = (Spinner)dialog.findViewById(R.id.estimatedtime);

        final String WSTS = Long.toString( estimatedtime_sp.getSelectedItemId());
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppStatus.getInstance(activity).isOnline()) {
                    String EstimatedTime_Server = null;
                    EstimatedTime_Server  = Long.toString(estimatedtime_sp.getSelectedItemId());

                    String VehicleType_Server = typecar_sp.getSelectedItem().toString().trim();
                    Log.e("EstimatedTime_Server",WSTS);
                    Log.e("VehicleType_Server",VehicleType_Server);
                    Log.e("ParkingId_Server",ParkingId_Server);
                    Log.e("Phone no",phonenumber_tv.getText().toString());
                    Log.e("Car Number",carnumber_tv.getText().toString());

                    dialog.dismiss();

                    String URL = Econstants.URL_MAIN + "/getParkOutRequest_JSON";
                    Log.e("URL",URL);
                    new Generic_Async_Post(activity, Custom_Dialog.this, TaskType.PARK_OUT_USER).execute("getParkOutRequest_JSON", URL, EstimatedTime_Server,ParkingId_Server,phonenumber_tv.getText().toString().trim(),carnumber_tv.getText().toString().trim(),VehicleType_Server);
                }else{
                    Toast.makeText(activity, "Network not found" ,Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }



    @Override
    public void onTaskCompleted(String result, TaskType taskType) {



    }

    @Override
    public void onTaskCompleted(Activity activity, String result, TaskType taskType) {

        if(taskType.equals(TaskType.USER_RATING)){
            String Result_to_Show = null;
            Result_to_Show = Ratings_Json.Rating_Parse(result);
            showDialog(activity,Result_to_Show);

        }else if(taskType.equals(TaskType.PARK_USER)){
            String Result_to_Show = null;
            Result_to_Show = Manager_Json.Parse_PArkME(result);
            showDialog(activity,Result_to_Show);
        }else if(taskType.equals(TaskType.PARK_OUT_USER)){
            String Result_to_Show = null;
            Result_to_Show = Manager_Json.Parse_ParkOut(result);
            showDialog(activity,Result_to_Show);
        }
        else{
            showDialog(activity,"Something went wrong.");
        }
    }


}
