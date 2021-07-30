package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaselogin.activities.MainMenu;
import com.example.firebaselogin.activities.PhoneSignActivity;
import com.example.firebaselogin.activities.ResetPassword;
import com.example.firebaselogin.activities.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mail;
    private EditText password;
    private Button signIn;
    private Button signUp;
    private Button phoneSign;

    private Button forgotPassword;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mail = findViewById(R.id.editTextEmail);
        password =findViewById(R.id.editTextPassword);
        signIn = findViewById(R.id.btnSignIn);
        signUp = findViewById(R.id.btnSignUp);
        forgotPassword = findViewById(R.id.btnForgot);
        phoneSign = findViewById(R.id.btnSignInPhone);

        phoneSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PhoneSignActivity.class);
                startActivity(i);
                finish();

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();
                signInFirebase(userEmail, userPassword);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(i);
            }
        });

    }

    public void signInFirebase(String userMail, String userPassword){

        auth.signInWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(MainActivity.this, MainMenu.class);
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Mail or Password is not correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null){

            Intent i = new Intent(MainActivity.this, MainMenu.class);
            startActivity(i);
            finish();
        }
    }
}