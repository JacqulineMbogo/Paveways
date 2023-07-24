package com.paveways.Tenant;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.paveways.AboutPage;
import com.paveways.Auth.LogIn;
import com.paveways.ContactPage;
import com.paveways.Feedback.FeedbackHistory;
import com.paveways.HelpPage;
import com.paveways.Home.HomeActivity;
import com.paveways.R;
import com.paveways.Tenant.Services.Service_Requests_History;
import com.paveways.Tenant.Services.ServicesHome;

public class TenantHome extends AppCompatActivity {

    String TAG = "TenantHomeActivity";
    LinearLayout maintain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_home);

        maintain=findViewById(R.id.maintain);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        maintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenantHome.this, ServicesHome.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tenant_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shop:
                Intent intent = new Intent(TenantHome.this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.service:
                Intent intent9 = new Intent(TenantHome.this, Service_Requests_History.class);
                startActivity(intent9);
                return true;
            case R.id.feedback:
                Intent intent3 = new Intent(TenantHome.this, FeedbackHistory.class);
                startActivity(intent3);
                return true;
            case R.id.signout:
                Intent intent4 = new Intent(TenantHome.this, LogIn.class);
                startActivity(intent4);
                return true;
            case R.id.help:
                Intent intent5 = new Intent(TenantHome.this, HelpPage.class);
                startActivity(intent5);
                return true;
            case R.id.about_us:
                Intent intent6 = new Intent(TenantHome.this, AboutPage.class);
                startActivity(intent6);
                return true;
            case R.id.Contact:
                Intent intent7 = new Intent(TenantHome.this, ContactPage.class);
                startActivity(intent7);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
