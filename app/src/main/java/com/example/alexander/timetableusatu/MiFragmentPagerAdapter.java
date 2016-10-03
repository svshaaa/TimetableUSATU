package com.example.alexander.timetableusatu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alexander.timetableusatu.fragments.FragmentFriday;
import com.example.alexander.timetableusatu.fragments.FragmentMonday;
import com.example.alexander.timetableusatu.fragments.FragmentSaturday;
import com.example.alexander.timetableusatu.fragments.FragmentThursday;
import com.example.alexander.timetableusatu.fragments.FragmentTuesday;
import com.example.alexander.timetableusatu.fragments.FragmentWednesday;

public class MiFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 6;
    private final String tabTitles[] =
            new String[] {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    public MiFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        switch(position) {
            case 0:
                f = FragmentMonday.newInstance();
                break;
            case 1:
                f = FragmentTuesday.newInstance();
                break;
            case 2:
                f = FragmentWednesday.newInstance();
                break;
            case 3:
                f = FragmentThursday.newInstance();
                break;
            case 4:
                f = FragmentFriday.newInstance();
                break;
            case 5:
                f = FragmentSaturday.newInstance();
                break;
        }

        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}