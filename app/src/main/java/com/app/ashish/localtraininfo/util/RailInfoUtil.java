package com.app.ashish.localtraininfo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class RailInfoUtil {
    private static DataShareSingleton commonData = DataShareSingleton.getInstance();
    public static List<String> parseStationDetails(String serverResponse) {
        Map<String, String> allStationNames = new HashMap<>();
        Map<String, String> allStationCodes = new HashMap<>();
        List<String> allStations = new ArrayList<>();
        if(serverResponse != null) {
            serverResponse = serverResponse.substring(serverResponse.indexOf("\""), serverResponse.lastIndexOf("\";"));

            String[] allStn = serverResponse.split(",");

            // Bulk update the database
//            SQLiteDatabase userSettingsDB = null;
            try {
//                userSettingsDB = commonData.getContext().openOrCreateDatabase("APP_SETTINGS", Context.MODE_PRIVATE, null);
//                String sql = "Insert or Replace into STATIONS (STN_CODE, STN_NAME) values(?,?)";
//                SQLiteStatement insert = userSettingsDB.compileStatement(sql);
                for(int i = 0; i < allStn.length; i+=2) {
//                    allStationCodes.put(allStn[i], allStn[i + 1]);
//                    allStationNames.put(allStn[i+1], allStn[i]);
                    allStations.add((allStn[i] + "-" + allStn[i + 1]).toUpperCase());
//                    insert.bindString(1, allStn[i]);
//                    insert.bindString(2, allStn[i+1]);
//                    insert.execute();
                }
//                allStationNames.putAll(allStationCodes);
//                userSettingsDB.setTransactionSuccessful();
            } catch (Exception e) {

            } finally {
//                if(userSettingsDB != null ) userSettingsDB.close();
            }

        }
        return allStations;
    }

    public static List<String> getStationByCodeOrName(String stnCdOrName) {
        List<String> matchedStnLst = new ArrayList<String>();
        String regex = "(.*-|)" + stnCdOrName + ".*";
        Pattern p = Pattern.compile(regex);

        for (String stn:commonData.getAllStnList()) {
            if (p.matcher(stn).matches()) {
                matchedStnLst.add(stn);
            }
        }

        return matchedStnLst;
    }

}
