package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import Abstract.AsyncTaskListener;
import Adapters.Parking_List_Adapter;
import Generic.Custom_Dialog;
import Generic.Generic_Async_Get;
import Generic.Generic_Async_Post;
import Model.Parking_List_Pojo;
import Parse.Parking_List_POJO_JSON;
import Utilities.AppStatus;
import Utilities.Econstants;
import enums.TaskType;

public class List_Parking extends Activity implements  AsyncTaskListener{

    Custom_Dialog custom_dialog  = new Custom_Dialog();
    List<Parking_List_Pojo> Parking_list_Server;   // change the list
    ListView listv;
    Button bt_map_back;

    Double Latitude , Longitude = null;

    Parking_List_Adapter adapter;  // change the adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__parking);

      Intent getRoomDetailsIntent = getIntent();
        Latitude =  Double.parseDouble(getRoomDetailsIntent.getStringExtra("Latitude"));
        Longitude =   Double.parseDouble(getRoomDetailsIntent.getStringExtra("Longitude"));
       // Log.e("Latitude is:- ", Double.toString(Latitude));
       // Log.e("Longitude is:- ", Double.toString(Longitude) );

        listv = (ListView) findViewById(R.id.list);

        bt_map_back = (Button)findViewById(R.id.map_back);

        bt_map_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List_Parking.this.finish();
            }
        });

        if(AppStatus.getInstance(List_Parking.this).isOnline()){


            new Generic_Async_Get(List_Parking.this, List_Parking.this, TaskType.LIST_LOCATIONS).execute(Econstants.URL_GENERIC);


        }else{
            custom_dialog.showDialog(List_Parking.this,"Unable to connect to Server. Please check your Network connection.");
        }


        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Parking_List_Pojo Parking_Details = (Parking_List_Pojo) parent.getItemAtPosition(position);   //change object
                Intent userSearch = new Intent();
                userSearch.putExtra("INBOX", Parking_Details);
                Parking_Details.setLatitude_my_Location(Latitude);
                Parking_Details.setLongitude_my_Location(Longitude);
                userSearch.setClass(List_Parking.this, ParkingDetailsList_Activity.class);
                startActivity(userSearch);
               // List_Parking.this.finish();


            }
        });

    }


    @Override
    public void onTaskCompleted(String result, enums.TaskType taskType) {

        if(taskType == TaskType.LIST_LOCATIONS){
            Log.e("List IS:", result);
            //Parse Result
            Parking_list_Server = Parking_List_POJO_JSON.parseFeed(result);
            if(Parking_list_Server.isEmpty()){
                Toast.makeText(getApplicationContext(),"List Empty",Toast.LENGTH_LONG).show();
            }else
            {
                updateDisplay();
            }



        }
    }

    protected void updateDisplay() {

       // LGone.setVisibility(View.VISIBLE);   //Adapter needs to be changed
        adapter = new Parking_List_Adapter(this, R.layout.item_inbox, Parking_list_Server);
        listv.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
        // listv.setTextFilterEnabled(true);

    }

    @Override
    public void onTaskCompleted(Activity activity, String result, enums.TaskType taskType) {

    }
}
