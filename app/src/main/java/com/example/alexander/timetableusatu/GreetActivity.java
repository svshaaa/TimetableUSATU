package com.example.alexander.timetableusatu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class GreetActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greet);


        Button buttonStartUsing;
        TextView textViewSlogan;

        textViewSlogan = (TextView) findViewById(R.id.textViewSlogan);

        buttonStartUsing = (Button) findViewById(R.id.buttonStartUsing);
        buttonStartUsing.setOnClickListener(this);

        Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.greet_alpha);
        Animation animTrans = AnimationUtils.loadAnimation(this, R.anim.greet_trans);

        buttonStartUsing.startAnimation(animTrans);
        textViewSlogan.startAnimation(animAlpha);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStartUsing:
                Intent intent = new Intent(this, ChoiceGroupActivity.class);
                startActivity(intent);
        }
    }
}
