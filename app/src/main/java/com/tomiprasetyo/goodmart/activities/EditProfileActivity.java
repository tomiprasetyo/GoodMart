package com.tomiprasetyo.goodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tomiprasetyo.goodmart.R;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText txtNameEdit,txtAddressEdit,txtPhoneEdit;
    ImageView imageEdit;
    FirebaseFirestore dbroot;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Profile");

        txtNameEdit = findViewById(R.id.txtNameEdit);
        txtAddressEdit = findViewById(R.id.txtAddressEdit);
        txtPhoneEdit = findViewById(R.id.txtPhoneEdit);
        imageEdit = findViewById(R.id.imageEdit);
        updateButton = findViewById(R.id.updateButton);

        dbroot = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Data updating to firestore....

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
                pd.setMessage("Updating...");
                pd.show();

                String name = txtNameEdit.getText().toString();
                String address = txtAddressEdit.getText().toString();
                String phone = txtPhoneEdit.getText().toString();
                String email = user.getEmail();

                HashMap hashMap = new HashMap();
                hashMap.put("Name",name);
                hashMap.put("Address",address);
                hashMap.put("Phone",phone);

                dbroot.collection("goodmart").document(email)
                        .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                pd.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("address",address);
                                intent.putExtra("phone",phone);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Failed Update Profile", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }


    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}