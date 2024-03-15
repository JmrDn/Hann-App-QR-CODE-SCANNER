package com.example.hannappqrcodescanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    AppCompatButton btn_scan;
    AppCompatButton historyBtn;

    Dialog scanResultDialog;
    Dialog sendRemarksDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_scan = findViewById(R.id.btn_scan);
        historyBtn = findViewById(R.id.history_Button);
        btn_scan.setOnClickListener(v -> scanCode());
        historyBtn.setOnClickListener(v1 ->{
            gotoHistoryActivity();
        });

        noInternetDialog();
    }

    private void gotoHistoryActivity() {
        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
    }

    private void scanCode() {
        noInternetDialog();
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up for flash down for off");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
    }

    private void noInternetDialog() {
        NoInternetDialogPendulum.Builder builder = new NoInternetDialogPendulum.Builder(
                this,
                getLifecycle()
        );

        DialogPropertiesPendulum properties = builder.getDialogProperties();



        properties.setCancelable(false); // Optional
        properties.setNoInternetConnectionTitle("No Internet"); // Optional
        properties.setNoInternetConnectionMessage("Check your Internet connection and try again"); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText("Please turn on"); // Optional
        properties.setWifiOnButtonText("Wifi"); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        builder.build();
    }


    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {

            //Get value of qrcode
            String itemChild = result.getContents();
            showResultDialog(itemChild);
            setScannedHistory(itemChild);

        }
    });
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setScannedHistory(String itemChild) {

        if (!itemChild.contains(".") &&
                !itemChild.contains("#") &&
                !itemChild.contains("$") &&
                !itemChild.contains("[") &&
                !itemChild.contains("]")){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("item").child(itemChild);

            db.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String amount =snapshot.child("amount").getValue().toString();
                    String category =snapshot.child("category").getValue().toString();
                    String brand =snapshot.child("brand").getValue().toString();
                    String condition = snapshot.child("condition").getValue().toString();
                    String date = snapshot.child("date").getValue().toString();
                    String employee = snapshot.child("employee").getValue().toString();
                    String item = snapshot.child("item").getValue().toString();
                    String property = snapshot.child("property").getValue().toString();
                    String serial = snapshot.child("serial").getValue().toString();
                    String remarks = snapshot.child("remarks").getValue().toString();

                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy H:m:s");
                    String scannedDate = now.format(dateTimeFormatter);


                    DatabaseReference historyDB = FirebaseDatabase.getInstance().getReference("history").child(itemChild);

                    historyDB.child("amount").setValue(amount);
                    historyDB.child("category").setValue(category);
                    historyDB.child("brand").setValue(brand);
                    historyDB.child("condition").setValue(condition);
                    historyDB.child("date").setValue(date);
                    historyDB.child("employee").setValue(employee);
                    historyDB.child("item").setValue(item);
                    historyDB.child("property").setValue(property);
                    historyDB.child("serial").setValue(serial);
                    historyDB.child("remarks").setValue(remarks);
                    historyDB.child("amount").setValue(amount);
                    historyDB.child("scannedDate").setValue(scannedDate);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", error.getMessage());
                }
            });
        }


    }

    private void showResultDialog(String itemChild) {

        scanResultDialog = new Dialog(this);
        scanResultDialog.setContentView(R.layout.scan_result_dialog);
        scanResultDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scanResultDialog.setCancelable(false);
        scanResultDialog.show();

        TextView itemTV, amountTV, brandTV,
                categoryTV, conditionTV, dateTV,
                employeeTV, propertyTV,serialTV, remarksTV;

        AppCompatButton okBtn, remarksBtn;

        //Initializing Widgets
        itemTV = scanResultDialog.findViewById(R.id.item_Textview);
        amountTV = scanResultDialog.findViewById(R.id.amount_Textview);
        brandTV = scanResultDialog.findViewById(R.id.brand_Textview);
        categoryTV = scanResultDialog.findViewById(R.id.category_Textview);
        conditionTV = scanResultDialog.findViewById(R.id.condition_Textview);
        employeeTV = scanResultDialog.findViewById(R.id.employee_Textview);
        propertyTV = scanResultDialog.findViewById(R.id.property_Textview);
        serialTV = scanResultDialog.findViewById(R.id.serial_Textview);
        dateTV = scanResultDialog.findViewById(R.id.date_Textview);
        remarksTV = scanResultDialog.findViewById(R.id.remarks_Textview);

        okBtn = scanResultDialog.findViewById(R.id.ok_Button);
        remarksBtn = scanResultDialog.findViewById(R.id.remarks_Button);

        if (!itemChild.contains(".") &&
                !itemChild.contains("#") &&
                !itemChild.contains("$") &&
                !itemChild.contains("[") &&
                !itemChild.contains("]")){

            //Get item Details
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("item").child(itemChild);

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String amount ="Amount: " + snapshot.child("amount").getValue().toString();
                        String category ="Category: " + snapshot.child("category").getValue().toString();
                        String brand ="Brand: " + snapshot.child("brand").getValue().toString();
                        String condition ="Condition: " + snapshot.child("condition").getValue().toString();
                        String date = "Date: " +snapshot.child("date").getValue().toString();
                        String employee = "Employee: " +snapshot.child("employee").getValue().toString();
                        String item = "Item: " + snapshot.child("item").getValue().toString();
                        String property ="Property: " + snapshot.child("property").getValue().toString();
                        String serial = "Serial: " +snapshot.child("serial").getValue().toString();
                        String remarks = "Remarks: " + snapshot.child("remarks").getValue().toString();

                        itemTV.setText(item);
                        amountTV.setText(amount);
                        categoryTV.setText(category);
                        brandTV.setText(brand);
                        conditionTV.setText(condition);
                        dateTV.setText(date);
                        employeeTV.setText(employee);
                        propertyTV.setText(property);
                        serialTV.setText(serial);
                        remarksTV.setText(remarks);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        scanResultDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", "Item info failed to retrieve " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    scanResultDialog.dismiss();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            scanResultDialog.dismiss();
        }

        okBtn.setOnClickListener(v->{
            scanResultDialog.dismiss();
        });

        remarksBtn.setOnClickListener(v1->{
            scanResultDialog.dismiss();
            showSendRemarksDialog(itemChild);
        });





    }

    private void showSendRemarksDialog(String itemChild) {

        sendRemarksDialog = new Dialog(this);
        sendRemarksDialog.setContentView(R.layout.remarks_dialog);
        sendRemarksDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sendRemarksDialog.setCancelable(false);
        sendRemarksDialog.show();

        EditText remarksET;
        AppCompatButton cancelBtn, sendRemarksBtn;

        remarksET = sendRemarksDialog.findViewById(R.id.remarks_EditText);
        cancelBtn = sendRemarksDialog.findViewById(R.id.cancel_Button);
        sendRemarksBtn = sendRemarksDialog.findViewById(R.id.sendRemarks_Button);



        sendRemarksBtn.setOnClickListener(v->{
            String remarks = remarksET.getText().toString();
            String date = DateAndTimeUtils.getDate();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("item").child(itemChild);

            db.child("remarks").setValue(remarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    db.child("remdate").setValue(date);
                    Toast.makeText(getApplicationContext(), "Remarks send Successfully", Toast.LENGTH_LONG).show();
                    sendRemarksDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Remarks failed to send " + e.getMessage(), Toast.LENGTH_LONG).show();
                    sendRemarksDialog.dismiss();
                }
            });
        });

        cancelBtn.setOnClickListener(v1->{
            sendRemarksDialog.dismiss();
        });

    }

    private void showRemarksDialog() {
        // Create an input dialog for remarks
        AlertDialog.Builder remarkDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        remarkDialogBuilder.setTitle("Add Remarks");

        // Set up the input
        final EditText input = new EditText(MainActivity.this);
        remarkDialogBuilder.setView(input);

        // Set up the buttons
        remarkDialogBuilder.setPositiveButton("Send Remarks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String remarks = input.getText().toString().trim();

                // Send remarks to admin email (you need to implement this logic)
                sendRemarksToAdmin(remarks);

                // Display a confirmation message
                showConfirmationDialog("Remarks Sent", "Remarks sent successfully!");
            }
        });

        remarkDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        remarkDialogBuilder.show();
    }

    private void sendRemarksToAdmin(String remarks) {
        // Implement the logic to send remarks to the admin email
        // You might want to use an email library or an API to send emails
        // For simplicity, you can print the remarks to the console for now
        System.out.println("Remarks: " + remarks);
        // Add your logic here to send the remarks to the admin email
    }

    private void showConfirmationDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
