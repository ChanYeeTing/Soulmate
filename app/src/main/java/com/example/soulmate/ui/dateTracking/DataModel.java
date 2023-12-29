package com.example.soulmate.ui.dateTracking;

public class DataModel {
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private String text5;
    private String text6;
    private String text7;

    public DataModel(String text1, String text2, String text3, String text4, String text5) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
    }
    public DataModel(String text1, String text2, String text3, String text4, String text5, String text6) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
        this.text6 = text6;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }
    public String getText3() {
        return text3;
    }
    public String getText4() {
        return text4;
    }
    public String getText5() {
        return text5;
    }
    public  String getText6(){return text6;};


}
