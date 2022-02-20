package com.paveways.Orders;

import android.print.PrintManager;
import android.content.Context;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.R;


public class CustomPrintActivity extends AppCompatActivity {


    public void printDocument(View view)
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        printManager.print(jobName, new
                        MyPrintDocumentAdapter(this),
                null);
    }
}



