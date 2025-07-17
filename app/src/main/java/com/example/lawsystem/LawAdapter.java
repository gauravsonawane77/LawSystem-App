package com.example.lawsystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LawAdapter extends RecyclerView.Adapter<LawAdapter.LawViewHolder> {

    private Context context;
    private List<Law> lawList;

    public LawAdapter(Context context, List<Law> lawList) {
        this.context = context;
        this.lawList = lawList;
    }

    @NonNull
    @Override
    public LawViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_law, parent, false);
        return new LawViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LawViewHolder holder, int position) {
        Law law = lawList.get(position);
        holder.textViewLawName.setText(law.getSection()+": "+law.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = law.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lawList.size();
    }

    public class LawViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLawName;


        public LawViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLawName = itemView.findViewById(R.id.textViewLawName);
        }
    }
}
