package com.example.roombasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button insertButton,updateButton,deleteButton,clearButton;
    TextView textView;
    private DataBaseViewModel wordViewModel;
    RecyclerView recyclerView;
    MyAdapter myAdapter_cardView,myAdapter_normalView;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        myAdapter_normalView = new MyAdapter(false);
        myAdapter_cardView = new MyAdapter(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        aSwitch = findViewById(R.id.switch_cardview);
        recyclerView.setAdapter(myAdapter_normalView);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    recyclerView.setAdapter(myAdapter_cardView);
                } else {
                    recyclerView.setAdapter(myAdapter_normalView);
                }
            }
        });

        insertButton = findViewById(R.id.insert);
        //updateButton = findViewById(R.id.update);
        //deleteButton = findViewById(R.id.delete);
        clearButton = findViewById(R.id.clear);

        textView = findViewById(R.id.textViewEnglish);

        wordViewModel = new ViewModelProvider(this,new SavedStateViewModelFactory(getApplication(),this))
                .get(DataBaseViewModel.class);
        wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                myAdapter_cardView.setAllWords(words);
                myAdapter_cardView.notifyDataSetChanged();

                myAdapter_normalView.setAllWords(words);
                myAdapter_normalView.notifyDataSetChanged();
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] english_word = {
                        "Hello",
                        "Apple",
                        "Orange",
                        "World",
                        "Data",
                        "Database"
                };
                String[] chinese_meaning = {
                        "你好",
                        "苹果",
                        "橘子",
                        "世界",
                        "数据",
                        "数据库"
                };
                for(int i=0;i<english_word.length;i++) {
                    wordViewModel.insertWords(new Word(english_word[i],chinese_meaning[i]));
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            wordViewModel.deleteAllWords();
            }
        });

//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Word word = new Word("test","测试");
//                word.setId(90);
//                wordViewModel.updateWords(word);
//            }
//        });

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Word word = new Word("test","测试");
//                word.setId(35);
//                wordViewModel.deleteWords(word);
//            }
//        });
    }
}