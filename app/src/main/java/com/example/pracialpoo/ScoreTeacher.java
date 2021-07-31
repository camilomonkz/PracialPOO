package com.example.pracialpoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class ScoreTeacher extends AppCompatActivity {

    private TextView tvNickname,tvDate,tvContent;
    private EditText etScore;
    private FirebaseFirestore firebaseFirestore;
    private Button setScoreBtn,backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_teacher);

        Bundle takeObject = getIntent().getExtras();

        Comment comment = null;

        if(takeObject != null){
            comment = (Comment) takeObject.getSerializable("comment");
        }

        Comment finalComment = comment;
        String nickname = getIntent().getExtras().getString("nickname");
        String id = getIntent().getExtras().getString("id");
        String type = getIntent().getExtras().getString("type");

        firebaseFirestore = FirebaseFirestore.getInstance();

        tvContent = (TextView)findViewById(R.id.tvContentT);
        tvDate = (TextView)findViewById(R.id.tvDateT);
        tvNickname = (TextView)findViewById(R.id.tvNicknameT);

        etScore = (EditText)findViewById(R.id.etScoreT);

        tvNickname.setText(finalComment.getNickname());
        tvContent.setText(finalComment.getContent());
        tvDate.setText(finalComment.getDate());

        setScoreBtn =(Button)findViewById(R.id.scoreBtnT);

        setScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringScore = etScore.getText().toString().trim();
                if(!stringScore.isEmpty()) {
                    Teacher teacher = new Teacher();
                    teacher.setScore(finalComment, finalComment.getDate(),Integer.parseInt(stringScore), getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(),CommentView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname",nickname);
                    bundle.putString("type",type);
                    bundle.putString("id",id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"No hay puntuación",Toast.LENGTH_SHORT).show();
                }

            }
        });

        backBtn = (Button)findViewById(R.id.backBtnT);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CommentView.class);
                Bundle bundle = new Bundle();
                bundle.putString("nickname",nickname);
                bundle.putString("type",type);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
