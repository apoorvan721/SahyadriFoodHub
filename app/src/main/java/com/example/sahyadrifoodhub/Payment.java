package com.example.sahyadrifoodhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Payment extends AppCompatActivity {
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    ImageView googlepay;
    EditText pay_amount;
    String amt;
    Uri uri;
    String name="Nagesh Poojary";
    String upiId="nageshpoojary4507@oksbi";
    String transactionNote="pay test";
    String status;

    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "upiID")
                .appendQueryParameter("pn", "name")
                .appendQueryParameter("tn", "transactionNote")
                .appendQueryParameter("am", "amount")
                .appendQueryParameter("cu", "INR")
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        googlepay = findViewById(R.id.googlePayButton);
        pay_amount = findViewById(R.id.note_amount);

        googlepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amt = pay_amount.getText().toString();
                if (!amt.isEmpty()) {
                    uri = getUpiPaymentUri(name, upiId, transactionNote, amt);
                    payWithGPay();
                } else {
                    pay_amount.setError("Amount is required!");
                    pay_amount.requestFocus();
                }

            }
        });
    }
    private void payWithGPay () {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(Payment.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }

        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                status = data.getStringExtra("Status").toLowerCase();
            }


            if ((RESULT_OK == resultCode) && status.equals("success")) {
                Toast.makeText(Payment.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Payment.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }



