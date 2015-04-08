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
import com.app.ashish.localtraininfo.bean.TrainScheduleBean;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
                        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                        StringBuilder sb = new StringBuilder();
                        sb.append("<HTML><HEAD><LINK href=\"file:///android_asset/railInfo.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
                        sb.append(parseAndFormatHtml(jsonResult.getString("result")));
                        sb.append("</body></HTML>");

                        liveStatusWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8", null);
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


    private static String parseAndFormatHtml(String htmlData) {
        List<TrainScheduleBean> trainScheduleLst = new ArrayList<>();

        // If the train already reached to the destination then do not format the
        Document trainStatusHtml = Jsoup.parse(htmlData);
        trainStatusHtml.outputSettings().prettyPrint(false); // this will make sure that it will not add line breaks
        Element firstTr =  trainStatusHtml.select("tr").get(0);
        Element firstTable =  firstTr.parent();

        // If the element has one tr then do not change the html table
        if(firstTr.childNodes().size() <= 1) {
            Elements allTrs = firstTable.children();
            for(int i = 2; i < allTrs.size(); i++) {
                Element tr = allTrs.get(i);
                TrainScheduleBean trainSchedule = new TrainScheduleBean();
                trainSchedule.setTrainReachedDest(true);
                trainSchedule.setSlNo(tr.child(0).html());
                trainSchedule.setStationName(tr.child(1).html());
                trainSchedule.setSchArr(tr.child(2).html());
                trainSchedule.setSchDept(tr.child(3).html());
                trainScheduleLst.add(trainSchedule);
            }
        } else {
            // Modify the html
            // Remove the tr(0)/td(2) (PF column) from the html
            firstTr.child(1).remove();
            String trainName = firstTr.child(0).html();
            String currentStatusHtml = firstTr.child(1).html();
            firstTr.child(1).remove(); // Remove the current status span


            // Add another row in the table with currentStatusHtml
            currentStatusHtml = "<tr><td colspan='6'><div style='color: blue; background: #f0f0f0;'>" + trainName + "</div>"
                    + "<div>" + currentStatusHtml + "</div></td></div></tr>";
            currentStatusHtml = currentStatusHtml.replace("</span>,", "</span><div>");
            currentStatusHtml = currentStatusHtml.replace("</b>, Updated:", "</b><div>Last Updated:");

            firstTable.child(0).html(currentStatusHtml);   //append(currentStatusHtml);

            // Remove arrival and departure from each row and merge the data with station name
            // 1. Remove two columns (Arr and Dept) from the heading
            firstTable.child(1).child(2).remove();
            firstTable.child(1).child(2).remove();

            // 2. Remove three columns corresponding to Arr, Dept, PF
            Elements allTrs = firstTable.children();
            for(int i = 2; i < allTrs.size(); i++) {
                Element tr = allTrs.get(i);
                String arrTime = tr.child(2).html();
                String deptTime = tr.child(3).html();

                TrainScheduleBean trainSchedule = new TrainScheduleBean();
                trainSchedule.setTrainReachedDest(false);
                trainSchedule.setKm(currentStatusHtml);
                trainSchedule.setKm(trainName);

                trainSchedule.setSlNo(tr.child(0).html());
                trainSchedule.setStationName(tr.child(1).html());
                trainSchedule.setSchArr(arrTime);
                trainSchedule.setSchDept(deptTime);
                trainSchedule.setDate(tr.child(5).html());
                trainSchedule.setActArr(tr.child(6).html());
                trainSchedule.setActDept(tr.child(7).html());
                trainSchedule.setKm(tr.child(8).html());
                trainScheduleLst.add(trainSchedule);


                tr.child(2).remove();
                tr.child(2).remove();
                tr.child(2).remove();

                String stationName = tr.child(1).html();
                stationName = "<div  style='color: blue;'><b>" + stationName + "</b></div><div>" + arrTime + "/" + deptTime + "</div>";
                tr.child(1).html(stationName);
            }
            htmlData = trainStatusHtml.toString();
        }
        return htmlData;
    }

}
