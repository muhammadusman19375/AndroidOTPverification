package com.example.otpverificationcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private Button btn_getOtp;
    private ProgressBar progressBar;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phoneNumber=findViewById(R.id.phoneNumber);
        btn_getOtp=findViewById(R.id.btn_getOtp);
        progressBar=findViewById(R.id.progressBar);
        ccp=findViewById(R.id.ccp);

        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccp.registerCarrierNumberEditText(phoneNumber);
                String fullNumber=ccp.getFullNumberWithPlus();

                if(phoneNumber.getText().toString().trim().isEmpty()){
                    Toast.makeText(PhoneNumberActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }
                    progressBar.setVisibility(View.VISIBLE);
                    btn_getOtp.setVisibility(View.INVISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(fullNumber,
                            60,
                            TimeUnit.SECONDS,
                            PhoneNumberActivity.this,

                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    btn_getOtp.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    btn_getOtp.setVisibility(View.VISIBLE);
                                    Toast.makeText(PhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    btn_getOtp.setVisibility(View.VISIBLE);
                                    Intent intent=new Intent(getApplicationContext(),OTPActivity.class);
                                    intent.putExtra("mobile", fullNumber);
                                    intent.putExtra("VerificationId",verificationId);
                                    startActivity(intent);
                                    finish();
                                }
                            }


                    );
            }
        });

    }
}