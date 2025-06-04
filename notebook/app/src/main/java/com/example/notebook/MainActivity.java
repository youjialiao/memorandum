package com.example.notebook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText taskDescription, editTextDateTime;
    private Button btnAddTask, btnDeleteTasks, btnConfirm, btnToggleImportance;
    private ListView listViewTasks;
    private ArrayList<TaskItem> tasks;
    private TaskAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDescription = findViewById(R.id.editTextTask);
        editTextDateTime = findViewById(R.id.editTextDateTime);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnDeleteTasks = findViewById(R.id.btnDeleteTasks);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnToggleImportance = findViewById(R.id.btnToggleImportance);
        listViewTasks = findViewById(R.id.listViewTasks);

        tasks = new ArrayList<>();
        tasksAdapter = new TaskAdapter(this, android.R.layout.simple_list_item_multiple_choice, tasks);
        listViewTasks.setAdapter(tasksAdapter);

        listViewTasks.setOnItemClickListener((adapterView, view, position, id) -> updateButtonsVisibility());

        btnAddTask.setOnClickListener(view -> addTask());
        btnDeleteTasks.setOnClickListener(view -> deleteSelectedTasks());
        btnConfirm.setOnClickListener(view -> confirmSelectedTasks());
        btnToggleImportance.setOnClickListener(view -> toggleImportance());
        editTextDateTime.setOnClickListener(view -> showDateTimePicker());

        // 在應用重新啟動時讀取狀態
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedTaskDescription = preferences.getString("taskDescription", "");
        String savedDateTime = preferences.getString("dateTime", "");

        // 將讀取到的數據設置到對應的控件上
        taskDescription.setText(savedTaskDescription);
        editTextDateTime.setText(savedDateTime);
    }

    // 在適當的地方調用這個方法，以保存狀態
    private void saveData() {
        Log.d("MainActivity", "Saving data...");

        String taskDescriptionText = taskDescription.getText().toString();
        String dateTimeText = editTextDateTime.getText().toString();

        // 在保存狀態時
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("taskDescription", taskDescriptionText);
        editor.putString("dateTime", dateTimeText);
        editor.apply();
    }

    private void addTask() {
        String task = taskDescription.getText().toString().trim();
        String dateTime = editTextDateTime.getText().toString().trim();

        if (!task.isEmpty() && !dateTime.isEmpty()) {
            TaskItem taskItem = new TaskItem(task, dateTime);
            taskItem.setColor(Color.TRANSPARENT);

            tasks.add(taskItem);
            tasksAdapter.notifyDataSetChanged();
            clearInputFields();
            updateButtonsVisibility();
        } else {
            Toast.makeText(this, "未填寫代辦事項及時間", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSelectedTasks() {
        SparseBooleanArray checkedItems = listViewTasks.getCheckedItemPositions();
        for (int i = checkedItems.size() - 1; i >= 0; i--) {
            int position = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                tasks.remove(position);
            }
        }
        listViewTasks.clearChoices();
        tasksAdapter.notifyDataSetChanged();
        updateButtonsVisibility();
    }

    private void confirmSelectedTasks() {
        SparseBooleanArray checkedItems = listViewTasks.getCheckedItemPositions();
        for (int i = 0; i < checkedItems.size(); i++) {
            int position = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                TaskItem taskItem = tasks.get(position);
                // You can perform actions based on the selected taskItem, such as changing its color.
                // For example, changing the background color to indicate importance.
                taskItem.setImportant(true);
                taskItem.setColor(Color.RED); // 設置為紅色
            }
        }
        tasksAdapter.notifyDataSetChanged();
    }

    private void toggleImportance() {
        SparseBooleanArray checkedItems = listViewTasks.getCheckedItemPositions();
        for (int i = 0; i < checkedItems.size(); i++) {
            int position = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                TaskItem taskItem = tasks.get(position);

                // 設置回一般事項
                taskItem.setNormal();
            }
        }
        tasksAdapter.notifyDataSetChanged();
    }

    private void updateButtonsVisibility() {
        int checkedCount = listViewTasks.getCheckedItemCount();
        btnDeleteTasks.setVisibility(checkedCount > 0 ? View.VISIBLE : View.GONE);
        btnConfirm.setVisibility(checkedCount > 0 ? View.VISIBLE : View.GONE);
        btnToggleImportance.setVisibility(checkedCount > 0 ? View.VISIBLE : View.GONE);
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> showTimePicker(year1, month1, dayOfMonth),
                year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker(final int year, final int month, final int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> displaySelectedDateTime(year, month, dayOfMonth, hourOfDay, minute1),
                hour, minute, true);

        timePickerDialog.show();
    }

    private void displaySelectedDateTime(int year, int month, int day, int hour, int minute) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        Calendar selectedDateTime = Calendar.getInstance();
        selectedDateTime.set(year, month, day, hour, minute);
        String formattedDateTime = dateFormat.format(selectedDateTime.getTime());
        editTextDateTime.setText(formattedDateTime);
    }

    private void clearInputFields() {
        taskDescription.getText().clear();
        editTextDateTime.getText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在應用程序停止時保存數據
        saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause - Saving data...");
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy - Saving data...");
        saveData();
    }
}
