package com.paveways;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.Utility.Constant;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.cart.CartDetails;
import com.paveways.cart.Order_Summary;

import java.util.Objects;

public class TermsAndConditions extends AppCompatActivity {

    Button accept;
    TextView terms;
    SharedPreferenceActivity sharedPreferenceActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        sharedPreferenceActivity = new SharedPreferenceActivity(this);


        accept =  findViewById(R.id.acceptButton);
        terms = findViewById(R.id.terms);

        if(Objects.equals(sharedPreferenceActivity.getItem(Constant.SALE_TYPE), "sale") || Objects.equals(sharedPreferenceActivity.getItem(Constant.SALE_TYPE), "own")){
            terms.setText(R.string.sale_terms_and_conditions_content);
        }else{
            terms.setText(R.string.rent_terms_and_conditions_content);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(TermsAndConditions.this , Order_Summary. class);
                startActivity(intent);


            }
        });

    }
}
