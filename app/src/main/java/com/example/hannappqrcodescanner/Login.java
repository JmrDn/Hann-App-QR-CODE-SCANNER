package com.example.hannappqrcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;
@RequiresApi(api = Build.VERSION_CODES.O)
public class Login extends AppCompatActivity {
    AppCompatEditText emailET, passwordET;
    AppCompatButton loginBtn;
    ProgressBar progressBar;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidgets();
        passwordHideMethod();

        progressBar.setVisibility(View.GONE);

        loginBtn.setOnClickListener(v->{

            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
            String EMAIL = emailET.getText().toString();
            String PASSWORD = passwordET.getText().toString();
            boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches();

            if (EMAIL.isEmpty()){
                progressBar.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);

                emailET.setError("Enter email");
            }
            else if (!isEmailValid){
                progressBar.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
                emailET.setError("Enter valid email");
            }
            else if (PASSWORD.isEmpty()){
                progressBar.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
                passwordET.setError("Enter password");
            }
            else if (!EMAIL.isEmpty() && !PASSWORD.isEmpty()){
                checkAccount(EMAIL, PASSWORD, new OnAccountCheckListener() {

                    @Override
                    public void onAccountChecked(boolean isCorrect) {
                        if (isCorrect){
                            //Log in successfully
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");

                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        UserDetails userDetails = new UserDetails(Login.this);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String email = dataSnapshot.child("email").getValue(String.class);
                                            String ALIAS = dataSnapshot.child("alias").getValue(String.class);

                                            if (EMAIL.equals(email)){
                                                userDetails.setAlis(ALIAS);
                                                userDetails.setEmail(EMAIL);
                                                userDetails.setPassword(PASSWORD);
                                                Toast.makeText(getApplicationContext(), "Successfully log in", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("TAG", "User details storing error");
                                }
                            });


                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);
                            //Failed to log in
                            Toast.makeText(getApplicationContext(), "Email and password is incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void checkAccount(String email, String password, final OnAccountCheckListener listener) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isCorrect = false;
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String EMAIL = dataSnapshot.child("email").getValue(String.class);
                        String PASSWORD = dataSnapshot.child("pass").getValue(String.class);
                        if (email.equals(EMAIL) && password.equals(PASSWORD)) {
                            isCorrect = true;
                            break;
                        }
                    }
                }
                listener.onAccountChecked(isCorrect);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "Log in error");
                // Handle onCancelled if needed
            }
        });
    }

    // Define an interface for the callback
    interface OnAccountCheckListener {
        void onAccountChecked(boolean isCorrect);
    }

    @SuppressLint("CutPasteId")
    private void initWidgets() {
        emailET = findViewById(R.id.email_EditText);
        passwordET = findViewById(R.id.password_EditText);
        loginBtn = findViewById(R.id.login_Button);
        progressBar = findViewById(R.id.progressbar);
    }
    @SuppressLint("ClickableViewAccessibility")
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
    }

}