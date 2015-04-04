package com.app.ashish.localtraininfo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.ashish.localtraininfo.R;
import com.app.ashish.localtraininfo.bean.WebServiceCallType;
import com.app.ashish.localtraininfo.services.RailInfoInterface;
import com.app.ashish.localtraininfo.services.RailInfoServices;
import com.app.ashish.localtraininfo.util.DataShareSingleton;
import com.app.ashish.localtraininfo.util.DatabaseUtil;
import com.app.ashish.localtraininfo.util.RailInfoUtil;

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
        DataShareSingleton appData = DataShareSingleton.getInstance();
        String url = "http://111.118.213.140/js/cmp/stations.js";
        appData.setUrl(url);
        appData.setEditText(editText);
        appData.setWebServiceCallType(WebServiceCallType.ALL_STATION_NAME_CALL);
        railInfo = new RailInfoServices();
        railInfo.getStationListByName("JOX");


        // Get data on button click
        Button searchTrain = (Button)findViewById(R.id.search);
        searchTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fromStn = (EditText)findViewById(R.id.fromStn);
                EditText toStn = (EditText)findViewById(R.id.toStn);
toStn.setText(RailInfoUtil.getStationByCodeOrName(fromStn.getText().toString().toUpperCase()).get(0));
            }
        });
    }
}
