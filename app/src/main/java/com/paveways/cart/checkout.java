package com.paveways.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.R;


public class checkout extends AppCompatActivity {


    private String TAG =" checkout";
    private TextView mpesabutton;
    private ImageView cod;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout );



        mpesabutton= findViewById(R.id.mpesabutton);
        cod= findViewById(R.id.cod);

        mpesabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(checkout.this,PlaceOrderActivity.class);
                startActivity(intent);

            }
        });





    }

}
