package com.example.alexander.timetableusatu;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

public class ChoiceGroupActivity extends AppCompatActivity implements View.OnClickListener{
    //elements of screen
    static Button buttonNext;
    static Spinner spinnerGroup;
    //for database
    DatabaseHelper myDbHelper;
    //other used
    int faculty_position, klass_position, group_position = 0;   //position selected Spinners
    final String[] faculty = {"Факультет","АВИЭТ", "ИНЭК", "ОНФ", "ФАДЭТ", "ФАТС", "ФЗЧС", "ФИРТ"};
    final String[] klass = {"Курс", "1", "2", "3", "4", "5", "6"};
    ArrayList<String> groupNameList = new ArrayList<String>();  //list names of the current groups
    ArrayList<String> groupIdList = new ArrayList<String>();    //list id of the current groups
    boolean firstOpen = false; // for first inflate spinners

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_group);

        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        buttonNext.setEnabled(false);
        buttonNext.setBackgroundColor(0xFFF4F4F4);

        //аdapter of faculty
        ArrayAdapter<String> adapterFaculty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, faculty);
        adapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerFaculty = (Spinner) findViewById(R.id.spinnerFaculty);
        spinnerFaculty.setAdapter(adapterFaculty);
        //set of the click handler
        spinnerFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                faculty_position = position;
                test_can();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //adapter for klass
        ArrayAdapter<String> adapterKlass = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, klass);
        adapterKlass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerKlass = (Spinner) findViewById(R.id.spinnerKlass);
        spinnerKlass.setAdapter(adapterKlass);
        //set of the click handler
        spinnerKlass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                klass_position = position;
                test_can();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        myDbHelper = new DatabaseHelper(ChoiceGroupActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);
        sampleOfGroups();
        setSpinnerGroup();
        spinnerGroup.setEnabled(false);

    }

    public void sampleOfGroups() {
        //variables for query
        String[] selectionArgs = new String[]{Integer.toString(klass_position), Integer.toString(faculty_position)};
        String selection = "(COURSE = ?) AND (FACULTY_ID = ?)";
        String[] columns = new String[]{"GROUP_ID", "GROUP_NAME"};

        Cursor c = myDbHelper.query("GROUPS", columns, selection, selectionArgs, null, null, null);
        groupListStandart();
        if (c.moveToFirst()) {
            do {
                groupIdList.add(c.getString(0));
                groupNameList.add(c.getString(1));
            } while (c.moveToNext());
        }
    }

    public void setSpinnerGroup(){
        //adapter for groups
        ArrayAdapter<String> adapterGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, groupNameList);
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapterGroup);
        //set of the click handler
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(firstOpen) {
                    if(position != 0) {
                        MainActivity.prefName.edit().putString(MainActivity.APP_PREFERENCES_SELECTED_GROUP_NAME, groupNameList.get(position)).commit();
                        MainActivity.prefId.edit().putString(MainActivity.APP_PREFERENCES_SELECTED_GROUP_ID, groupIdList.get(position - 1)).commit();
                    }
                    group_position = position;
                }
                firstOpen = true;
                test_can_group();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    void test_can(){
        if (faculty_position != 0 & klass_position != 0){
            spinnerGroup.setEnabled(true);
            sampleOfGroups();
            setSpinnerGroup();
        } else {
            buttonNext.setBackgroundColor(0xFFF4F4F4);
            buttonNext.setEnabled(false);
            spinnerGroup.setSelection(0);
            spinnerGroup.setEnabled(false);
        }
    }

    void test_can_group(){
        if(group_position != 0){
            buttonNext.setEnabled(true);
            buttonNext.setBackgroundColor(0xFF139DD1);
        } else {
            buttonNext.setBackgroundColor(0xFFF4F4F4);
            buttonNext.setEnabled(false);
        }
    }

    public void groupListStandart(){
        groupNameList.clear();
        groupIdList.clear();
        groupNameList.add("Группа");
        groupIdList.clear();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case R.id.buttonNext:
               MainActivity.prefName.edit().putBoolean(String.valueOf(MainActivity.APP_PREFERENCES_CHECK_IN ), true).commit(); // пишем в SharedPreferences CHEK_IN true
               Intent intent = new Intent(this, MainActivity.class);
               startActivity(intent);
        }
    }
}
