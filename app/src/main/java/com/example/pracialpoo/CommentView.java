package com.example.pracialpoo;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;

public class CommentView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListofCommnets listofCommnets;
    private FirebaseFirestore firebaseFirestore;
    private FloatingActionButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);

        String nickname = getIntent().getExtras().getString("nickname");
        String id = getIntent().getExtras().getString("id");
        String type = getIntent().getExtras().getString("type");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("comments");

        FirestoreRecyclerOptions<Comment> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Comment>()
                .setQuery(query,Comment.class)
                .build();

        listofCommnets = new ListofCommnets(firestoreRecyclerOptions,type,nickname,id);
        listofCommnets.notifyDataSetChanged();
        recyclerView.setAdapter(listofCommnets);

        backBtn = (FloatingActionButton)findViewById(R.id.fBtnBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("estudiante")){
                    Intent intent = new Intent(getApplicationContext(),StudentMain.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname",nickname);
                    bundle.putString("type",type);
                    bundle.putString("id",id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(),TeacherMain.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname",nickname);
                    bundle.putString("type",type);
                    bundle.putString("id",id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        listofCommnets.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listofCommnets.stopListening();
    }
}