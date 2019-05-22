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

        // DB作成
        helper = new MySQLiteOpenHelper(getApplicationContext());

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                "task",
                new String[] {"title", "description", "limit_date", "create_date", "update_date"},
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();
        List<Task> taskList = new ArrayList<Task>();

        //独自リスト表示用のadapterを用意
        TaskListAdapter adapter = new TaskListAdapter(getApplicationContext(), taskList);

        for (int i = 0; i < cursor.getCount(); i++) {
            Task task = new Task();
            task.setTitle(cursor.getString(0));
            task.setDescription(cursor.getString(1));
            task.setLimitDate(cursor.getString(2));
            taskList.add(task);
//            sbuilder.append(cursor.getString(0));
//            sbuilder.append(",");
//            sbuilder.append(cursor.getString(1));
//            sbuilder.append(",");
//            sbuilder.append(cursor.getString(2));
//            sbuilder.append(",");
//            sbuilder.append(cursor.getString(3));
//            sbuilder.append(",");
//            sbuilder.append(cursor.getString(4));
//            sbuilder.append("点\n\n");
            cursor.moveToNext();
        }


        cursor.close();

//        if (taskList != null) {
//            for (Task task : taskList) {
//                adapter.addAll(taskList);
//            }
//        }

        adapter.addAll(taskList);

        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        //ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);

        // ListViewにArrayAdapterを設定する
        ListView listView = (ListView)findViewById(R.id.task_list);
        listView.setAdapter(adapter);

        // 変数textViewに結果を表示
//        textView = findViewById(R.id.task_text);
//        textView.setText(sbuilder.toString());

        Button sendButton = findViewById(R.id.to_task_edit);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TaskActivity.class);
                startActivity(intent);
            }
        });
    }
}
