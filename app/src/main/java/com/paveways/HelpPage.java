package com.paveways;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.Utility.SharedPreferenceActivity;

public class HelpPage extends AppCompatActivity {
    SharedPreferenceActivity sharedPreferenceActivity;
    private String TAG ="Help";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


    }
}
