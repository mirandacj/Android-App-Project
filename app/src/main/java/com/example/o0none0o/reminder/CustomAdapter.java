package com.example.o0none0o.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by o0None0o on 10/1/2015.
 */
public class CustomAdapter extends ArrayAdapter<Event> {
    CustomAdapter (Context context, Event[] events)
    {
        super(context, R.layout.custom_table, events);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_table, parent, false);
        String event = getItem(position).getName();
        String desc = getItem(position).getDescription();
        String date = getItem(position).getEnd().getTime().toString();
        TextView tvName = (TextView) customView.findViewById(R.id.tvName);
        TextView tvDesc = (TextView) customView.findViewById(R.id.tvDesc);
        TextView tvDate = (TextView) customView.findViewById(R.id.tvDate);

        tvName.setText(event);
        tvDesc.setText(desc);
        tvDate.setText(date);
        return customView;
    }

}

