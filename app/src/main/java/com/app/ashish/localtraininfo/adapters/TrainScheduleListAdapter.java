package com.app.ashish.localtraininfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.ashish.localtraininfo.R;
import com.app.ashish.localtraininfo.services.RailInfoInterface;
import com.app.ashish.localtraininfo.services.RailInfoServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class TrainScheduleListAdapter extends BaseAdapter {
    private Context context= null;
    private List<String> scheduleList = null;
    private String trainNo = "0";

    public TrainScheduleListAdapter(Context context, List<String> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }
    @Override
    public int getCount() {
        if(scheduleList != null) return scheduleList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.train_schedule_list_view, null);
        TextView tv = (TextView)convertView.findViewById(R.id.trainScheduleDtls);
        TextView trainName = (TextView)convertView.findViewById(R.id.trainName);

        String scheduleDtls =  scheduleList.get(position);
        String scheduleArr[] = scheduleDtls.split("/");
        String textToBeDisplayed = "";
        if(scheduleArr != null && scheduleArr.length >= 5) {
            boolean isTrainAvailableToday = (scheduleArr[5].equalsIgnoreCase("true")) ? true:false;
            String availableToday = (scheduleArr[5].equalsIgnoreCase("true")) ? "YES":"NO";
//            textToBeDisplayed = "Train No:- " + scheduleArr[1] + " Dep:- " + scheduleArr[0] +
//                    " Arr:- " + scheduleArr[2] + " Journey:- " + scheduleArr[3] + " Running Today? " + availableToday;

            textToBeDisplayed = scheduleArr[1] + "    " + scheduleArr[0] +
                    "  " + scheduleArr[3] + " " + scheduleArr[4] + "  " + availableToday;
            trainNo = scheduleArr[1];
            tv.setText(textToBeDisplayed);
            trainName.setText(scheduleArr[2]);
            final Button liveStatusBtn = (Button)convertView.findViewById(R.id.liveStatus);
            if(isTrainAvailableToday) {
                liveStatusBtn.setVisibility(View.VISIBLE);
            } else {
                liveStatusBtn.setVisibility(View.INVISIBLE);
            }

            liveStatusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String scheduleDtls = scheduleList.get(position);
                    String scheduleArr[] = scheduleDtls.split("/");
                    liveStatusBtn.setText(scheduleArr[1]);
                }
            });

        }
        // Check if train is available today

        return convertView;
    }
}
