package com.app.ashish.localtraininfo.services;

import java.util.List;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public interface RailInfoInterface {
    public List<String> getStationListByName(String stnName);
    public List<String> getTrainListByRoute(String fromStn, String toStn);
}
