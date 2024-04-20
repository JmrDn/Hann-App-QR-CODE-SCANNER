package com.example.hannappqrcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressLint("ClickableViewAccessibility")
public class ChangePasswordActivityStep2 extends AppCompatActivity {
    AppCompatEditText passwordET, confirmPasswordET;
    AppCompatButton enterBtn;
    ProgressBar progressBar;
    boolean passwordVisible;
    boolean confirmPasswordVisible;




    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_step2);
        initWidgets();
        passwordHideMethod();
        progressBar.setVisibility(View.GONE);
        enterBtn.setEnabled(false);
        enterBtn.setBackgroundColor(Color.LTGRAY);

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String PASSWORD = s.toString();
                if (PASSWORD.isEmpty()){
                    enterBtn.setEnabled(false);
                    enterBtn.setBackgroundColor(Color.LTGRAY);
                }
                else{

                    confirmPasswordET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String CONFIRM_PASSWORD = s.toString();
                            if (CONFIRM_PASSWORD.isEmpty()){
                                enterBtn.setEnabled(false);
                                enterBtn.setBackgroundColor(Color.LTGRAY);
                            }
                            else{
                                enterBtn.setEnabled(true);
                                enterBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        enterBtn.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            enterBtn.setVisibility(View.GONE);
            String PASSWORD = passwordET.getText().toString();
            String CONFIRM_PASSWORD = confirmPasswordET.getText().toString();

            if (!PASSWORD.equals(CONFIRM_PASSWORD)){
                progressBar.setVisibility(View.GONE);
                enterBtn.setVisibility(View.VISIBLE);
                passwordET.setError("Password not match");
                confirmPasswordET.setError("Password not match");
            }
            else {
                changePassword(CONFIRM_PASSWORD);
            }
        });

    }

    private void changePassword(String password) {
        UserDetails userDetails = new UserDetails(ChangePasswordActivityStep2.this);
        userDetails.setPassword(password);
        String email = userDetails.getEmail();

        if (email != null){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");

            db.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            String emailFromDB = dataSnapshot.child("email").getValue(String.class);


                            if (emailFromDB.equals(email)){
                                String childName = dataSnapshot.getKey();
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference("User").child(childName);
                                db.child("pass").setValue(password);
                                Log.d("TAG", emailFromDB + " " + "Password changed");
                                Toast.makeText(getApplicationContext(), "Password change successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                break;

                            }

                        }
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        enterBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Failed to change password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", error.getMessage());
                }
            });
        }


    }

    private void initWidgets() {
        passwordET = findViewById(R.id.password_EditText);
        confirmPasswordET = findViewById(R.id.confirmPassword_EditText);
        enterBtn = findViewById(R.id.enter_Button);
        progressBar  = findViewById(R.id.progressbar);
    }


    private void passwordHideMethod() {

        passwordET.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int Right = 2;

                if (motionEvent.getAction()== MotionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>= passwordET.getRight()-passwordET.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = passwordET.getSelectionEnd();
                        if (passwordVisible){
                            //set drawable image here
                            passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_off_24, 0);
                            // for hide passwordET
                            passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }
                        else {

                            //set drawable image here
                            passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_24, 0);
                            // for show password
                            passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;

                        }
                        passwordET.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        confirmPasswordET.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int Right = 2;

                if (motionEvent.getAction()== MotionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>= confirmPasswordET.getRight()-confirmPasswordET.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = confirmPasswordET.getSelectionEnd();
                        if (confirmPasswordVisible){
                            //set drawable image here
                            confirmPasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_off_24, 0);
                            // for hide passwordET
                            confirmPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            confirmPasswordVisible = false;
                        }
                        else {

                            //set drawable image here
                            confirmPasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_24, 0);
                            // for show password
                            confirmPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            confirmPasswordVisible = true;

                        }
                        confirmPasswordET.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }


}