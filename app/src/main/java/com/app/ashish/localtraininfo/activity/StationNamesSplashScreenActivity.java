package com.app.ashish.localtraininfo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.app.ashish.localtraininfo.R;
import com.app.ashish.localtraininfo.util.DataShareSingleton;
import com.app.ashish.localtraininfo.util.RailInfoUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ashis_000 on 4/5/2015.
 */
public class StationNamesSplashScreenActivity extends ActionBarActivity {
    private DataShareSingleton appData = DataShareSingleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        new WebServiceCall().execute();
    }

    public class WebServiceCall extends AsyncTask<Void, Void, String> {

        private DataShareSingleton commonData = DataShareSingleton.getInstance();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if(!isNetworkConnected()) {
                Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION",Toast.LENGTH_LONG).show();
            }
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
            if(results == null || results.length() < 100) {
                results = commonData.getHardCodedAllStations();

            }
            if (results != null) {

                if (commonData.getAllStnList() == null) {
                    List<String> allStnMap = RailInfoUtil.parseStationDetails(results);
                    commonData.setAllStnList(allStnMap);
                }


                // close this activity
                finish();
                //DataShareSingleton.getInstance().getEditText().setText(results);
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}
