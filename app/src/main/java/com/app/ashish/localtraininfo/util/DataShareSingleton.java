package com.app.ashish.localtraininfo.util;

import android.content.Context;
import android.widget.EditText;

import com.app.ashish.localtraininfo.bean.WebServiceCallType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class DataShareSingleton {
    private static DataShareSingleton dataShareObj;
    private EditText editText = null;
    private String url = "";
    private Map<String, String> allStationsMap = null;
    private WebServiceCallType webServiceCallType;
    private Context context;

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

    public Map<String, String> getAllStationsMap() {
        return allStationsMap;
    }

    public void setAllStationsMap(Map<String, String> allStationsMap) {
        this.allStationsMap = allStationsMap;
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
}
