package com.app.ashish.localtraininfo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.ashish.localtraininfo.R;
import com.app.ashish.localtraininfo.adapters.TrainScheduleListAdapter;
import com.app.ashish.localtraininfo.bean.WebServiceCallType;
import com.app.ashish.localtraininfo.services.RailInfoInterface;
import com.app.ashish.localtraininfo.services.RailInfoServices;
import com.app.ashish.localtraininfo.util.DataShareSingleton;
import com.app.ashish.localtraininfo.util.DatabaseUtil;
import com.app.ashish.localtraininfo.util.RailInfoUtil;
import com.app.ashish.localtraininfo.util.WebServiceCall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class TrainScheduleActivity  extends ActionBarActivity {
    private EditText fromStnTxt, toStnTxt;
    private Button searchTrain;
    private ArrayAdapter<String> dataAdapter = null;
    private ListView listView = null;
    private DataShareSingleton commonData = DataShareSingleton.getInstance();

    @Override
    protected void onStart() {
        getAllStations();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Set the navigation up to the main page
        setContentView(R.layout.train_schedule);

        ((TextView)findViewById(R.id.scheduleHeader)).setVisibility(View.INVISIBLE);

        fromStnTxt = (EditText)findViewById(R.id.fromStn);
        toStnTxt = (EditText)findViewById(R.id.toStn);

        if(commonData.getFromStation() != null) {
            fromStnTxt.setText(commonData.getFromStation());
        }
        if(commonData.getToStation() != null) {
            toStnTxt.setText(commonData.getToStation());
        }

        fromStnTxt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count){
                // After typing three characters
                if(count >= 3) {
                    populateStationListInListView(s.toString(), fromStnTxt);
                } else {
                    listView = (ListView) findViewById(R.id.stnListView);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                commonData.setTrainScheduleArray(null);
            }

        });

        toStnTxt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count){
                // After typing three characters
                if(count >= 3) {
                    populateStationListInListView(s.toString(), toStnTxt);
                } else {
                    listView = (ListView) findViewById(R.id.stnListView);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        // Get data on button click
        searchTrain = (Button)findViewById(R.id.search);
        searchTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call webservice to get the train schedule
//                new RailInfoServices(getApplicationContext()).getTrainListByRoute(fromStnTxt.getText().toString(), toStnTxt.getText().toString());
                getTrainListByRoute(fromStnTxt.getText().toString(), toStnTxt.getText().toString());

            }

        });

        // Display history in the list view
        populateHistoryInListView();
    }

    private void getTrainListByRoute(String fromStn, String toStn) {
        ((TextView)findViewById(R.id.scheduleHeader)).setVisibility(View.INVISIBLE);
        DataShareSingleton appData = DataShareSingleton.getInstance();
        appData.setTrainScheduleArray(new ArrayList<String>()); // This will clear the table
        String url = "http://erail.in/rail/getTrains.aspx?Station_From="+ fromStn +
                "&Station_To=" + toStn + "&DataSource=0&Language=0&Cache=true";
        appData.setUrl(url);
        appData.setWebServiceCallType(WebServiceCallType.TRAIN_SCHEDULE_CAL);

        Intent i = new Intent(getApplicationContext(), SplashScreenActivity.class);
        appData.setToStation(toStnTxt.getText().toString());
        appData.setFromStation(fromStnTxt.getText().toString());
//        i.putExtra("from", fromStnTxt.getText().toString());
//        i.putExtra("to", toStnTxt.getText().toString());
        appData.setActivity(this);
        startActivity(i);

//        ListView scheduleView = (ListView)findViewById(R.id.scheduleView);
//        TrainScheduleListAdapter trainScheduleAdapter = new TrainScheduleListAdapter(getApplicationContext(), commonData.getTrainScheduleArray());
//        scheduleView.setAdapter(trainScheduleAdapter);


//        return commonData.getTrainScheduleArray();
    }

    /**
     * This method populates station names in the listview
     * @param station
     * @param currentText
     */
    private void populateStationListInListView(String station, final EditText currentText) {
        List<String> stationList = new RailInfoServices(getApplicationContext()).getStationListByName(station.toUpperCase());
        dataAdapter = new ArrayAdapter<String>(this, R.layout.station_list_layout,R.id.rowStnTextView, stationList);
        listView = (ListView) findViewById(R.id.stnListView);
        listView.setVisibility(View.VISIBLE);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  itemValue    = (String) listView.getItemAtPosition(position);
                itemValue= itemValue.substring(0, itemValue.indexOf("-"));
                currentText.setText(itemValue);
                listView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void populateHistoryInListView() {
        String[] historyData = new DatabaseUtil(getApplicationContext()).getTrainsFromHistoryTable();
        if(historyData != null) {
            dataAdapter = new ArrayAdapter<String>(this, R.layout.history_by_train_no_layout, R.id.historyDataByTrainNoTextView, historyData);
            final ListView listViewHistoryByTrainNo = (ListView) findViewById(R.id.historyByTrainNo);
            listViewHistoryByTrainNo.setVisibility(View.VISIBLE);
            // Assign adapter to ListView
            listViewHistoryByTrainNo.setAdapter(dataAdapter);
            listViewHistoryByTrainNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemValue = (String) listViewHistoryByTrainNo.getItemAtPosition(position);
                    String trainNo = itemValue.substring(1,itemValue.indexOf("]"));
                    String trainDtls = itemValue.substring(itemValue.indexOf("]")+1);

                    DataShareSingleton appData = DataShareSingleton.getInstance();
                    appData.setTrainScheduleArray(new ArrayList<String>()); // This will clear the table
//                    String url = "http://111.118.213.141/getIR.aspx?jsonp=1&Data=RUNSTATUSALL~" + trainNo;
                    String url = "http://m.runningstatus.in/status/" + trainNo;

                    // Insert data into history table
                    DatabaseUtil dbUtil = new DatabaseUtil(getApplicationContext());
                    dbUtil.updateTrainInHistoryTable(trainNo, trainDtls);

                    appData.setUrl(url);
                    appData.setWebServiceCallType(WebServiceCallType.LIVE_STATUS_CALL2);

                    Intent i = new Intent(TrainScheduleActivity.this, LiveStatusSpashActivity.class);
                    TrainScheduleActivity.this.startActivityForResult(i, 1);

//                    listView.setVisibility(View.INVISIBLE);
                }
            });
        }

    }

    private void getAllStations() {
        DataShareSingleton appData = DataShareSingleton.getInstance();
        if (appData.getAllStnList() == null || appData.getAllStnList().size() == 0) {
            String url = "http://111.118.213.140/js/cmp/stations.js";
            appData.setUrl(url);
            appData.setWebServiceCallType(WebServiceCallType.ALL_STATION_NAME_CALL);

            Intent i = new Intent(getApplicationContext(), StationNamesSplashScreenActivity.class);
            startActivity(i);
        }
    }
}
