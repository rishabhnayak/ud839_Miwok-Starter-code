package com.example.android.miwok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sahu on 5/3/2017.
 */

public class RescheduledTrainsAdaptor extends ArrayAdapter<RescheduledTrainClass>{


    public RescheduledTrainsAdaptor(RescheduledTrains context, ArrayList<RescheduledTrainClass> words) {
        super(context,0, words);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.rescheduled_trains_list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        RescheduledTrainClass currentAndroidFlavor = getItem(position);

        // Find the TextView in the Canceled_Trains_list_itemTrains_list_item.xml layout with the ID version_name
        TextView trainNo = (TextView) listItemView.findViewById(R.id.trainNo);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        trainNo.setText(currentAndroidFlavor.getTrainNo());

        // Find the TextView in the Canceled_Trains_list_itemTrains_list_item.xml layout with the ID version_number
        TextView trainName = (TextView) listItemView.findViewById(R.id.trainName);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        trainName.setText(currentAndroidFlavor.getTrainName());

        TextView trainSrc = (TextView) listItemView.findViewById(R.id.trainSrc);
        trainSrc.setText(currentAndroidFlavor.getTrainSrc());

        TextView startDate= (TextView) listItemView.findViewById(R.id.startDate);
        startDate.setText(currentAndroidFlavor.getStartDate());

        TextView schTime= (TextView) listItemView.findViewById(R.id.scsTime);
       schTime.setText(currentAndroidFlavor.getSchTime());

        TextView reschTime= (TextView) listItemView.findViewById(R.id.resTime);
        reschTime.setText(currentAndroidFlavor.getReschTime());

        TextView reschBy= (TextView) listItemView.findViewById(R.id.resBy);
        reschBy.setText(currentAndroidFlavor.getReschBy()+" min");

        TextView trainDstn= (TextView) listItemView.findViewById(R.id.trainDstn);
        trainDstn.setText(currentAndroidFlavor.getTrainDstn());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

}
