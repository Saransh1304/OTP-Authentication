package com.example.personall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);


        final EditText inputMobile=findViewById(R.id.inputMobile);
        final Button buttonGetOTP =findViewById(R.id.buttonGetOTP);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputMobile.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter your mobile number",Toast.LENGTH_SHORT).show();
                }
                else if(inputMobile.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid mobile number",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonGetOTP.setVisibility(View.INVISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + inputMobile.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            SendOTP.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonGetOTP.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                        Toast.makeText(SendOTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonGetOTP.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                                    intent.putExtra("mobile", inputMobile.getText().toString());
                                    intent.putExtra("verificationId" , verificationId);
                                    startActivity(intent);
                                }
                            }
                    );

                }
            }
        });
    }
}