package com.app.ashish.localtraininfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.ashish.localtraininfo.services.RailInfoInterface;
import com.app.ashish.localtraininfo.services.RailInfoServices;

import java.util.ArrayList;

/**
 * Created by ashis_000 on 4/4/2015.
 */
public class StationListAdapter extends BaseAdapter implements Filterable{
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
