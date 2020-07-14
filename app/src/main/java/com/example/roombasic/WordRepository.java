package com.example.roombasic;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private WordDao wordDao;
    private LiveData<List<Word>> allWords;
    public WordRepository(Application application) {
        WordDatabase wordDatabase = WordDatabase.getInstance(application);
        this.wordDao = wordDatabase.getWordDao();
        allWords = wordDao.getAllWords(); //自动后台执行
    }

    LiveData<List<Word>> getAllWords() {
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

    public LiveData<List<Word>> filterWords(String pattern) {
        return wordDao.getFilteredWords("%" + pattern + "%");//模糊查询SQL需要加%
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
