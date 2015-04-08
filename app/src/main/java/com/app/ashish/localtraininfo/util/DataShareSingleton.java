package com.app.ashish.localtraininfo.util;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import com.app.ashish.localtraininfo.bean.WebServiceCallType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class DataShareSingleton {
    private static DataShareSingleton dataShareObj;
    private EditText editText = null;
    private String url = "";
    private String allStationsDetails = null;
    private List<String> allStnList = null;
    private List<String> trainScheduleArray = null;
    private WebServiceCallType webServiceCallType;
    private Context context;
    private Activity activity;
    private String fromStation;
    private String toStation;
    private String hardCodedAllStations;

    private DataShareSingleton() {
    }

    public static DataShareSingleton getInstance() {
        if(dataShareObj == null) {
            dataShareObj = new DataShareSingleton();
        }
        return dataShareObj;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAllStationsDetails() {
        return allStationsDetails;
    }

    public void setAllStationsDetails(String allStationsDetails) {
        this.allStationsDetails = allStationsDetails;
    }

    public WebServiceCallType getWebServiceCallType() {
        return webServiceCallType;
    }

    public void setWebServiceCallType(WebServiceCallType webServiceCallType) {
        this.webServiceCallType = webServiceCallType;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<String> getAllStnList() {
        return allStnList;
    }

    public void setAllStnList(List<String> allStnList) {
        this.allStnList = allStnList;
    }

    public List<String> getTrainScheduleArray() {
        return trainScheduleArray;
    }

    public void setTrainScheduleArray(List<String> trainScheduleArray) {
        this.trainScheduleArray = trainScheduleArray;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getHardCodedAllStations() {
        return hardCodedAllStations;
    }

    public void setHardCodedAllStations(String hardCodedAllStations) {
        this.hardCodedAllStations = hardCodedAllStations;
    }
}
