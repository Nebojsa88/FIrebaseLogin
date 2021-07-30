package com.example.firebaselogin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaselogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneSignActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private Button sendCode;
    private EditText smsCode;
    private Button signWithPhone;

    private String sentCode;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sign);

        phoneNumber = findViewById(R.id.editTextSignPhone);
        sendCode = findViewById(R.id.btnSendSms);
        smsCode = findViewById(R.id.editTextSentCode);
        signWithPhone = findViewById(R.id.btnPhoneSignIn);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhoneNumber = phoneNumber.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhoneNumber, 60, TimeUnit.SECONDS, PhoneSignActivity.this, mCallBack);

            }
        });

        signWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signWithPhoneCode();

            }
        });
    }

    public void signWithPhoneCode(){

        String enterUserCode = smsCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sentCode, enterUserCode);
        signInWithPhoneAuthCredential(credential);

    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Intent i = new Intent(PhoneSignActivity.this, MainMenu.class);
                    startActivity(i);
                    finish();

                }else {

                    Toast.makeText(PhoneSignActivity.this, "Code is not correct ", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            sentCode = s;
        }
    };
}