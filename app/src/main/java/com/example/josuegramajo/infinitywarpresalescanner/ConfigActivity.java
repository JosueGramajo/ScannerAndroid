package com.example.josuegramajo.infinitywarpresalescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by josuegramajo on 3/23/18.
 */

public class ConfigActivity extends AppCompatActivity {

    RadioButton rbMinutes, rbSeconds;
    EditText etInterval, etPalabrasClave;
    Button btnSave;

    public static boolean minutesSelected = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        rbMinutes = (RadioButton) findViewById(R.id.radioButtonMinutes);
        rbSeconds = (RadioButton) findViewById(R.id.radioButtonSeconds);
        etInterval = (EditText) findViewById(R.id.editTextInterval);
        btnSave = (Button) findViewById(R.id.buttonSave);
        etPalabrasClave = (EditText) findViewById(R.id.textArea);
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(etInterval.getText().toString().equals("")){
                            Toast.makeText(ConfigActivity.this, "Ingrese un intervalo", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        minutesSelected = rbMinutes.isChecked();
                        if(minutesSelected){
                            MainActivity.repetitionInterval = (Integer.parseInt(etInterval.getText().toString()) * 60) * 1000;
                        }else{
                            MainActivity.repetitionInterval = (Integer.parseInt(etInterval.getText().toString()) * 1000);
                        }

                        String[] result = etPalabrasClave.getText().toString().split(",");
                        MainActivity.palabrasClave.clear();
                        for(String s : result){
                            MainActivity.palabrasClave.add(s);
                        }

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
        );

        rbMinutes.setChecked(minutesSelected);
        rbSeconds.setChecked(!rbMinutes.isChecked());

        int value = minutesSelected ? (MainActivity.repetitionInterval / 1000) / 60 : (MainActivity.repetitionInterval / 1000);
        etInterval.setText(String.valueOf(value));

        String resultString = "";
        for(String s : MainActivity.palabrasClave){
            resultString = resultString == "" ? s : resultString + "," + s;
        }
        etPalabrasClave.setText(resultString);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
