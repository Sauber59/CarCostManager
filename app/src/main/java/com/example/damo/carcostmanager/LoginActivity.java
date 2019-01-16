package com.example.damo.carcostmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText loginET;
    private EditText passwordET;
    private CheckBox rememberCB;
    private Button loginBtn;
    private Button registerBtn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){

        }

        progressDialog = new ProgressDialog(this);

        loginET = (EditText)findViewById(R.id.loginET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        rememberCB = (CheckBox) findViewById(R.id.rememberCB);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
    }

    private void registerUser(){
        String email = loginET.getText().toString().trim();
        String password = loginET.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Uzupełnij email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Uzupełnij haslo", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Rejestrowanie w trakcie.. ");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Rejestracja się powiodła",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Rejestracja się nie powiodła", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
    }

    private void userLogin() {
        String email = loginET.getText().toString().trim();
        String password = loginET.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Uzueplenij email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Uzueplenij haslo", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logowanie w trakcie.. ");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog.dismiss();
                        if (task.isSuccessful()){
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Zalogowano",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Logowanie się nie powiodło", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
    }

    public void click(View view) {
        switch (view.getId()){

            case R.id.loginBtn:
                userLogin();
                break;

            case R.id.registerBtn:
                registerUser();
                break;
        }

    }
}
