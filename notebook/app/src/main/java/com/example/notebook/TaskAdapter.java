package com.example.notebook;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<TaskItem> {

    private ListView listViewTasks;

    public TaskAdapter(Context context, int resource, List<TaskItem> tasks) {
        super(context, resource, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TaskItem taskItem = getItem(position);

        if (taskItem != null) {
            SpannableString spannableString = new SpannableString(taskItem.toString());

            if (taskItem.isImportant()) {
                // 重要事項，使用紅色字體
                spannableString.setSpan(new ForegroundColorSpan(taskItem.getColor()), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                // 一般事項，使用白色字體
                spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            ((TextView) view).setText(spannableString);
        }

        return view;
    }
}

