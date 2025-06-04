package com.example.notebook;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.graphics.Color;


public class TaskItem implements Serializable {
    private String taskDescription;
    private String dateTime;
    private boolean isImportant;
    private int color; // 新增颜色属性

    public TaskItem(String taskDescription, String dateTime) {
        this.taskDescription = taskDescription;
        this.dateTime = dateTime;
        this.isImportant = false;
        this.color = Color.TRANSPARENT; // 默认颜色为透明
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public void setNormal() {
        this.isImportant = false;
        this.color = Color.WHITE; // 設置為白色
    }



    @Override
    public String toString() {
        String importantTag = isImportant ? " " : " ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(new Date());
        return taskDescription + importantTag + "(日期: " + dateTime  + ")";
    }
}
