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
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    public static final String APP_PREFERENCES_GROUP_NAME = "prefGroupName";
    public static final String APP_PREFERENCES_GROUP_ID = "prefGroupId";
    public static final boolean APP_PREFERENCES_CHECK_IN = false;
    public static final String APP_PREFERENCES_SELECTED_GROUP_NAME = "";
    public static final String APP_PREFERENCES_SELECTED_GROUP_ID = "";
    public static String todayDate = "16.09.2016";
    public static String[][] weeks = new String[18][7];
    public static String[][] weeksName = new String[18][7];

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
        DateGeneration();
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

    public static void DateGeneration(){
        Calendar c = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), 7, 20);
        if(cNow.compareTo(c) == 1){
            c.set(c.get(Calendar.YEAR), 8, 1);
        } else {
            c.set(c.get(Calendar.YEAR), 0, 1);
            c.roll(Calendar.WEEK_OF_YEAR, 6);
        }
        while(c.get(Calendar.DAY_OF_WEEK) >2){
            c.roll(Calendar.DAY_OF_YEAR, false);
        }
        String currentMonth;
        for(int i=0; i<18; i++){
            for(int j=0; j<7; j++){
                weeks[i][j] = normalDay(c.get(Calendar.DATE)) + "." + normalMonth(c.get(Calendar.MONTH)) + "." + c.get(Calendar.YEAR);
                currentMonth = getCurrentMonth(c);
                weeksName[i][j] = String.valueOf(c.get(Calendar.DATE)) + " " + currentMonth;
                c.roll(Calendar.DAY_OF_YEAR, true);
            }
        }
    }

    private static String getCurrentMonth(Calendar c) {
        Locale locale = Locale.getDefault();
        String month = "month";
        if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("August")) month = "августа";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("September")) month=  "сентября";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("October")) month = "октября";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("November")) month = "ноября";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("December")) month = "декабря";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("January")) month = "января";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("February")) month = "февраля";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("March")) month = "марта";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("April")) month = "апреля";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("May")) month = "мая";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("June")) month = "июня";
        else if (c.getDisplayName(Calendar.MONTH, Calendar.LONG,locale).equals("Jule")) month = "июля";
        return month;
    }

    public static String normalDay(int dayOfMonth){
        if(dayOfMonth/10 == 0) {
            String returnDay = "0" + dayOfMonth;
            return returnDay;
        } else return Integer.toString(dayOfMonth);
    }

    public static String normalMonth(int monthOfYear){
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
