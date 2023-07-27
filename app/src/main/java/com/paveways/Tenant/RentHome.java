package com.paveways.Tenant;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.R;

public class RentHome extends AppCompatActivity {

    String TAG = "RentManagerHomeActivity";
    LinearLayout maintain, rent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_home);

    }
}
