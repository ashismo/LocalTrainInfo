package com.app.ashish.localtraininfo.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.app.ashish.localtraininfo.activity.LiveStatusSpashActivity;
import com.app.ashish.localtraininfo.activity.TrainScheduleActivity;
import com.app.ashish.localtraininfo.bean.WebServiceCallType;
import com.app.ashish.localtraininfo.services.RailInfoInterface;
import com.app.ashish.localtraininfo.services.RailInfoServices;
import com.app.ashish.localtraininfo.util.DataShareSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class TrainScheduleListAdapter extends BaseAdapter {
    private Context context= null;
    private List<String> scheduleList = null;
    private String trainNo = "0";
    private boolean isTrainFound = true;

    public TrainScheduleListAdapter(Context context, List<String> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }
    @Override
    public int getCount() {
        if(scheduleList != null && scheduleList.size() > 0) {
            return scheduleList.size();
        } else {
            isTrainFound = false;
        }
        return 1;
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

        String scheduleDtls =  (isTrainFound) ? scheduleList.get(position) : null;
        String scheduleArr[] = (scheduleDtls != null) ? scheduleDtls.split("/") : null;
        String textToBeDisplayed = "";
        if(isTrainFound && scheduleArr != null && scheduleArr.length >= 5) {
            boolean isTrainAvailableToday = (scheduleArr[5].equalsIgnoreCase("true")) ? true:false;
            String availableToday = (scheduleArr[5].equalsIgnoreCase("true")) ? "YES":"NO";
//            textToBeDisplayed = "Train No:- " + scheduleArr[1] + " Dep:- " + scheduleArr[0] +
//                    " Arr:- " + scheduleArr[2] + " Journey:- " + scheduleArr[3] + " Running Today? " + availableToday;

            textToBeDisplayed = scheduleArr[1] + "    " + scheduleArr[0] +
                    "  " + scheduleArr[3] + " " + scheduleArr[4] + "  " + availableToday;
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

                    DataShareSingleton appData = DataShareSingleton.getInstance();
                    appData.setTrainScheduleArray(new ArrayList<String>()); // This will clear the table
                    String url = "http://111.118.213.141/getIR.aspx?jsonp=1&Data=RUNSTATUSALL~" + scheduleArr[1];
                    appData.setUrl(url);
                    appData.setWebServiceCallType(WebServiceCallType.LIVE_STATUS_CALL);

                    Intent i = new Intent(appData.getActivity(), LiveStatusSpashActivity.class);
                    appData.getActivity().startActivity(i);
                }
            });

        }

        // Display message if no train available
        if(!isTrainFound) {
            Button liveStatusBtn = (Button)convertView.findViewById(R.id.liveStatus);
            liveStatusBtn.setVisibility(View.INVISIBLE);
            tv.setText("No Train found in this route");
        }
        // Check if train is available today

        return convertView;
    }
}
