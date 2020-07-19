package com.example.parcelabletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.parcelabletest.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

                    /*对象转换为Json*/
                    Gson gson = new Gson();
                    String jsonStudent = gson.toJson(student);
                    Log.i("GSON",jsonStudent);

                    /*Json转化为对象*/
                    String jsonString = "{\"id\":123,\"name\":\"Tom\"}";
                    Student fromJsonStudent = gson.fromJson(jsonString,Student.class);
                    Log.i("GSON",fromJsonStudent.getName());

                    List<Student> students = new ArrayList<>();
                    Student s1 = new Student(0,"Jerry");
                    Student s2 = new Student(1,"Rose");
                    students.add(s1);
                    students.add(s2);
                    String jsonStudents = gson.toJson(students);
                    Log.i("GSON",jsonStudents);

                    String _jsonStudents = "[{\"id\":0,\"name\":\"Jerry\"},{\"id\":1,\"name\":\"Rose\"}]";

                    //fromJson无法识别List<Student>.class，这里用反射创建新的Type
                    Type typeStudents = new TypeToken<List<Student>>(){}.getType();
                    List<Student> _students = gson.fromJson(_jsonStudents,typeStudents);
                    Log.i("GSON",_students.get(1).getName());
                }
            }
        });
    }
}