package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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


        tv1.setText(RO.getParkingPlace());
        tv2.setText(RO.getIdentifier());


        return view;
    }
}

