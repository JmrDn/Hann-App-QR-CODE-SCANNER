package com.example.hannappqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
@SuppressLint("ClickableViewAccessibility")
public class ChangePasswordActivity extends AppCompatActivity {
    AppCompatEditText passwordET;
    AppCompatButton confirmBtn;
    ProgressBar progressBar;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initWidgets();
        passwordHideMethod();
        progressBar.setVisibility(View.GONE);

        UserDetails userDetails = new UserDetails(ChangePasswordActivity.this);
        confirmBtn.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            confirmBtn.setVisibility(View.GONE);
            String PASSWORD = passwordET.getText().toString();

            if (!PASSWORD.equals(userDetails.getPassword())){
                progressBar.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
                passwordET.setError("Password is incorrect");
            }
            else{
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivityStep2.class));
            }

        });

    }

    private void initWidgets() {
        passwordET = findViewById(R.id.password_EditText);
        confirmBtn = findViewById(R.id.submit_Button);
        progressBar = findViewById(R.id.progressbar);
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
    }
}