package com.app.ashish.localtraininfo.bean;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public enum WebServiceCallType {
        ALL_STATION_NAME_CALL, //  http://111.118.213.140/js/cmp/stations.js
        LIVE_STATUS_CALL,      // http://111.118.213.141/getIR.aspx?jsonp=1&Data=RUNSTATUSALL~36836
        LIVE_STATUS_CALL2,     // http://runningstatus.in/status/36820-today
        TRAIN_SCHEDULE_CAL;    // http://erail.in/rail/getTrains.aspx?Station_From=JOX&Station_To=HWH&DataSource=0&Language=0&Cache=true
}
