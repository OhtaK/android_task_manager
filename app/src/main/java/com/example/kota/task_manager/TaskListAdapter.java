package com.example.kota.task_manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by keisuke-ota on 2019/05/22.
 */

public class TaskListAdapter extends ArrayAdapter<Task> {

    private LayoutInflater inflater;

    public TaskListAdapter(Context context, List<Task> objects) {
        super(context, 0, objects);
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Viewを受け取ってlist要素用のlayoutにセットしていく
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_list, parent, false);
        }

        Task task = this.getItem(position);

        TextView tv = (TextView) convertView.findViewById(R.id.title);
        tv.setText("タスク名：" + task.getTitle());

        tv = (TextView) convertView.findViewById(R.id.limit_date);
        tv.setText("期日：" + task.getLimitDate());

        tv = (TextView) convertView.findViewById(R.id.description);
        tv.setText("備考：" + task.getDescription());

        return convertView;
    }
}
