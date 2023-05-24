package com.paveways;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.cart.CartDetails;
import com.paveways.cart.Order_Summary;

public class TermsAndConditions extends AppCompatActivity {

    Button accept;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        accept =  findViewById(R.id.acceptButton);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(TermsAndConditions.this , Order_Summary. class);
                startActivity(intent);


            }
        });

    }
}
