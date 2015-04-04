package com.app.ashish.localtraininfo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import com.app.ashish.localtraininfo.R;
import com.app.ashish.localtraininfo.services.RailInfoInterface;
import com.app.ashish.localtraininfo.services.RailInfoServices;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class TrainScheduleActivity  extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Set the navigation up to the main page
        setContentView(R.layout.train_schedule);

        RailInfoInterface railInfo = null;
        EditText editText = (EditText)findViewById(R.id.serviceData);
        railInfo = new RailInfoServices(editText);
        railInfo.getStationListByName("JOX");
    }
}
