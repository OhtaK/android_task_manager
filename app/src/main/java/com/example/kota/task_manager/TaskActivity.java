package com.example.kota.task_manager;

/**
 * Created by keisuke-ota on 2019/04/17.
 */

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;

public class TaskActivity extends AppCompatActivity  {

    private MySQLiteOpenHelper helper;
    private Integer editTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add);

        EditText etTitle = (EditText)findViewById(R.id.edit_task_name);
        EditText etLimitDate = (EditText) findViewById(R.id.edit_limit_date);
        EditText etDescription = (EditText) findViewById(R.id.edit_description);
        Spinner spinnerItem = (Spinner) findViewById(R.id.status_id_spinner);
        Button taskAddButton = findViewById(R.id.task_add_btn);
        Button taskDeleteButton = findViewById(R.id.task_delete_btn);

        Intent intent = getIntent();
        editTaskId = 0;
        if(intent.getStringExtra("task_id") != null) {
            //タスク情報から遷移してきた場合、そのタスクの情報を初期値として設定しておく
            editTaskId = Integer.valueOf(intent.getStringExtra("task_id"));
            helper = new MySQLiteOpenHelper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            Task task = Task.findByTaskId(db, editTaskId);

            etTitle.setText(task.getTitle(), TextView.BufferType.NORMAL);
            etLimitDate.setText(task.getLimitDate(), TextView.BufferType.NORMAL);
            etDescription.setText(task.getDescription(), TextView.BufferType.NORMAL);
            spinnerItem.setSelection(task.getStatusId() - 1);

            //削除ボタン表示
            taskDeleteButton.setVisibility(View.VISIBLE);
        }

        taskAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //各テキストボックスの値取得
                EditText etTitle = (EditText) findViewById(R.id.edit_task_name);
                EditText etLimitDate = (EditText) findViewById(R.id.edit_limit_date);
                EditText etDescription = (EditText) findViewById(R.id.edit_description);

                // Spinnerから選択したステータスを取得
                Spinner spinnerItem = (Spinner) findViewById(R.id.status_id_spinner);
                String selectedStatus = (String) spinnerItem.getSelectedItem();

                String title = etTitle.getText().toString();
                String limitDate = etLimitDate.getText().toString();
                String description = etDescription.getText().toString();
                Integer statusId = StatusId.valueOf(selectedStatus).getStatusId();

                //SQLiteに保存
                helper = new MySQLiteOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                if(editTaskId > 0){
                    MySQLiteOpenHelper.updateById(db, editTaskId, title, description, limitDate, statusId);
                }
                else{
                    MySQLiteOpenHelper.saveData(db, Task.fetchLastId(db) + 1, title, description, limitDate, statusId);
                }

                //トップページにリダイレクト
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        taskDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SQLiteに保存
                helper = new MySQLiteOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                if(editTaskId > 0){
                    MySQLiteOpenHelper.deleteById(db, editTaskId);
                }
                else{
                    //タスクが選択されてませんエラーを投げる
                }

                //トップページにリダイレクト
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        etLimitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //datepickerを呼び出し
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.showDateView = (EditText) findViewById(R.id.edit_limit_date);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }
}
