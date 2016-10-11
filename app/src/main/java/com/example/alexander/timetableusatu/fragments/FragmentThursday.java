package com.example.alexander.timetableusatu.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.alexander.timetableusatu.R;
import com.example.alexander.timetableusatu.MainActivity;


public class FragmentThursday extends Fragment {


    static LinearLayout linearGeneral, linearDate;
    public static TextView tvDate;
    public static boolean increate = false;

    public static FragmentThursday newInstance() {
        FragmentThursday fragment = new FragmentThursday();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return FillFragment.fillFragment(inflater, container, savedInstanceState, linearGeneral, linearDate, tvDate, increate, 3);
    }

    public static void setTvDate(String date){
        if(!increate) return;
        tvDate.setText(date);
    }

}
