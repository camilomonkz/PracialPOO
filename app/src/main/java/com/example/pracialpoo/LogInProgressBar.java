package com.example.pracialpoo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LogInProgressBar extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String userID = user.getUid();

        DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(userID);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                documentReference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, FirebaseFirestoreException error) {
                        if(value.getString("userType").equals("estudiante")){

                            Student student = new Student(value.getString("name")
                                    ,value.getString("lastname")
                                    ,value.getString("nickname")
                                    ,user.getEmail()
                                    ,userID
                                    ,value.getString("userType"));

                            Intent intent = new Intent(getApplicationContext(),StudentMain.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("nickname",student.getNickname());
                            bundle.putString("type",student.getType());
                            bundle.putString("id",student.getId());

                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }else{

                            Teacher teacher = new Teacher(value.getString("name")
                                    ,value.getString("lastname")
                                    ,value.getString("nickname")
                                    ,user.getEmail()
                                    ,userID
                                    ,value.getString("userType"));

                            Intent intent = new Intent(getApplicationContext(),TeacherMain.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("nickname",teacher.getNickname());
                            bundle.putString("type",teacher.getType());
                            bundle.putString("id",teacher.getId());

                            intent.putExtras(bundle);

                            startActivity(intent);
                            finish();
                        }
                        Toast.makeText(getApplicationContext(),"Inicio de sesión exitoso",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, 1500);
    }
}