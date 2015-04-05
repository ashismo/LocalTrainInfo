package com.app.ashish.localtraininfo.services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import com.app.ashish.localtraininfo.bean.WebServiceCallType;
import com.app.ashish.localtraininfo.util.DataShareSingleton;
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
import java.util.regex.Pattern;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class RailInfoServices implements RailInfoInterface {
    private DataShareSingleton commonData = DataShareSingleton.getInstance();
    private Context context;

    public RailInfoServices(Context context) {
        this.context = context;
    }
    @Override
    public List<String> getStationListByName(String stnName) {

        List<String> matchedStnLst = new ArrayList<String>();
        if(commonData.getAllStnList() != null) {
            String regex = "(.*-|)" + stnName.toUpperCase() + ".*";
            Pattern p = Pattern.compile(regex);

            for (String stn : commonData.getAllStnList()) {
                if (p.matcher(stn).matches()) {
                    matchedStnLst.add(stn);
                }
            }
        }

        return matchedStnLst;
    }

    @Override
    public List<String> getTrainListByRoute(String fromStn, String toStn) {
        DataShareSingleton appData = DataShareSingleton.getInstance();
        String url = "http://erail.in/rail/getTrains.aspx?Station_From="+ fromStn +
                "&Station_To=" + toStn + "&DataSource=0&Language=0&Cache=true";
        appData.setUrl(url);
        appData.setWebServiceCallType(WebServiceCallType.TRAIN_SCHEDULE_CAL);
        new WebServiceCall(context).execute();
        return commonData.getTrainScheduleArray();
    }


}
