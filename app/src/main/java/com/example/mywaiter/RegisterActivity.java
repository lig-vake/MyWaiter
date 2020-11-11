package com.example.mywaiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edLogin, edPassword, edPasswordSec;
    private String email, password;
    FirebaseUser currentUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);
        edPasswordSec = findViewById(R.id.edPasswordSec);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Регистрация");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void onClickRegister(View view) {
        boolean allowRegister = true;

        TextInputLayout tilLogin = findViewById(R.id.tilLogin);
        TextInputLayout tilPassword = findViewById(R.id.tilPassword);
        TextInputLayout tilPasswordSec = findViewById(R.id.tilPasswordSec);

        tilLogin.setError(null);
        tilPassword.setError(null);
        tilPasswordSec.setError(null);

        if(!edLogin.getText().toString().matches("^(.+)@(.+)$")) {
            tilLogin.setError("E-mail адрес должен быть действительным");
            allowRegister = false;
        }

        if(TextUtils.isEmpty(edLogin.getText().toString())) {
            tilLogin.setError("Необходимо ввести почту");
            allowRegister = false;
        }

        if(TextUtils.isEmpty(edPassword.getText().toString())) {
            tilPassword.setError("Необходимо придумать пароль");
            allowRegister = false;
        }

        if(!edPassword.getText().toString().matches(".*\\d.*")) {
            tilPassword.setError(null);
            tilPassword.setError("Пароль должен содержать как минимум одну цифру");
            allowRegister = false;
        }

        if(!edPassword.getText().toString().matches("[\\w]+")) {
            tilPassword.setError("Пароль может состоять только из букв латиницы и цифр");
            allowRegister = false;
        }

        if(edPassword.getText().toString().length() < 8) {
            tilPassword.setError("Пароль должен содержать как минимум 8 знаков");
            allowRegister = false;
        }


        if(!edPassword.getText().toString().equals(edPasswordSec.getText().toString())) {
            tilPasswordSec.setError("Пароли должны совпадать");
            allowRegister = false;
        }

        if (allowRegister) {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = mAuth.getCurrentUser();
                            if (!currentUser.isEmailVerified()) {
                                sendEmailVerification();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Произошла ошибка",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private void sendEmailVerification() {
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "На вашу почту было отправлено письмо для подтверждения", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Ошибка подтверждения аккаунта", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}