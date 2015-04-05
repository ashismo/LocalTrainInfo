package com.app.ashish.localtraininfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
    List<String> scheduleList = null;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.train_schedule_list_view, null);
        TextView tv = (TextView)convertView.findViewById(R.id.trainScheduleDtls);

        String scheduleDtls =  scheduleList.get(position);
        String scheduleArr[] = scheduleDtls.split("/");
        String textToBeDisplayed = "";
        if(scheduleArr != null && scheduleArr.length >= 5) {
            boolean isTrainAvailableToday = (scheduleArr[4].equalsIgnoreCase("true")) ? true:false;
            String availableToday = (scheduleArr[4].equalsIgnoreCase("true")) ? "YES":"NO";
            textToBeDisplayed = "Train No:- " + scheduleArr[0] + " Dep:- " + scheduleArr[1] +
                    " Arr:- " + scheduleArr[2] + " Journey:- " + scheduleArr[3] + " Running Today? " + availableToday;
            tv.setText(textToBeDisplayed);

            if(isTrainAvailableToday) {
                convertView.findViewById(R.id.liveStatus).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.liveStatus).setVisibility(View.INVISIBLE);
            }
        }
        // Check if train is available today

        return convertView;
    }
}
