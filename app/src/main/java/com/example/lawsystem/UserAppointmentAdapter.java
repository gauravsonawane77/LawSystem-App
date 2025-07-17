package com.example.lawsystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.ViewHolder> {
    private final List<UserAppointment> appointments;

    public UserAppointmentAdapter(List<UserAppointment> appointments) {
        this.appointments = appointments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName, textUserEmail, textUserType, textUserTime, textUserStatus;
        Button btnUserChat,btnUpdateStatus;
        Spinner spinnerStatus;

        public ViewHolder(View view) {
            super(view);
            textUserName = view.findViewById(R.id.textUserName);
            textUserEmail = view.findViewById(R.id.textUserEmail);
            textUserType = view.findViewById(R.id.textUserType);
            textUserTime = view.findViewById(R.id.textUserTime);
            textUserStatus = view.findViewById(R.id.textUserStatus);
            btnUserChat = view.findViewById(R.id.btnUserChat);
            spinnerStatus = view.findViewById(R.id.spinnerStatus);
            btnUpdateStatus = view.findViewById(R.id.btnUpdateStatus);
        }
    }

    @NonNull
    @Override
    public UserAppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_card_lawyer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserAppointment a = appointments.get(position);
        holder.textUserName.setText(a.userName);
        holder.textUserEmail.setText(a.userEmail);
        holder.textUserType.setText("Type: " + a.appointmentType);
        holder.textUserTime.setText("Time: " + a.appointmentTime);
        holder.textUserStatus.setText("Status: " + a.status);

        if (a.appointmentType.equalsIgnoreCase("online")) {
            holder.btnUserChat.setVisibility(View.VISIBLE);
            holder.btnUserChat.setEnabled(a.status.equalsIgnoreCase("approved"));
            holder.btnUserChat.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), ChatActivity.class);
                i.putExtra("userID",a.getUserId());
                v.getContext().startActivity(i);
            });
        } else {
            holder.btnUserChat.setVisibility(View.GONE);
        }

        holder.spinnerStatus.setSelection(getStatusIndex(a.status));  // Set current status
        holder.btnUpdateStatus.setOnClickListener(v -> {
            String selectedStatus = holder.spinnerStatus.getSelectedItem().toString();
            updateAppointmentStatus(v.getContext(), a.id, selectedStatus, () -> {
                a.status = selectedStatus;
                holder.textUserStatus.setText("Status: " + selectedStatus);
                Toast.makeText(v.getContext(), "Status updated!", Toast.LENGTH_SHORT).show();
            });
        });

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    private int getStatusIndex(String status) {
        switch (status.toLowerCase()) {
            case "approved": return 1;
            case "rejected": return 2;
            default: return 0;
        }
    }

    private void updateAppointmentStatus(Context context, String appointmentId, String status, Runnable onSuccess) {
        String url = constant.API_BASE_URL+"appointment.php";

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("appointment_id", appointmentId);
            jsonBody.put("status", status);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                    response -> {
                        if (response.optBoolean("status", false)) {
                            onSuccess.run();
                        } else {
                            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(context).add(request);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
