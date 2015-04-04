package com.app.ashish.localtraininfo.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
