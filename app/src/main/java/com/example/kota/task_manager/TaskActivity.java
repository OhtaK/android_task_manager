package com.example.kota.task_manager;

/**
 * Created by keisuke-ota on 2019/04/17.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

public class TaskActivity extends AppCompatActivity  {

    private MySQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add);

        Intent intent = getIntent();
        Integer taskId = 0;
        if(intent.getStringExtra("task_id") != null) {
            taskId = Integer.valueOf(intent.getStringExtra("task_id"));
            helper = new MySQLiteOpenHelper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            Task task = Task.findByTaskId(db, taskId);

            //各テキストボックスの値取得
            EditText etTitle = (EditText)findViewById(R.id.edit_task_name);
            EditText etLimitDate = (EditText) findViewById(R.id.edit_limit_date);
            EditText etDescription = (EditText) findViewById(R.id.edit_description);
            EditText etStatusId = (EditText) findViewById(R.id.edit_status_id);

            etTitle.setText(task.getTitle(), TextView.BufferType.NORMAL);
            etLimitDate.setText(task.getLimitDate(), TextView.BufferType.NORMAL);
            etDescription.setText(task.getDescription(), TextView.BufferType.NORMAL);
            etStatusId.setText(String.valueOf(task.getStatusId()), TextView.BufferType.NORMAL);
        }

        Button taskAddButton = findViewById(R.id.task_add_btn);
        taskAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //各テキストボックスの値取得
                EditText etTitle = (EditText) findViewById(R.id.edit_task_name);
                EditText etLimitDate = (EditText) findViewById(R.id.edit_limit_date);
                EditText etDescription = (EditText) findViewById(R.id.edit_description);
                EditText etStatusId = (EditText) findViewById(R.id.edit_status_id);

                String title = etTitle.getText().toString();
                String limitDate = etLimitDate.getText().toString();
                String description = etDescription.getText().toString();
                Integer status_id = Integer.valueOf(etStatusId.getText().toString());

                //SQLiteに保存
                helper = new MySQLiteOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();
                db = helper.getReadableDatabase();
                Integer lastId = Task.fetchLastId(db);
                MySQLiteOpenHelper.saveData(db, lastId + 1, title, description, limitDate, status_id, "2017-12-09 15:00:00", "2017-12-09 15:00:00");

                //トップページにリダイレクト
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
