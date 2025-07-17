package com.example.lawsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LawyerAdapter extends RecyclerView.Adapter<LawyerAdapter.LawyerViewHolder> {

    private Context context;
    private List<Lawyer> lawyerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LawyerAdapter(Context context, List<Lawyer> lawyerList) {
        this.context = context;
        this.lawyerList = lawyerList;
    }

    @NonNull
    @Override
    public LawyerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_lawyer, parent, false);
        return new LawyerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LawyerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Lawyer lawyer = lawyerList.get(position);
        holder.textViewName.setText(lawyer.getName());
        holder.textViewExpertise.setText(lawyer.getExpertise());
        // Load image using Picasso
        Picasso.get().load(lawyer.getPhoto()).placeholder(R.drawable.lawyer).into(holder.imageViewLawyer);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        holder.iconAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppointmentsManageActivity.class);
                intent.putExtra("lawyer_id",lawyer.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lawyerList.size();
    }

    public class LawyerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewExpertise;
        ImageView imageViewLawyer,iconAppointment;

        public LawyerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewExpertise = itemView.findViewById(R.id.textViewExpertise);
            imageViewLawyer = itemView.findViewById(R.id.imageViewLawyer);
            iconAppointment = itemView.findViewById(R.id.iconAppointment);
        }
    }
}
