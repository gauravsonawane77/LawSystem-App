package com.example.lawsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CriminalAdapter extends RecyclerView.Adapter<CriminalAdapter.CriminalViewHolder> {

    private Context context;
    private List<Criminal> criminalList;

    public CriminalAdapter(Context context, List<Criminal> criminalList) {
        this.context = context;
        this.criminalList = criminalList;
    }

    @NonNull
    @Override
    public CriminalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_criminal, parent, false);
        return new CriminalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CriminalViewHolder holder, int position) {
        Criminal criminal = criminalList.get(position);
        holder.textViewFullName.setText(criminal.getFullName());
        holder.textViewDescription.setText(criminal.getDescription());
        holder.textViewReward.setText(criminal.getReward());

        // Load image using Picasso
        Picasso.get().load(criminal.getPhotoId()).placeholder(R.drawable.robber).into(holder.imageViewCriminal);
    }

    @Override
    public int getItemCount() {
        return criminalList.size();
    }

    public static class CriminalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCriminal;
        TextView textViewFullName;
        TextView textViewDescription;
        TextView textViewReward;

        public CriminalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCriminal = itemView.findViewById(R.id.imageViewCriminal);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewReward = itemView.findViewById(R.id.textViewReward);
        }
    }
}

