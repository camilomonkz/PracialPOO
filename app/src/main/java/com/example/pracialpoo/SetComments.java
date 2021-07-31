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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SetComments extends AppCompatActivity {

    private EditText etComment;
    private Button commentBtn,backBtn;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_comments);

        String nickname = getIntent().getExtras().getString("nickname");
        String id = getIntent().getExtras().getString("id");
        String type = getIntent().getExtras().getString("type");

        Log.d("bundle",nickname+" "+id+" "+type);

        etComment = (EditText)findViewById(R.id.etCommentC);
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        DocumentReference documentReferenceUser = firebaseFirestore
                .collection("Users")
                .document(userId);

        DocumentReference documentReferenceComment = firebaseFirestore
                .collection("comments")
                .document();

        commentBtn = (Button)findViewById(R.id.commentBtnC);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if(!comment.isEmpty()){
                    documentReferenceUser.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            String nickname = value.get("nickname").toString();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.getDefault());
                            String formattedDate = dateFormat.format(Calendar.getInstance().getTime());

                            HashMap<Object,Object> commentInfo = new HashMap<>();

                            commentInfo.put("nickname",nickname);
                            commentInfo.put("content",comment);
                            commentInfo.put("date",formattedDate);
                            commentInfo.put("score",1);
                            commentInfo.put("number",1);

                            documentReferenceComment.set(commentInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Comentario exitoso", Toast.LENGTH_SHORT).show();
                                    etComment.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error en comentar", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });


                }else{
                    Toast.makeText(getApplicationContext(), "No hay comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn = (Button)findViewById(R.id.backBtnSet);
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
}