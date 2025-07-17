package com.example.lawsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity {
    EditText etOtp;
    Button btnVerifyOtp;
    String email; // You'll receive this from previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = getIntent().getStringExtra("email");

        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            if (otp.isEmpty()) {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifyOtp(email, otp);
            }
        });
    }

    private void verifyOtp(String email, String otp) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = constant.API_BASE_URL+"verify_otp.php";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("otp", otp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Boolean status = response.getBoolean("status");
                        String message = response.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        if (status) {
                            Toast.makeText(this, "OTP Verified, Please login.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OTPActivity.this,loginActivity.class));
                            finish();
                        }else{
                            Toast.makeText(this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    String errMsg = "Error verifying OTP";
                    if (error.networkResponse != null) {
                        errMsg += ": " + error.networkResponse.statusCode;
                    }
                    Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
                }
        );

//        request.setRetryPolicy(new DefaultRetryPolicy(100000, 1, 1.0f));
        Volley.newRequestQueue(this).add(request);
    }
}