package com.example.mywaiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edLogin, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);

        getSupportActionBar().setTitle("Вход в аккаунт");
    }

    public void onClickSignIn (View view) {
        String login, password;

        login = edLogin.getText().toString();
        password = edPassword.getText().toString();
        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        assert (currentUser != null);
                        if (currentUser.isEmailVerified()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Всё гуд.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Подтвердите пожалуйста вашу почту", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Ошибка входа.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}