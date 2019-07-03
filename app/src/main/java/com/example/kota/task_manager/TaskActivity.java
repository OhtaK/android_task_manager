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

import com.example.kota.task_manager.Constant.StatusId;
import com.example.kota.task_manager.Entity.Task;
import com.example.kota.task_manager.Entity.TaskSQLiteOpenHelper;
import com.example.kota.task_manager.Fragment.DatePickerDialogFragment;

public class TaskActivity extends AppCompatActivity  {

    private Integer editTaskId;
    private EditText etTitle;
    private EditText etLimitDate;
    private EditText etDescription;
    private Spinner spinnerItem;
    private Button taskAddButton;
    private Button taskDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add);

        etTitle = (EditText)findViewById(R.id.edit_task_name);
        etLimitDate = (EditText) findViewById(R.id.edit_limit_date);
        etDescription = (EditText) findViewById(R.id.edit_description);
        spinnerItem = (Spinner) findViewById(R.id.status_id_spinner);
        taskAddButton = findViewById(R.id.task_add_btn);
        taskDeleteButton = findViewById(R.id.task_delete_btn);

        Intent intent = getIntent();
        editTaskId = 0;
        if(intent.getStringExtra("task_id") != null) {
            //タスク情報から遷移してきた場合、そのタスクの情報を初期値としてテキストボックスに設定しておく
            editTaskId = Integer.valueOf(intent.getStringExtra("task_id"));
            TaskSQLiteOpenHelper helper = new TaskSQLiteOpenHelper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            Task task = TaskSQLiteOpenHelper.findByTaskId(db, editTaskId);

            //TextView.BufferTypeによって返り値が異なる。参考：https://high-programmer.com/2017/09/09/android-studio-edittext-setvalue/
            etTitle.setText(task.getTitle(), TextView.BufferType.NORMAL);
            etLimitDate.setText(task.getLimitDate(), TextView.BufferType.NORMAL);
            etDescription.setText(task.getDescription(), TextView.BufferType.NORMAL);
            spinnerItem.setSelection(task.getStatusId() - 1);//スピナーの要素の添え字は0からなので

            //削除ボタン表示
            taskDeleteButton.setVisibility(View.VISIBLE);
        }

        taskAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = (String) spinnerItem.getSelectedItem();
                String title = etTitle.getText().toString();
                String limitDate = etLimitDate.getText().toString();
                String description = etDescription.getText().toString();
                Integer statusId = StatusId.valueOf(selectedStatus).getValue();

                TaskSQLiteOpenHelper helper = new TaskSQLiteOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();

                if(editTaskId > 0){
                    TaskSQLiteOpenHelper.updateById(db, editTaskId, title, description, limitDate, statusId);
                }
                else{
                    TaskSQLiteOpenHelper.saveData(db, helper.fetchLastId(db) + 1, title, description, limitDate, statusId);
                }

                //トップページにリダイレクト
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        taskDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskSQLiteOpenHelper helper = new TaskSQLiteOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();
                TaskSQLiteOpenHelper.deleteById(db, editTaskId);

                //トップページにリダイレクト
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        etLimitDate.setKeyListener(null);
        etLimitDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    //datepickerを呼び出し
                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.showDateView = (EditText) findViewById(R.id.edit_limit_date);
                    datePicker.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });
        etLimitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLimitDate.getOnFocusChangeListener().onFocusChange(etLimitDate, true);
            }
        });
    }
}
