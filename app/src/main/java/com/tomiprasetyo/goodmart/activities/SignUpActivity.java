package com.tomiprasetyo.goodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tomiprasetyo.goodmart.R;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText txtEmail,txtPassword;
    private Button singUpButton;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Objects.requireNonNull(getSupportActionBar()).hide();

        txtEmail = findViewById(R.id.txtAddressEdit);
        txtPassword = findViewById(R.id.txtPhoneEdit);
        singUpButton = findViewById(R.id.updateButton);

        auth = FirebaseAuth.getInstance();

        singUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String txt_email = txtEmail.getText().toString();
                String txt_password  = txtPassword.getText().toString();

                Log.i("goodmart",txt_email+txt_password);

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) ){
                    pd.dismiss();
                    Toast.makeText(SignUpActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.length()<6){
                    pd.dismiss();
                    Toast.makeText(SignUpActivity.this, "Password is too short!", Toast.LENGTH_SHORT).show();
                }

                else{
                    registerUser(txt_email,txt_password,pd);
                }
            }
        });
    }




    private void registerUser(String email, String password, ProgressDialog pd) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()) {
                    pd.dismiss();
                    Toast.makeText(SignUpActivity.this, "Sing Up Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}