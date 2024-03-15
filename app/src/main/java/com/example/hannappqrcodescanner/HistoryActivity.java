package com.example.hannappqrcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.hannappqrcodescanner.Adapter.HistoryAdapter;
import com.example.hannappqrcodescanner.Model.HistoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
@RequiresApi(api = Build.VERSION_CODES.O)
public class HistoryActivity extends AppCompatActivity {
     RecyclerView recyclerView;
    ArrayList<HistoryModel> list;
     HistoryAdapter myAdapter;
     Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initWidgets();
        setUpToolbar();
        setUpRecyclerView();
        setData();



    }

    private void setUpRecyclerView() {
        list = new ArrayList<>();
        myAdapter = new HistoryAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);


    }

    private void setData() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy H:m:s");
        String dateNow = now.format(dateTimeFormatter);


        DatabaseReference db = FirebaseDatabase.getInstance().getReference("history");

        db.orderByChild("scannedDate").endAt(dateNow).addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            String amount ="Amount: " + getStringValue(dataSnapshot, "amount");
                            String category ="Category: " + getStringValue(dataSnapshot, "category");
                            String brand ="Brand: " + getStringValue(dataSnapshot, "brand");
                            String condition ="Condition: " + getStringValue(dataSnapshot, "condition");
                            String date = "Date: " + getStringValue(dataSnapshot, "date");
                            String employee = "Employee: " + getStringValue(dataSnapshot, "employee");
                            String item = "Item: " + getStringValue(dataSnapshot, "item");
                            String property ="Property: " + getStringValue(dataSnapshot, "property");
                            String serial = "Serial: " + getStringValue(dataSnapshot, "serial");
                            String remarks = "Remarks: " + getStringValue(dataSnapshot, "remarks");
                            String scannedDate = getStringValue(dataSnapshot, "scannedDate");
                            String remDate = getStringValue(dataSnapshot, "remdate");

                            if (!scannedDate.isEmpty()){

                                scannedDate = DateAndTimeUtils.convertTimeStampFormatToDateAndTime(scannedDate);
                            }


                            if (!amount.isEmpty() &&
                                    !category.isEmpty() &&
                                    !brand.isEmpty() &&
                                    !condition.isEmpty() &&
                                    !date.isEmpty() &&
                                    !employee.isEmpty() &&
                                    !item.isEmpty() &&
                                    !property.isEmpty() &&
                                    !serial.isEmpty() &&
                                    !remarks.isEmpty() &&
                                    !scannedDate.isEmpty() &&
                                    !remarks.isEmpty()){

                                if (remDate.equals("----/--/--"))
                                    remDate = "";

                                list.add(new HistoryModel(amount,category,brand, condition,
                                        date, employee, item, property, serial,
                                        remarks, scannedDate,remDate));
                            }


                        }
                    }

                    if (myAdapter != null)
                        myAdapter.notifyDataSetChanged();


                }
                else {
                    list.clear();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });

        refresh(1000);
    }

    private void refresh(int milliseconds) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, milliseconds);
    }

    private String getStringValue(DataSnapshot dataSnapshot, String childKey) {
        Object value = dataSnapshot.child(childKey).getValue();
        return (value != null) ? value.toString() : "";
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeActionContentDescription("Back");
    }

    private void initWidgets() {
        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        
        
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}