package com.app.ashish.localtraininfo.services;

import android.os.AsyncTask;
import android.widget.EditText;

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

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class RailInfoServices implements RailInfoInterface {
    private String url = "";
    private EditText editText = null;

    public RailInfoServices(EditText editTest) {
        this.editText = editText;
    }
    @Override
    public String getStationListByName(String stnName) {
        url = "http://erail.in/rail/getTrains.aspx?Station_From=JOX&Station_To=HWH&DataSource=0&Language=0&Cache=true";
        new WebServiceCall(url,editText).execute();
        return null;
    }


}
