package com.example.parcelabletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.parcelabletest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding databinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        databinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = databinding.editTextName.getText().toString();
                String _id = databinding.editTextId.getText().toString();
                if(!name.isEmpty() && !_id.isEmpty() ) {
                    int id = Integer.valueOf(_id);
                    Student student = new Student(id,name);
                    Intent intent = new Intent(MainActivity.this,MainActivity3.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("student",student);
                    intent.putExtra("student_data",bundle);
                    startActivity(intent);
                }
            }
        });
    }
}