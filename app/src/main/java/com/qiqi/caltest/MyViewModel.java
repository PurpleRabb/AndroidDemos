package com.qiqi.caltest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.Random;

public class MyViewModel extends AndroidViewModel {
    private static final String KEY_HIGH_SCORE = "key_high_score";
    private static final String KEY_RIGHT_NUM = "key_right_num";
    private static final String KEY_LEFT_NUM = "key left_num";
    private static final String KEY_OPERATOR = "key_operator";
    private static final String SAVE_DATA_NAME = "data";
    private static final String KEY_CURRENT_SCORE = "key_current_score";
    private static final String KEY_ANSWER = "key_answer";
    private String[] operators = {"+","-"};//,"X","÷"};
    private SavedStateHandle handle;
    public boolean new_record = false;

    public MyViewModel(Application application, SavedStateHandle handle) {
        super(application);
        if(!handle.contains(KEY_HIGH_SCORE)) {
            SharedPreferences shp = getApplication().getSharedPreferences(SAVE_DATA_NAME, Context.MODE_PRIVATE);
            handle.set(KEY_HIGH_SCORE,shp.getInt(KEY_HIGH_SCORE,0));
            handle.set(KEY_LEFT_NUM,0);
            handle.set(KEY_RIGHT_NUM,0);
            handle.set(KEY_OPERATOR,"+");
            handle.set(KEY_ANSWER,0);
            handle.set(KEY_CURRENT_SCORE,0);
        }
        this.handle = handle;
    }

    public MutableLiveData<Integer> getLeftNum() {
        return handle.getLiveData(KEY_LEFT_NUM);
    }

    public MutableLiveData<Integer> getRightNum() {
        return handle.getLiveData(KEY_RIGHT_NUM);
    }

    public MutableLiveData<String> getOperator() {
        return handle.getLiveData(KEY_OPERATOR);
    }

    public MutableLiveData<Integer> getHighScore() {
        return handle.getLiveData(KEY_HIGH_SCORE);
    }

    public MutableLiveData<Integer> getCurScore() {
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }

    public MutableLiveData<Integer> getAnswer() {
        return handle.getLiveData(KEY_ANSWER);
    }

    void generator() {
        int LEVEL = 10;
        Random random = new Random();
        int x,y;
        x = random.nextInt(LEVEL) + 1;
        y = random.nextInt(LEVEL) + 1;
        int index=(int)(Math.random()*operators.length);
        getOperator().setValue(operators[index]);
        if(getOperator().getValue() == "+") {
            getLeftNum().setValue(x);
            getRightNum().setValue(y);
            getAnswer().setValue(x+y);
        }
        if(getOperator().getValue() == "-") {
            if(x < y) {
                getAnswer().setValue(y - x);
                getLeftNum().setValue(y);
                getRightNum().setValue(x);
            } else {
                //避免出现负数
                getAnswer().setValue(x - y);
                getLeftNum().setValue(x);
                getRightNum().setValue(y);
            }
        }
    }

    void save() {
        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_DATA_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt(KEY_HIGH_SCORE,getHighScore().getValue());
        editor.apply();
    }

    void answerCorrect() {
        getCurScore().setValue(getCurScore().getValue() + 1);
        if(getCurScore().getValue() > getHighScore().getValue()) {
            getHighScore().setValue(getCurScore().getValue());
            new_record = true;
        }
        generator();
    }

    void reset() {
        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_DATA_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt(KEY_HIGH_SCORE,0);
        editor.apply();
        getHighScore().setValue(0);
        getCurScore().setValue(0);
    }
}
