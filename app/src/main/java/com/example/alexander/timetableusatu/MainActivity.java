package com.example.alexander.timetableusatu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity{

    public static final String APP_PREFERENCES_GROUP_NAME = "prefGroupName";
    public static final String APP_PREFERENCES_GROUP_ID = "prefGroupId";
    public static final boolean APP_PREFERENCES_CHECK_IN = false;
    public static final String APP_PREFERENCES_SELECTED_GROUP_NAME = "";
    public static final String APP_PREFERENCES_SELECTED_GROUP_ID = "";
    public static String todayDate = "16.09.2016";
    public static String[][] weeks = {
            {"29.08.2016", "30.08.2016", "31.08.2016", "01.09.2016", "02.09.2016", "03.09.2016"},
            {"05.09.2016", "06.09.2016", "07.09.2016", "08.09.2016", "09.09.2016", "10.09.2016"},
            {"12.09.2016", "13.09.2016", "14.09.2016", "15.09.2016", "16.09.2016", "17.09.2016"},
            {"19.09.2016", "20.09.2016", "21.09.2016", "22.09.2016", "23.09.2016", "24.09.2016"},
            {"26.09.2016", "27.09.2016", "28.09.2016", "29.09.2016", "30.09.2016", "01.10.2016"},
            {"03.10.2016", "04.10.2016", "05.10.2016", "06.10.2016", "07.10.2016", "08.10.2016"},
            {"10.10.2016", "11.10.2016", "12.10.2016", "13.10.2016", "14.10.2016", "15.10.2016"},
            {"17.10.2016", "18.10.2016", "19.10.2016", "20.10.2016", "21.10.2016", "22.10.2016"},
            {"24.10.2016", "25.10.2016", "26.10.2016", "27.10.2016", "28.10.2016", "29.10.2016"},
            {"31.10.2016", "01.11.2016", "02.11.2016", "03.11.2016", "04.11.2016", "05.11.2016"},
            {"07.11.2016", "08.11.2016", "09.11.2016", "10.11.2016", "11.11.2016", "12.11.2016"},
            {"14.11.2016", "15.11.2016", "16.11.2016", "17.11.2016", "18.11.2016", "19.11.2016"},
            {"21.11.2016", "22.11.2016", "23.11.2016", "24.11.2016", "25.11.2016", "26.11.2016"},
            {"28.11.2016", "29.11.2016", "30.11.2016", "01.12.2016", "02.12.2016", "03.12.2016"},
            {"05.12.2016", "06.12.2016", "07.12.2016", "08.12.2016", "09.12.2016", "10.12.2016"},
            {"12.12.2016", "13.12.2016", "14.12.2016", "15.12.2016", "16.12.2016", "17.12.2016"},
            {"19.12.2016", "20.12.2016", "21.12.2016", "22.12.2016", "23.12.2016", "24.12.2016"},
            {"26.12.2016", "27.12.2016", "28.12.2016", "29.12.2016", "30.12.2016", "31.12.2016"}
    };
    public static String[][] weeksName = {
            {"29 августа", "30 августа", "31 августа", "1 сентября", "2 сентября", "3 сентября"},
            {"5 сентября", "6 сентября", "7 сентября", "8 сентября", "9 сентября", "10 сентября"},
            {"12 сентября", "13 сентября", "14 сентября", "15 сентября", "16 сентября", "17 сентября"},
            {"19 сентября", "20 сентября", "21 сентября", "22 сентября", "23 сентября", "24 сентября"},
            {"26 сентября", "27 сентября", "28 сентября", "29 сентября", "30 сентября", "1 октября"},
            {"3 октября", "4 октября", "5 октября", "6 октября", "7 октября", "8 октября"},
            {"10 октября", "11 октября", "12 октября", "13 октября", "14 октября", "15 октября"},
            {"17 октября", "18 октября", "19 октября", "20 октября", "21 октября", "22 октября"},
            {"24 октября", "25 октября", "26 октября", "27 октября", "28 октября", "29 октября"},
            {"31 октября", "1 ноября", "2 ноября", "3 ноября", "4 ноября", "5 ноября"},
            {"7 ноября", "8 ноября", "9 ноября", "10 ноября", "11 ноября", "12 ноября"},
            {"14 ноября", "15 ноября", "16 ноября", "17 ноября", "18 ноября", "19 ноября"},
            {"21 ноября", "22 ноября", "23 ноября", "24 ноября", "25 ноября", "26 ноября"},
            {"28 ноября", "29 ноября", "30 ноября", "1 декабря", "2 декабря", "3 декабря"},
            {"5 декабря", "6 декабря", "7 декабря", "8 декабря", "9 декабря", "10 декабря"},
            {"12 декабря", "13 декабря", "14 декабря", "15 декабря", "16 декабря", "17 декабря"},
            {"19 декабря", "20 декабря", "21 декабря", "22 декабря", "23 декабря", "24 декабря"},
            {"26 декабря", "27 декабря", "28 декабря", "29 декабря", "30 декабря", "31 декабря"}
    };

    static SharedPreferences prefName;
    static SharedPreferences prefId;

    public static DatabaseHelper myDbHelper;
    public static float scale;
    static ViewPager viewPager;
    int DIALOG_DATE;
    static Spinner weekSpinnerToolbar;
    public static int selectedWeek;
    static int selectedWeekDay;
    public static String savedTitle, savedId;
    TextView textViewTitle;
    Calendar dateAndTime = Calendar.getInstance();
    private static Context con;
    MenuItem menuItemToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scale = getResources().getDisplayMetrics().density;

        prefName = getSharedPreferences(APP_PREFERENCES_GROUP_NAME, Context.MODE_PRIVATE);
        prefId = getSharedPreferences(APP_PREFERENCES_GROUP_ID, Context.MODE_PRIVATE);

        if (prefName.getBoolean(String.valueOf(APP_PREFERENCES_CHECK_IN ), false) != true) {
            Intent intent = new Intent(this, GreetActivity.class);
            startActivity(intent);
        }

        MainActivity.con = getApplicationContext();

        myDbHelper = new DatabaseHelper(MainActivity.this);
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
        Cursor c = myDbHelper.query("SCHEDULE", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {

            } while (c.moveToNext());
        }

        //Appbar, find the toolbar and assign him to the handler
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //apportunity to changing selected group
        textViewTitle = (TextView) findViewById(R.id.textViewTittle);
        textViewTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.textViewTittle:
                        Intent intent = new Intent(MainActivity.this, ChoiceGroupActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        //setting the tittle
        savedTitle = prefName.getString(APP_PREFERENCES_SELECTED_GROUP_NAME, "");
        textViewTitle.setText(savedTitle);
        savedId = prefId.getString(APP_PREFERENCES_SELECTED_GROUP_ID, "");

        //Tabs + ViewPager

        //Establecer el PageAdapter del componente ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiFragmentPagerAdapter(
                getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        //Appbar page filter
        weekSpinnerToolbar = (Spinner) findViewById(R.id.CmbToolbar);
        //Adapter for weekSpinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getSupportActionBar().getThemedContext(),
                R.layout.appbar_filter_title,
                new String[]{"1 неделя", "2 неделя", "3 неделя", "4 неделя", "5 неделя", "6 неделя",
                        "7 неделя", "8 неделя", "9 неделя", "10 неделя", "11 неделя", "12 неделя",
                        "13 неделя", "14 неделя", "15 неделя", "16 неделя", "17 неделя", "18 неделя"});

        adapter.setDropDownViewResource(R.layout.appbar_filter_list);

        weekSpinnerToolbar.setAdapter(adapter);


        //выбор спиннера недели
        weekSpinnerToolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //... Acciones al seleccionar una opción de la lista

                selectedWeek = i;

                weekSpinnerToolbar.setSelection(i);
                int position = viewPager.getCurrentItem();
                setNewViewPager();
                viewPager.setCurrentItem(position);

                Log.i("weekSpinnerToolbar", "selected week " + selectedWeek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //... Acciones al no existir ningún elemento seleccionado
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuItemToday = menu.findItem(R.id.action_today);
        onOptionsItemSelected(menuItemToday);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_DatePikerDialog) {
            showDialog(DIALOG_DATE);
        }
        
        if (id == R.id.action_today){
            Calendar c = new GregorianCalendar();
            todayDate = normalDay(c.get(Calendar.DATE)) + "." + normalMonth(c.get(Calendar.MONTH)) + "." + c.get(Calendar.YEAR);
            weekSpinnerToolbar.setSelection(getWeekByDate(todayDate));
            int today = c.get(Calendar.DAY_OF_WEEK);
            //setNewViewPager();
            if(today == 1 ) {
                viewPager.setCurrentItem(5);
            }else {
                viewPager.setCurrentItem(today - 2);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog myDatePikerDialog = new DatePickerDialog(this, myCallBack, dateAndTime.get(Calendar.YEAR),
                                                                                        dateAndTime.get(Calendar.MONTH),
                                                                                        dateAndTime.get(Calendar.DAY_OF_MONTH));
            todayDate = normalDay(dateAndTime.get(Calendar.DAY_OF_MONTH)) + "."
                            + normalMonth(dateAndTime.get(Calendar.MONTH)) + "."
                            + dateAndTime.get(Calendar.YEAR);
            return myDatePikerDialog;
        }
        return super.onCreateDialog(id);
    }

    public static int weekday(int day, int month, int year){
        month+=1;
        String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int a = (14 - month) / 12, y = year - a, m = month + 12 * a - 2;
        Log.i("DataTimePicker", "selected week day: " + days[(7000 + (day + y + y / 4 - y / 100 + y / 400 + (31 * m) / 12)) % 7]);
        return (7000 + (day + y + y / 4 - y / 100 + y / 400 + (31 * m) / 12)) % 7;
    }
    //выбор даты датапикера
    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            selectedWeekDay = weekday(dayOfMonth, monthOfYear, year);
            String selectedDate = normalDay(dayOfMonth) + "." + normalMonth(monthOfYear) + "." + year;

            selectedWeek = getWeekByDate(selectedDate);
            if (selectedWeek != 17) {
                weekSpinnerToolbar.setSelection(selectedWeek);
                setNewViewPager();
                viewPager.setCurrentItem(selectedWeekDay - 1);
            } else {
                Toast.makeText(MainActivity.this, "На эту дату нет расписания", Toast.LENGTH_SHORT).show();
            }
        }

    };

    public static int getWeekByDate(String date){
        for (int i = 0; i<17; i++)
            for(int j = 0; j<6; j++) {
                if (weeks[i][j].equals(date))
                    return i;
            }
        return 17;
    }

    public String normalDay(int dayOfMonth){
        if(dayOfMonth/10 == 0) {
            String returnDay = "0" + dayOfMonth;
            return returnDay;
        } else return Integer.toString(dayOfMonth);
    }

    public String normalMonth(int monthOfYear){
        monthOfYear++;
        if(monthOfYear/10 == 0) {
            String returnMonth = "0" + monthOfYear;
            return returnMonth;
        } else return Integer.toString(monthOfYear);
    }
    public void setNewViewPager(){
        viewPager.setAdapter(new MiFragmentPagerAdapter(
                getSupportFragmentManager()));
    }

    public static Context getAppContext() {
        return MainActivity.con;
    }
}
