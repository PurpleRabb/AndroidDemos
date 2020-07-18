package com.example.serializabletest;

import java.io.Serializable;

public class Student implements Serializable {
    //transient关键字可防止被序列化
    private String name;
    private int num;
    private Score score;

    public Student(String name, int num, Score score) {
        this.name = name;
        this.num = num;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

}

class Score implements Serializable {
    private int Chinese;
    private int English;

    public Score(int chinese, int english) {
        Chinese = chinese;
        English = english;
    }

    public int getChinese() {
        return Chinese;
    }

    public void setChinese(int chinese) {
        Chinese = chinese;
    }

    public int getEnglish() {
        return English;
    }

    public void setEnglish(int english) {
        English = english;
    }
}
