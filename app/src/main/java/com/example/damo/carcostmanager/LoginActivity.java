package com.example.damo.carcostmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ViewUtils;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private String rememberedEmail = "";
    private String rememberedPassword = "";
    boolean rememberFlag = false;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        loginET = (EditText)findViewById(R.id.loginET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        rememberCB = (CheckBox) findViewById(R.id.rememberCB);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        mPreferences = getSharedPreferences("com.example.damo.carcostmanager", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        checkSharedPreferences();

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
            Toast.makeText(this, "Uzupełnij email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Uzupełnij haslo", Toast.LENGTH_SHORT).show();
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
// TODO: 16.01.2019 Dokonczyn zapamietywanie loginu i hasla 
            case R.id.rememberCB:
                if (rememberCB.isChecked()){
                    mEditor.putString(getString(R.string.checkbox), "True");
                    mEditor.commit();

                    //zapisz name
                    String name = loginET.getText().toString();
                    mEditor.putString(getString(R.string.name), name);
                    mEditor.commit();

                    //zapisz password
                    String password = passwordET.getText().toString();
                    mEditor.putString(getString(R.string.password), password);
                    mEditor.commit();

                }else{
                    mEditor.putString(getString(R.string.checkbox), "False");
                    mEditor.commit();

                    //zapisz name
                    mEditor.putString(getString(R.string.name), "");
                    mEditor.commit();

                    //zapisz password
                    mEditor.putString(getString(R.string.password), "");
                    mEditor.commit();
                }
        }

    }

    private void checkSharedPreferences(){
        String checkbox = mPreferences.getString(getString(R.string.checkbox), "False");
        String name = mPreferences.getString(getString(R.string.name), "");
        String password = mPreferences.getString(getString(R.string.password), "");

        loginET.setText(name);
        passwordET.setText(password);

        if (checkbox.equals("True")){
            rememberCB.setChecked(true);
        }else {
            rememberCB.setChecked(false);
        }

    }
}
