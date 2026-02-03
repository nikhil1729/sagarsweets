package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.TeamModel;
import com.sagarsweets.in.R;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamVH> {

    private Context context;
    private List<TeamModel> teamList;

    public TeamAdapter(Context context, List<TeamModel> teamList) {
        this.context = context;
        this.teamList = teamList;
    }

    @NonNull
    @Override
    public TeamVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_team_member, parent, false);
        return new TeamVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamVH holder, int position) {
        TeamModel team = teamList.get(position);

        holder.tvName.setText(team.getName());
        holder.tvDesignation.setText(team.getDesignation());
        holder.tvAbout.setText(team.getAboutHim());
        Log.d("imageProfile","image path- "+team.getProfilePic());
        Glide.with(context)
                .load(SuperController.base_url_images + team.getProfilePic())
                .placeholder(R.drawable.ic_user)
                .into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    static class TeamVH extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvName, tvDesignation,tvAbout;

        TeamVH(View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.imgProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesignation = itemView.findViewById(R.id.tvDesignation);
            tvAbout = itemView.findViewById(R.id.tvAbout);
        }
    }
}

