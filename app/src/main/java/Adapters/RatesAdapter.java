package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import Model.Rates_Pojo;
import findparking.hp.dit.himachal.com.shimlaparking.R;

/**
 * Created by kuush on 5/30/2016.
 */
public class RatesAdapter extends ArrayAdapter<Rates_Pojo>  {

    private Context context;
    private List<Rates_Pojo> userlist;

    public RatesAdapter(Context context, int resource, List<Rates_Pojo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.userlist = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_rates_small, parent, false);
       // Rates_Pojo rates_object = rates.get(position);
        Rates_Pojo RO = userlist.get(position);
        TextView tv1 = (TextView)view.findViewById(R.id.feetype);
        TextView tv2 = (TextView)view.findViewById(R.id.feeamount);


        tv2.setText(RO.getDuration());

        if(RO.getFeeAmount().equalsIgnoreCase("0")){
            tv1.setText("NA");
        }else{
            tv1.setText(RO.getFeeAmount());
        }


        return view;
    }




}
