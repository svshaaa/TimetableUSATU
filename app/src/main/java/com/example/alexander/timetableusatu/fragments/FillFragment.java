package com.example.alexander.timetableusatu.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.alexander.timetableusatu.MainActivity;
import com.example.alexander.timetableusatu.R;

public class FillFragment {

    public static View fillFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState,
                                    LinearLayout linearGeneral, LinearLayout linearDate, TextView tvDate, boolean increate, int i){

        View tableDay = inflater.inflate(R.layout.fragment_monday, container, false);
        linearGeneral = (LinearLayout) tableDay.findViewById(R.id.linearGeneral);
        linearGeneral.removeAllViews();
        linearDate = (LinearLayout) tableDay.findViewById(R.id.linearDate);
        tvDate = (TextView) tableDay.findViewById(R.id.tvDate);
        tvDate.setText(MainActivity.weeksName[MainActivity.selectedWeek][0]);
        increate = true;

        String[] selectionArgs = new String[]{MainActivity.weeks[MainActivity.selectedWeek][i], MainActivity.savedId};
        String selection = "(DATE = ?) AND (GROUP_ID = ?)";
        linearGeneral.removeAllViews();

        Cursor c = MainActivity.myDbHelper.query("SCHEDULE", null, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            do {
                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout linearLayout = new LinearLayout(MainActivity.getAppContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                lParams.setMargins((int) (15 * MainActivity.scale + 0.5f), 0, (int) (15 * MainActivity.scale + 0.5f), (int) (10 * MainActivity.scale + 0.5f));
                linearGeneral.addView(linearLayout, lParams);

                String type = c.getString(c.getColumnIndex("TYPE"));

                if (type.equals("bgc-practical-seminar")) {
                    linearLayout.setBackgroundColor(0xFFFFBB8E);
                } else if (type.equals("bgc-pe-class")) {
                    linearLayout.setBackgroundColor(0xFFF3E9B1);
                } else if (type.equals("bgc-lecture")) {
                    linearLayout.setBackgroundColor(0xFF7FDCE6);
                } else if (type.equals("bgc-labwork")) {
                    linearLayout.setBackgroundColor(0xFFC5BCF8);
                } else if (type.equals("bgc-lecture-practical")) {
                    linearLayout.setBackgroundColor(0xFF99D482);
                }


                TextView tvNumber = new TextView(MainActivity.getAppContext());
                LinearLayout.LayoutParams lParamsNumber = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lParamsNumber.setMargins(20, 10, 0, 0);

                if (c.getString(7).equals("1"))
                    tvNumber.setText(MainActivity.getAppContext().getResources().getString(R.string.FirstLesson));
                else if (c.getString(7).equals("2"))
                    tvNumber.setText(MainActivity.getAppContext().getResources().getString(R.string.SecondLesson));
                else if (c.getString(7).equals("3"))
                    tvNumber.setText(MainActivity.getAppContext().getResources().getString(R.string.ThirdLesson));
                else if (c.getString(7).equals("4"))
                    tvNumber.setText(MainActivity.getAppContext().getResources().getString(R.string.FourthLesson));
                else if (c.getString(7).equals("5"))
                    tvNumber.setText(MainActivity.getAppContext().getResources().getString(R.string.FifthLesson));
                else if (c.getString(7).equals("6"))
                    tvNumber.setText(MainActivity.getAppContext().getResources().getString(R.string.SixthLesson));
                else if (c.getString(7).equals("7"))
                    tvNumber.setText(MainActivity.getAppContext().getResources().getString(R.string.SeventhLesson));


                tvNumber.setTextSize(15);
                tvNumber.setTextColor(0xFF3F4243);
                linearLayout.addView(tvNumber, lParamsNumber);

                TextView tvSubject = new TextView(MainActivity.getAppContext());
                LinearLayout.LayoutParams lParamsSubject = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lParamsSubject.gravity = Gravity.CENTER_HORIZONTAL;
                lParamsSubject.setMargins(0, 10, 0, 0);
                tvSubject.setWidth((int) (276 * MainActivity.scale + 0.5f));
                tvSubject.setGravity(Gravity.CENTER);
                if (c.getString(3).equals("Элективные курсы по физической культуре"))
                    tvSubject.setText("Физвоспитание");
                else tvSubject.setText(c.getString(3));
                tvSubject.setTextSize(18);
                tvSubject.setTypeface(null, Typeface.BOLD);
                tvSubject.setTextColor(Color.BLACK);
                linearLayout.addView(tvSubject, lParamsSubject);

                TextView tvClassroom = new TextView(MainActivity.getAppContext());
                LinearLayout.LayoutParams lParamsClassroom = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lParamsClassroom.gravity = Gravity.CENTER;
                lParamsClassroom.setMargins(0, 10, 0, 20);
                tvClassroom.setText(c.getString(5));
                tvClassroom.setTextSize(18);
                tvClassroom.setTextColor(Color.BLACK);
                linearLayout.addView(tvClassroom, lParamsClassroom);

            } while (c.moveToNext());
        }
        return tableDay;
    }

}
