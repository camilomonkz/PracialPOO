package com.example.pracialpoo;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Teacher extends User implements Serializable {
    public Teacher(String name, String lastname, String nickname, String email, String id, String type) {
        super(name, lastname, nickname, email, id, type);
    }

    public Teacher() {
    }

    public void setScore(Comment comment,String date, int score, Context context){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        getInfo(new FirestoreCallback() {
            @Override
            public void onCallback(List<String> list) {
                String id = list.get(0);
                getScore(new FirestoreCallback2() {
                    @Override
                    public void onCallback(List<Integer> list) {
                        int totalScore =  (score+list.get(1));
                        int totalNumber = list.get(0) + 1;

                        HashMap<String,Object> ScoreInfo = new HashMap<>();
                        ScoreInfo.put("number",totalNumber);
                        ScoreInfo.put("score",totalScore);
                        DocumentReference documentReference = firebaseFirestore
                                .collection("comments")
                                .document(id);
                        documentReference.update(ScoreInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context,"!Comentario puntuado!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Error en puntuar comentario",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },id);
            }
        },date);
    }
    private void getInfo(FirestoreCallback firestoreCallback,String date){
        ArrayList<String> commentList = new ArrayList<>();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore
                .collection("comments");

        collectionReference
                .whereEqualTo("date",date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot element: task.getResult()){
                            String commentID = element.getId();
                            commentList.add(commentID);
                        }
                        firestoreCallback.onCallback(commentList);
                    }
                });

    }

    private void getScore(FirestoreCallback2 firestoreCallback,String id){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayList<Integer> commentList = new ArrayList<>();

        DocumentReference documentReference = firebaseFirestore
                .collection("comments").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int number = task.getResult().getLong("number").intValue();
                int score = task.getResult().getLong("score").intValue();
                commentList.add(number);
                commentList.add(score);
                firestoreCallback.onCallback(commentList);
            }

        });

    }
}
interface FirestoreCallback{
    void onCallback(List<String> list);
}
interface FirestoreCallback2{
    void onCallback(List<Integer>list);
}
