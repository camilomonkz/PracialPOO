package com.example.pracialpoo;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListofCommnets extends FirestoreRecyclerAdapter<Comment,ListofCommnets.ViewHolder>{
    String type;
    String nickname;
    String id;
    public ListofCommnets(@NonNull FirestoreRecyclerOptions<Comment> options,String type,String nickname,String id) {
        super(options);
        this.type = type;
        this.id = id;
        this.nickname = nickname;
    }

    @Override
    protected void onBindViewHolder(@NonNull ListofCommnets.ViewHolder holder, int position, @NonNull Comment model) {
        holder.tvNickname.setText(model.getNickname());
        holder.tvDate.setText(model.getDate());
        holder.tvContent.setText(model.getContent());
        holder.tvScore.setText(String.valueOf(model.getScore()/ model.getNumber()));
        if(type.equals("profesor")){
            Comment comment = new Comment(model.getContent()
                    , model.getDate()
                    , model.getScore()
                    , model.getNumber()
                    , model.getNickname());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(),ScoreTeacher.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("comment",comment);
                    bundle.putString("nickname",nickname);
                    bundle.putString("type",type);
                    bundle.putString("id",id);
                    intent.putExtras(bundle);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_coment, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNickname, tvDate,tvContent,tvScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNickname = itemView.findViewById(R.id.tvNicknameElement);
            tvDate = itemView.findViewById(R.id.tvDateElement);
            tvContent = itemView.findViewById(R.id.tvCommentElement);
            tvScore = itemView.findViewById(R.id.tvScoreElement);
        }
    }
}



