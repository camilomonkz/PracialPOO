package com.example.pracialpoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    private EditText etName,etLastname,etNickname,etAge,etEmail,etPassword,etRePassword;
    private RadioButton rbtnStudent, rbtnTeacher;
    private Button signInBtn, backBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        etName = (EditText)findViewById(R.id.etNameSign);
        etLastname = (EditText)findViewById(R.id.etLastnameSign);
        etNickname = (EditText)findViewById(R.id.etNickSign);
        etAge = (EditText)findViewById(R.id.etAgeSign);
        etEmail = (EditText)findViewById(R.id.etEmailSign);
        etPassword = (EditText)findViewById(R.id.etPasswordSign);
        etRePassword = (EditText)findViewById(R.id.etRepasswordSign);

        rbtnStudent = (RadioButton)findViewById(R.id.rbtnStudenSign);
        rbtnTeacher = (RadioButton)findViewById(R.id.rbtnTeacherSign);

        signInBtn = (Button)findViewById(R.id.signInBtnSign);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String lastname = etLastname.getText().toString().trim();
                String nickname = etNickname.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String rePassword = etRePassword.getText().toString().trim();

                if(!name.isEmpty()
                        && !lastname.isEmpty()
                        && !nickname.isEmpty()
                        && !age.isEmpty()
                        && !email.isEmpty()
                        && !password.isEmpty()
                        && !rePassword.isEmpty()
                        &&(rbtnStudent.isChecked() || rbtnTeacher.isChecked())){
                    if(password.length() > 6 && password.equals(rePassword)){

                        String userType = "";

                        if(rbtnTeacher.isChecked()){
                            userType = "profesor";
                        }else{
                            userType = "estudiante";
                        }

                        String finalUserType = userType;

                        firebaseAuth
                                .createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {

                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    String userID = user.getUid();

                                    DocumentReference documentReference;

                                    documentReference = firebaseFirestore
                                            .collection("Users").document(userID);

                                    HashMap<Object, Object> userInfo = new HashMap<>();

                                    userInfo.put("name",name);
                                    userInfo.put("lastname",lastname);
                                    userInfo.put("nickname",nickname);
                                    userInfo.put("age",Integer.parseInt(age));
                                    userInfo.put("userType", finalUserType);

                                    documentReference.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),LogIn.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error en el Registro", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(), "Error en el Registro", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Las Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });


        backBtn = (Button)findViewById(R.id.backBtnSign);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}