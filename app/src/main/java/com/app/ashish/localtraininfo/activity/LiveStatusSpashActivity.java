package com.app.ashish.localtraininfo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.ashish.localtraininfo.R;
import com.app.ashish.localtraininfo.adapters.TrainScheduleListAdapter;
import com.app.ashish.localtraininfo.bean.WebServiceCallType;
import com.app.ashish.localtraininfo.util.DataShareSingleton;
import com.app.ashish.localtraininfo.util.RailInfoUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ashis_000 on 4/5/2015.
 */
public class LiveStatusSpashActivity extends ActionBarActivity {
    private DataShareSingleton appData = DataShareSingleton.getInstance();
    private String fromStn = "";
    private String toStn = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        fromStn = appData.getFromStation();
        toStn = appData.getToStation();

        new WebServiceCall().execute();
    }

    public class WebServiceCall extends AsyncTask<Void, Void, String> {

        private DataShareSingleton commonData = DataShareSingleton.getInstance();
        private ProgressDialog mProgressDialog = null;
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(DataShareSingleton.getInstance().getUrl());
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String results) {
            try {
                if (results != null) {
                    if (commonData.getWebServiceCallType() == WebServiceCallType.LIVE_STATUS_CALL) {
//                    finish();
                        setContentView(R.layout.livestatus_layout);
                        results = results.substring(1, results.length() - 1);
                        JSONObject jsonResult = new JSONObject(results);
                        WebView liveStatusWebView = ((WebView) findViewById(R.id.liveStatusWebView));
                        WebSettings webSettings = liveStatusWebView.getSettings();
                        webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                        liveStatusWebView.loadDataWithBaseURL(null, jsonResult.getString("result"), "text/html", "UTF-8", null);
                    }
                }
            } catch (Exception e){
e.printStackTrace();
            }
        }

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();

            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);
                if (n>0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }
    }


}
