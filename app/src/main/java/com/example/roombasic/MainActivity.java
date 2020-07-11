package com.example.roombasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    WordDao wordDao;
    WordDatabase wordDatabase;
    Button insertButton,updateButton,deleteButton,clearButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordDatabase = Room.databaseBuilder(this,WordDatabase.class, "word_database")
                .allowMainThreadQueries()
                .build();
        wordDao = wordDatabase.getWordDao();
        insertButton = findViewById(R.id.insert);
        updateButton = findViewById(R.id.update);
        deleteButton = findViewById(R.id.delete);
        clearButton = findViewById(R.id.clear);

        textView = findViewById(R.id.textView2);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word1 = new Word("Hello","你好");
                Word word2 = new Word("Apple","苹果");
                wordDao.insertWords(word1,word2);
                updateView();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordDao.deleteAllWords();
                updateView();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("test","测试");
                word.setId(35);
                wordDao.updateWords(word);
                updateView();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("test","测试");
                word.setId(35);
                wordDao.deleteWords(word);
                updateView();
            }
        });
    }

    void updateView() {
        List<Word> words = wordDao.getAllWords();
        String text = "";
        for (int i=0;i<words.size();i++) {
            Word word = words.get(i);
            text += word.getId() + ":" + word.getWord() + "=" + word.getMeaning() + "\n";
        }
        textView.setText(text);
    }
}