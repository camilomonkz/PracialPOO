package com.example.pracialpoo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class StudentMain extends AppCompatActivity {

    private TextView tvTitle,tvNickname,tvName,tvLastname;
    private Button commentBtn,myCommentsBtn, allComentsBtn,logOut;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        String nickname = getIntent().getExtras().getString("nickname");
        String id = getIntent().getExtras().getString("id");
        String type = getIntent().getExtras().getString("type");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        tvTitle = (TextView)findViewById(R.id.tvTitleStudent);
        tvNickname = (TextView)findViewById(R.id.tvNicknameStudent);
        tvName = (TextView)findViewById(R.id.tvNameStudent);
        tvLastname = (TextView)findViewById(R.id.tvLastnameStudent);


        DocumentReference documentReference;
        documentReference = firebaseFirestore
                .collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid());

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvTitle.setText(value.getString("userType"));
                tvNickname.setText(value.getString("nickname"));
                tvName.setText(value.getString("name"));
                tvLastname.setText(value.getString("lastname"));
            }
        });

        commentBtn = (Button)findViewById(R.id.commentBtnStudent);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SetComments.class);
                Bundle bundle = new Bundle();
                bundle.putString("nickname",nickname);
                bundle.putString("type",type);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        allComentsBtn = (Button)findViewById(R.id.allCommentsStudent);

        allComentsBtn.setOnClickListener(new View.OnClickListener() {
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

        myCommentsBtn = (Button)findViewById(R.id.myCommentsBtnStudent);

        myCommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyCommentView.class);
                Bundle bundle = new Bundle();
                bundle.putString("nickname",nickname);
                bundle.putString("type",type);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        logOut = (Button)findViewById(R.id.logOutBtnStudent);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}