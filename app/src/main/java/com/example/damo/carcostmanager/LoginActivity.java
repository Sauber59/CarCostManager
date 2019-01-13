package com.example.damo.carcostmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void click(View view) {
        Intent intent = null;
        switch (view.getId()){

            case R.id.loginBtn:
                intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
                break;

            case R.id.rejestracjaBtn:
                intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
                break;
        }

    }
}
