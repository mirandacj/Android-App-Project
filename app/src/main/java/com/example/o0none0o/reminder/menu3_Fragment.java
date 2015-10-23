package com.example.o0none0o.reminder;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by o0None0o on 9/30/2015.
 */
public class menu3_Fragment extends Fragment {
View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      rootview=inflater.inflate(R.layout.menu3_layout,container,false);
        return rootview;
    }



}
