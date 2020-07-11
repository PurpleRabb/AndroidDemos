package com.example.roombasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    WordDao wordDao;
    WordDatabase wordDatabase;
    Button insertButton,updateButton,deleteButton,clearButton;
    TextView textView;
    private LiveData<List<Word>> words;
    private DataBaseViewModel wordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertButton = findViewById(R.id.insert);
        updateButton = findViewById(R.id.update);
        deleteButton = findViewById(R.id.delete);
        clearButton = findViewById(R.id.clear);

        textView = findViewById(R.id.textView2);

        wordViewModel = new ViewModelProvider(this,new SavedStateViewModelFactory(getApplication(),this))
                .get(DataBaseViewModel.class);
        words = wordViewModel.getAllWords();
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
                wordViewModel.insertWords(word1,word2);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            wordViewModel.deleteAllWords();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("test","测试");
                word.setId(35);
                wordViewModel.updateWords(word);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("test","测试");
                word.setId(35);
                wordViewModel.deleteWords(word);
            }
        });
    }
}