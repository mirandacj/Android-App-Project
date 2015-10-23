package com.example.o0none0o.reminder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by o0None0o on 10/17/2015.
 */
public class MenuFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int position = getArguments().getInt("position");
        String[] menus=getResources().getStringArray(R.array.menus);
        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tvContent);
        tv.setText(menus[position]);
        getActivity().getActionBar().setTitle(menus[position]);
        return v;
    }
}
