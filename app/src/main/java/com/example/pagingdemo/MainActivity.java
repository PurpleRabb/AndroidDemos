package com.example.pagingdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StudentDao studentDao;
    StudentDatabase studentDatabase;
    MyPageAdapter myPageAdapter;
    LiveData<PagedList<Student>> allStudents;
    Button insertBtn, clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPageAdapter = new MyPageAdapter();

        studentDatabase = StudentDatabase.getInstance(this);
        studentDao = studentDatabase.getStudentDao();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(myPageAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        allStudents = new LivePagedListBuilder<>(studentDao.getAllStudents(), 5).build(); //注意这里的数据获取方式
        allStudents.observe(this, new Observer<PagedList<Student>>() {
            @Override
            public void onChanged(PagedList<Student> students) {
                myPageAdapter.submitList(students);
            }
        });

        insertBtn = findViewById(R.id.insert);
        clearBtn = findViewById(R.id.clear);

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student[] students = new Student[1000];
                for (int i = 0; i < 1000; i++) {
                    Student student = new Student();
                    student.setNum(i);
                    students[i] = student;
                }
                new InsertAsync(studentDao).execute(students);
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteAsync(studentDao).execute();
            }
        });
    }

    static class InsertAsync extends AsyncTask<Student,Void,Void> {
        StudentDao studentDao;

        public InsertAsync(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.insert(students);
            return null;
        }
    }

    static class DeleteAsync extends AsyncTask<Void,Void,Void> {
        StudentDao studentDao;

        public DeleteAsync(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            studentDao.clear();
            return null;
        }
    }
}