package com.example.lawsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentsManageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Appointment> appointmentList;
    AppointmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointments_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button bookAppointmentBtn = findViewById(R.id.buttonBookAppointment);
        bookAppointmentBtn.setOnClickListener(v -> showAppointmentDialog());

        recyclerView = findViewById(R.id.recyclerAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList);
        recyclerView.setAdapter(adapter);

        loadAppointments();
    }

    private void showAppointmentDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book_appointment, null);
        DatePicker datePicker = dialogView.findViewById(R.id.datePickerAppointment);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupAppointmentType);

        new AlertDialog.Builder(this)
                .setTitle("Book Appointment")
                .setView(dialogView)
                .setPositiveButton("Book", (dialog, which) -> {
                    String[] userInfo = MyPreferences.getUserInfo(AppointmentsManageActivity.this);
                    String userId = userInfo[2];
                    Intent intent = getIntent();
                    String lawyerId = intent.getStringExtra("lawyer_id");

                    // Get selected date
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1; // Month is 0-based
                    int year = datePicker.getYear();
                    String selectedDate = String.format("%04d-%02d-%02d", year, month, day);

                    // Get selected appointment type
                    int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                    String appointmentType = null;
                    if (selectedRadioId != -1) {
                        RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioId); // ✅ fix here
                        appointmentType = selectedRadioButton.getText().toString();
                        Toast.makeText(this, "Date: " + selectedDate + "\nType: " + appointmentType, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please select an appointment type", Toast.LENGTH_SHORT).show();
                        return; // Stop processing if no type selected
                    }

                    String appointmentTime = selectedDate.trim();

                    try {
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("user_id", userId);
                        jsonBody.put("lawyer_id", lawyerId);
                        jsonBody.put("appointment_time", appointmentTime);
                        jsonBody.put("appointment_type", appointmentType);

                        sendAppointmentRequest(jsonBody);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sendAppointmentRequest(JSONObject requestBody) {
        String url = constant.API_BASE_URL + "appointment.php"; // Replace with your actual endpoint

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                response -> {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    private void loadAppointments() {
        String[] userInfo = MyPreferences.getUserInfo(AppointmentsManageActivity.this);
        String id = userInfo[2];
        String lid = getIntent().getStringExtra("lawyer_id");
        Toast.makeText(this, "user_id="+id, Toast.LENGTH_SHORT).show();

        String url = constant.API_BASE_URL+"appointment.php?user_id="+id+"&lawyer_id="+lid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("kalki", "loadAppointments: "+response);
                        if (response.getBoolean("status")) {
                            JSONArray array = response.getJSONArray("appointments");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                Appointment appointment = new Appointment(
                                        obj.getString("id"),
                                        obj.getString("user_id"),
                                        obj.getString("lawyer_id"),
                                        obj.getString("appointment_time"),
                                        obj.getString("status"),
                                        obj.getString("appointment_type"),
                                        obj.getString("lawyer_name"),
                                        obj.getString("lawyer_email"),
                                        obj.getString("lawyer_contact")
                                );
                                appointmentList.add(appointment);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

}