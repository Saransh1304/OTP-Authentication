package com.example.personall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class FirebasePhoneActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etPhone, etOtp;
    Button btSendOtp, btResendOtp, btVerifyOtp;

    //Add this on top where other variables are declared
    PhoneAuthProvider.ForceResendingToken mResendToken;

    //Add this on top where other variables are declared
    String mVerificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_phone);
        initFields();

        //Add it in the onCreate method, after calling method initFields()
        mAuth = FirebaseAuth.getInstance();
    }


    void initFields() {
        etPhone = findViewById(R.id.et_phone);
        etOtp = findViewById(R.id.et_otp);
        btSendOtp = findViewById(R.id.bt_send_otp);
        btResendOtp = findViewById(R.id.bt_resend_otp);
        btVerifyOtp = findViewById(R.id.bt_verify_otp);
        btResendOtp.setOnClickListener(this);
        btVerifyOtp.setOnClickListener(this);
        btSendOtp.setOnClickListener(this);
    }
    //Add it below the lines where you declared the fields
    private FirebaseAuth mAuth;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_send_otp:
                String phn = "+91" + etPhone.getText().toString();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phn)    // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks7
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
                break;
            case R.id.bt_resend_otp:
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        etPhone.getText().toString(),        // Phone number to verify
                        1  ,               // Timeout duration
                        TimeUnit.MINUTES,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mCallbacks,         // OnVerificationStateChangedCallbacks
                        mResendToken);             // Force Resending Token from callbacks
                break;
            case R.id.bt_verify_otp:
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, etOtp.getText().toString());
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(FirebasePhoneActivity.this, "Verification Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(FirebasePhoneActivity.this, "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                break;

        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    ///Add this method below auth initialization in the onCreate method.
    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(FirebasePhoneActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(FirebasePhoneActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }
            String mVerificationId;
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(FirebasePhoneActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };







    }
}
