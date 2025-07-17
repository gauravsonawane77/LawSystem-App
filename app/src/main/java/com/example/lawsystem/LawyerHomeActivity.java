package com.example.lawsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LawyerHomeActivity extends AppCompatActivity {
    TextView textViewName,textViewEmail,textViewlogout;
    RecyclerView recyclerView;
    ArrayList<UserAppointment> userAppointments;
    UserAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lawyer_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewlogout = findViewById(R.id.textViewlogout);
        textViewlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPreferences.saveUserInfo(LawyerHomeActivity.this, null, null,null,null);
                startActivity(new Intent(LawyerHomeActivity.this, loginActivity.class));
                finish();
            }
        });
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewName = findViewById(R.id.textViewName);
        String[] userInfo = MyPreferences.getUserInfo(LawyerHomeActivity.this);
        String name = userInfo[0];
        String email = userInfo[1];
        textViewEmail.setText(email);
        textViewName.setText(name);


        recyclerView = findViewById(R.id.recyclerLawyerAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAppointments = new ArrayList<>();
        adapter = new UserAppointmentAdapter(userAppointments);
        recyclerView.setAdapter(adapter);

        fetchUserAppointments();
    }

    private void fetchUserAppointments() {
        String[] userInfo = MyPreferences.getUserInfo(LawyerHomeActivity.this);
        String id = userInfo[2];
        String url = constant.API_BASE_URL+"lawyer_appointments.php?lawyer_id="+id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("status")) {
                            JSONArray array = response.getJSONArray("appointments");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                UserAppointment appointment = new UserAppointment(
                                        obj.getString("id"),
                                        obj.getString("user_id"),
                                        obj.getString("lawyer_id"),
                                        obj.getString("appointment_time"),
                                        obj.getString("status"),
                                        obj.getString("appointment_type"),
                                        obj.getString("user_name"),
                                        obj.getString("user_email")
                                );
                                userAppointments.add(appointment);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}