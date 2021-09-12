package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Telephony.Sms.Intents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity  {

    private final int REQUEST_PERMISSION_PHONE_CALL=1;

    TextView headerText;
    Button syncSettingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void initializeUI() {
        headerText = findViewById(R.id.textView);
        syncSettingsButton = findViewById(R.id.syncSettingsButton);
        syncSettingsButton.setOnClickListener(callButtonClickListener);
    }

    public void checkForTelephonyCallPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.ANSWER_PHONE_CALLS, REQUEST_PERMISSION_PHONE_CALL);
            } else {
                requestPermission(Manifest.permission.CALL_PHONE, REQUEST_PERMISSION_PHONE_CALL);
            }
        } else {
            dialPhoneNumber();
        }
    }

    View.OnClickListener callButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkForTelephonyCallPermission();
        }
    };

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_PHONE_CALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    dialPhoneNumber();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }


    // dial phone number
    public void dialPhoneNumber() {
        String encodedPhoneNumber = String.format("tel:%s", Uri.encode("##72786#"));
        Uri number = Uri.parse(encodedPhoneNumber);

//     TelephonyInte   android.provider.Telephony.Sms.Intents.SECRET_CODE_ACTION
//
//        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//        //Intent callIntent = new Intent(Intent.ACTION_CALL, number);
//        startActivity(callIntent);

        Intent intent = new Intent(Telephony.Sms.Intents.SECRET_CODE_ACTION, number);
        sendBroadcast(intent);


    }


}

