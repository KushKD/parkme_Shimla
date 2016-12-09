package Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.List;

import Model.Parking_List_Pojo;
import findparking.hp.dit.himachal.com.shimlaparking.R;

/**
 * Created by kuush on 9/14/2016.
 */
public class Parking_List_Adapter extends ArrayAdapter<Parking_List_Pojo> {

    private Context context;
    private List<Parking_List_Pojo> userlist;

    public Parking_List_Adapter(Context context, int resource, List<Parking_List_Pojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.userlist = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_inbox, parent, false);
        // Rates_Pojo rates_object = rates.get(position);
        Parking_List_Pojo RO = userlist.get(position);
        TextView tv1 = (TextView) view.findViewById(R.id.itemone);
        TextView tv2 = (TextView) view.findViewById(R.id.itemtwo);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.imageView1);

        if (RO.getAvailability().equalsIgnoreCase("Not Known")) {
           // tv1.setTextColor(Color.YELLOW);
           // tv2.setTextColor(Color.YELLOW);
            imageView1.setImageResource(R.mipmap.yellow);
        } else {
            //Toast.makeText(getApplicationContext(),myMarker.getParkingFullTag().toString(),Toast.LENGTH_LONG).show();
            // available.setText(myMarker.getAvailability()+"("+myMarker.getPercentage()+"%)");  //
            // parking_availability.setTextColor(Color.parseColor("#ffa500")); //orange
            if (RO.getPercentage().equalsIgnoreCase("0")) {
                //tv1.setTextColor(Color.RED);
               // tv2.setTextColor(Color.RED);
                imageView1.setImageResource(R.mipmap.red);
            } else {
               // tv1.setTextColor(Color.GREEN);
               // tv2.setTextColor(Color.GREEN);
                imageView1.setImageResource(R.mipmap.green);
            }
        }
            tv1.setText(RO.getParkingPlace());
            tv2.setText(RO.getIdentifier());


            return view;
        }

}

