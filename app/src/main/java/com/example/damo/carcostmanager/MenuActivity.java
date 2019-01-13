package com.example.damo.carcostmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void click(View view) {
        LinearLayout extraContainer = null;

        switch (view.getId()){

            case R.id.fuelBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerFuel);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
            break;

            case R.id.serviceBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerService);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;

            case R.id.reviewBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerReview);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;

            case R.id.protectionBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerProtection);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;

            case R.id.editCarBtn:
                extraContainer = (LinearLayout)findViewById(R.id.extraContainerCarInfo);

                if (extraContainer.getVisibility() == View.GONE) {
                    extraContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (extraContainer.getVisibility() == View.VISIBLE) {
                    extraContainer.setVisibility(View.GONE);
                }
                break;
        }
    }
}
