package com.example.parcelabletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.parcelabletest.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ActivityMain3Binding databinding = DataBindingUtil.setContentView(this,R.layout.activity_main3);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("student_data");
        Student student = bundle.getParcelable("student");
        databinding.textView.setText(String.valueOf(student.getId()));
        databinding.textView2.setText(student.getName());
    }
}