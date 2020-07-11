package com.example.roombasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.os.AsyncTask;
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
    LiveData<List<Word>> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordDatabase = WordDatabase.getInstance(this);
        wordDao = wordDatabase.getWordDao();
        insertButton = findViewById(R.id.insert);
        updateButton = findViewById(R.id.update);
        deleteButton = findViewById(R.id.delete);
        clearButton = findViewById(R.id.clear);

        textView = findViewById(R.id.textView2);

        words = wordDao.getAllWords();
        words.observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                String text = "";
                for (int i=0;i<words.size();i++) {
                    Word word = words.get(i);
                    text += word.getId() + ":" + word.getWord() + "=" + word.getMeaning() + "\n";
                }
                textView.setText(text);
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word1 = new Word("Hello","你好");
                Word word2 = new Word("Apple","苹果");
                new InsertTask(wordDao).execute(word1,word2);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteAllTask(wordDao).execute();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("test","测试");
                word.setId(35);
                new UpdateTask(wordDao).execute(word);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("test","测试");
                word.setId(35);
                new DeleteTask(wordDao).execute(word);
            }
        });
    }

    static class InsertTask extends AsyncTask<Word,Void,Void> {
        private WordDao wordDao;
        public InsertTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    static class UpdateTask extends AsyncTask<Word,Void,Void> {
        private WordDao wordDao;
        public UpdateTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWords(words);
            return null;
        }
    }

    static class DeleteTask extends AsyncTask<Word,Void,Void> {
        private WordDao wordDao;
        public DeleteTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }

    static class DeleteAllTask extends AsyncTask<Void,Void,Void> {
        private WordDao wordDao;
        public DeleteAllTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}