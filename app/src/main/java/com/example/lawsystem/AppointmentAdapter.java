package com.example.lawsystem;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private final List<Appointment> appointments;

    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textLawyerName, textEmail, textType, textTime, textStatus,textContact;
        Button btnChat;

        public ViewHolder(View view) {
            super(view);
            textLawyerName = view.findViewById(R.id.textLawyerName);
            textEmail = view.findViewById(R.id.textEmail);
            textType = view.findViewById(R.id.textType);
            textTime = view.findViewById(R.id.textTime);
            textStatus = view.findViewById(R.id.textStatus);
            btnChat = view.findViewById(R.id.btnChat);
            textContact = view.findViewById(R.id.textContact);
        }
    }

    @NonNull
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Appointment a = appointments.get(position);
        holder.textLawyerName.setText(a.getLawyerName());
        holder.textEmail.setText(a.getLawyerEmail());
        holder.textType.setText("Type: " + a.getAppointmentType());
        holder.textTime.setText("Time: " + a.getAppointmentTime());
        holder.textStatus.setText("Status: " + a.getStatus());

        if (a.appointmentType.equalsIgnoreCase("online")) {
            holder.btnChat.setVisibility(View.VISIBLE);
            holder.btnChat.setEnabled(a.status.equalsIgnoreCase("approved"));
            holder.btnChat.setOnClickListener(v -> {
                // Launch chat or do something
                Intent i = new Intent(v.getContext(), ChatActivity.class);
                i.putExtra("lawyerID",a.getLawyerId());
                v.getContext().startActivity(i);
            });
        } else {
            holder.btnChat.setVisibility(View.GONE);
            holder.textContact.setText("Contact: " + a.getLawyer_contact());
            holder.textContact.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
}
