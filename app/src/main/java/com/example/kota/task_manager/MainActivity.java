package com.example.kota.task_manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MySQLiteOpenHelper helper;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //タスクを検索してviewにセット
        for(int statusId = 1; statusId < 3; statusId++){
            setTaskListView(statusId);
        }

        Button sendButton = findViewById(R.id.to_task_edit);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TaskActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setTaskListView(Integer statusId){
        // DB作成
        helper = new MySQLiteOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        //タスク検索
        List<Task> taskList = Task.findAllByStatusId(db, statusId);

        //独自リスト表示用のadapterを用意
        TaskListAdapter adapter = new TaskListAdapter(getApplicationContext(), taskList);
        adapter.addAll(taskList);

        ListView listView = null;
        // ListViewにArrayAdapterを設定する
        switch(statusId){
            case 1:
                listView = (ListView)findViewById(R.id.todo_task_list);
                break;

            case 2:
                listView = (ListView)findViewById(R.id.doing_task_list);
                break;

            case 3:
                //listView = (ListView)findViewById(R.id.doing_task_list);
                break;

            default:
                //不正な引数が入りましたエラーを投げる
                break;
        }

        listView.setAdapter(adapter);
    }
}
