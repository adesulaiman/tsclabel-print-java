package com.example;

public class LabelRequest {
    private String leftBarcode;
    private String leftValue;
    private String leftInfo;
    private String rightBarcode;
    private String rightValue;
    private String rightInfo;
    private int count;

    public String getLeftBarcode() {
        return leftBarcode;
    }
    public void setLeftBarcode(String leftBarcode) {
        this.leftBarcode = leftBarcode;
    }
    public String getLeftValue() {
        return leftValue;
    }
    public void setLeftValue(String leftValue) {
        this.leftValue = leftValue;
    }
    public String getLeftInfo() {
        return leftInfo;
    }
    public void setLeftInfo(String leftInfo) {
        this.leftInfo = leftInfo;
    }
    public String getRightBarcode() {
        return rightBarcode;
    }
    public void setRightBarcode(String rightBarcode) {
        this.rightBarcode = rightBarcode;
    }
    public String getRightValue() {
        return rightValue;
    }
    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }
    public String getRightInfo() {
        return rightInfo;
    }
    public void setRightInfo(String rightInfo) {
        this.rightInfo = rightInfo;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
} 