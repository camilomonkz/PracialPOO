package com.example.pracialpoo;

import java.io.Serializable;

public class Student extends User implements Serializable {
    public Student(String name, String lastname, String nickname, String email, String id, String type) {
        super(name, lastname, nickname, email, id, type);
    }
}
