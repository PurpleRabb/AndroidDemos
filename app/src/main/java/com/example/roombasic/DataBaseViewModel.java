package com.example.roombasic;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DataBaseViewModel extends AndroidViewModel {
    private WordDao wordDao;
    private WordDatabase wordDatabase;
    private LiveData<List<Word>> allWords;
    public DataBaseViewModel(@NonNull Application application) {
        super(application);
        wordDatabase = WordDatabase.getInstance(application);
        this.wordDao = wordDatabase.getWordDao();
        allWords = wordDao.getAllWords(); //自动后台执行
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    void insertWords(Word... words) {
        new InsertTask(wordDao).execute(words);
    }

    void updateWords(Word... words) {
        new UpdateTask(wordDao).execute(words);
    }

    void deleteWords(Word... words) {
        new DeleteTask(wordDao).execute(words);
    }

    void deleteAllWords() {
        new DeleteAllTask(wordDao).execute();
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
