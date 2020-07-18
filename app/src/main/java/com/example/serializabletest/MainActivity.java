package com.example.serializabletest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    EditText num,name,chinese,english;
    Button save,load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num = findViewById(R.id.num);
        name = findViewById(R.id.name);
        chinese = findViewById(R.id.chinese);
        english = findViewById(R.id.english);

        save = findViewById(R.id.save);
        load = findViewById(R.id.load);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*未对输入值进行判断*/
                int _num = Integer.valueOf(num.getText().toString());
                String _name = name.getText().toString();
                int _chinese = Integer.valueOf(chinese.getText().toString());
                int _english = Integer.valueOf(english.getText().toString());
                Score score = new Score(_chinese,_english);
                Student student = new Student(_name,_num,score);

                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(openFileOutput("student.data",MODE_PRIVATE));
                    objectOutputStream.writeObject(student);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    name.setText("");
                    num.setText("");
                    chinese.setText("");
                    english.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput("student.data"));
                    Student student = (Student) objectInputStream.readObject();
                    name.setText(student.getName());
                    num.setText(String.valueOf(student.getNum()));
                    chinese.setText(String.valueOf(student.getScore().getChinese()));
                    english.setText(String.valueOf(student.getScore().getEnglish()));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}