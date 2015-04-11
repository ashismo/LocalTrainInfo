package com.app.ashish.localtraininfo.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by ashis_000 on 4/44/2015.
 */
public class DatabaseUtil {
    private Context context = null;
    private SQLiteDatabase userSettingsDB;
    public DatabaseUtil(Context context) {
        this.context = context;
        userSettingsDB = context.openOrCreateDatabase("APP_SETTINGS", Context.MODE_PRIVATE, null);
        userSettingsDB.execSQL("CREATE TABLE IF NOT EXISTS STATIONS(\n" +
                "\tSTN_CODE VARCHAR(50) NOT NULL,\n" +
                "\tSTN_NAME VARCHAR(50) NOT NULL,\n" +
                "\tPRIMARY KEY (STN_CODE))");

        userSettingsDB.execSQL("CREATE TABLE IF NOT EXISTS TRAIN_SEARCH_HISTORY(\n" +
                "\tTRAIN_NO VARCHAR(10) NOT NULL,\n" +
                "\tTRAIN_NAME VARCHAR(50) NOT NULL,\n" +
                "\tSEARCHED_ON TIMESTAMP,\n" +
                "\tPRIMARY KEY (TRAIN_NO))");

        userSettingsDB.close();
    }
    public void updateStationInDB(String stnCode, String stnName) {
        if(stnCode != null && stnName != null) {
            SQLiteDatabase userSettingsDB = context.openOrCreateDatabase("APP_SETTINGS", Context.MODE_PRIVATE, null);
            try {
                    userSettingsDB.execSQL("INSERT INTO STATIONS VALUES('" + stnCode + "','" + stnName + "');");
            } catch (Exception e) {

            } finally {
                userSettingsDB.close();
            }
        }
    }

    public void updateTrainInHistoryTable(String trainNo, String trainName) {
        if(trainNo != null && trainName != null) {
            SQLiteDatabase userSettingsDB = context.openOrCreateDatabase("APP_SETTINGS", Context.MODE_PRIVATE, null);
            try {
                // Check if the data is already there
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

                Cursor resultSet = userSettingsDB.rawQuery("Select * from TRAIN_SEARCH_HISTORY where " +
                        "TRAIN_NO='" + trainNo + "'", null);
                if (resultSet != null && resultSet.getCount() > 0) {
                    userSettingsDB.execSQL("UPDATE TRAIN_SEARCH_HISTORY SET SEARCHED_ON='" + currentTimeStamp +
                            "' WHERE TRAIN_NO='" + trainNo + "'");
                } else {
                    userSettingsDB.execSQL("INSERT INTO TRAIN_SEARCH_HISTORY VALUES('" + trainNo + "','" + trainName + "','" + currentTimeStamp + "');");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                userSettingsDB.close();
            }
        }
    }

    public String[] getTrainsFromHistoryTable() {
        String trainSearchHistory[] = null;
        SQLiteDatabase userSettingsDB = context.openOrCreateDatabase("APP_SETTINGS", Context.MODE_PRIVATE, null);
        try {

            Cursor resultSet = userSettingsDB.rawQuery("Select * from TRAIN_SEARCH_HISTORY ORDER BY SEARCHED_ON DESC", null);
            if(resultSet.getCount() > 0) {
                if(resultSet.getCount() >= 10) {
                    trainSearchHistory = new String[10];
                } else {
                    trainSearchHistory = new String[resultSet.getCount()];
                }
                int i = 0;
                while(resultSet.moveToNext()) {
                    if(i >= 10) { // delete the most old record
                        userSettingsDB.execSQL("DELETE FROM TRAIN_SEARCH_HISTORY WHERE TRAIN_NO='" + resultSet.getString(0) + "'");
                   } else {
                        trainSearchHistory[i++] = "[" + resultSet.getString(0) + "]" +resultSet.getString(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userSettingsDB.close();
        }
        return trainSearchHistory;
    }

    public String[] getStationByCodeOrName(String stnCdOrName) {
        String stnArray[] = null;
        if(stnCdOrName != null) {
            SQLiteDatabase userSettingsDB = context.openOrCreateDatabase("APP_SETTINGS", Context.MODE_PRIVATE, null);

            // Check if val exists for the given param
            try {
                Cursor resultSet = userSettingsDB.rawQuery("Select * from STATIONS where " +
                        "STN_CODE='" + stnCdOrName + "' or STN_NAME like '" + stnCdOrName + "%'", null);
                if (resultSet != null && resultSet.getCount() > 0) {
//                    resultSet.moveToFirst();
                    stnArray = new String[resultSet.getCount()];
                    int count = 0;
                    // Return maximum 4 records
                    while(resultSet.isAfterLast() == false) {
                        stnArray[count++] = resultSet.getString(0) + "-" + resultSet.getString(1);
                        resultSet.moveToNext();
                        if(count == 4) break;
                    }


                }
            } catch (Exception e) {
//                // Exception occurs if table not present. Below method call creates table for the first time
//                updateUserSettings(settingsParam, "false");
            } finally {
                userSettingsDB.close();
            }
        }
        return stnArray;
    }

    public SQLiteDatabase getUserSettingsDB() {
        return userSettingsDB;
    }
}
