package com.example.kota.task_manager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MySQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_main);

        //タスクを検索してviewにセット
        for(int statusId = 1; statusId < 4; statusId++){
            Map<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put("status_id", String.valueOf(statusId));
            helper = new MySQLiteOpenHelper(getApplicationContext());
            String condition = helper.buildSelectionStr(conditionMap);
            setTaskListView(statusId, condition, null);
        }

        Button sendButton = findViewById(R.id.to_task_edit);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditActivity();
            }
        });

        EditText etLimitDateStart = (EditText) findViewById(R.id.search_limit_date_start);
        etLimitDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ソフトキーボードを表示させない
                if (v != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                //datepickerを呼び出し
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.showDateView = (EditText) findViewById(R.id.search_limit_date_start);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        EditText etLimitDateEnd = (EditText) findViewById(R.id.search_limit_date_end);
        etLimitDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ソフトキーボードを表示させない
                if (v != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                //datepickerを呼び出し
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.showDateView = (EditText) findViewById(R.id.search_limit_date_end);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Button BtnSearch = findViewById(R.id.btn_search);
        BtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //conditionを取ってきてタスクリストを作り変える
                //各テキストボックスの値取得
                EditText etLimitDateStart = (EditText) findViewById(R.id.search_limit_date_start);
                EditText etLimitDateEnd = (EditText) findViewById(R.id.search_limit_date_end);
                String limitDateStart = etLimitDateStart.getText().toString();
                String limitDateEnd = etLimitDateEnd.getText().toString();

                // Spinnerから選択したステータスを取得
                Spinner spinnerOrderColumns = (Spinner) findViewById(R.id.sort_columns_spinner);
                String selectedOrderColumns = (String) spinnerOrderColumns.getSelectedItem();

                Spinner spinnerOrderBy = (Spinner) findViewById(R.id.sort_order_by_spinner);
                String selectedOrderBy = (String) spinnerOrderBy.getSelectedItem();

                String order = "";
                if(selectedOrderColumns.equals("期日")){
                    order += "limit_date ";
                }
                else{
                    order += "title ";
                }

                if(selectedOrderBy.equals("昇順")){
                    order += "asc";
                }
                else{
                    order += "desc";
                }

                //検索、Viewにセット
                SQLiteDatabase db = helper.getReadableDatabase();
                for(int statusId = 1; statusId < 4; statusId++){
                    //SQLiteのqueryメソッドに入れるconditionのString作成
                    Map<String, String> conditionMap = new HashMap<String, String>();
                    conditionMap.put("limit_date_start", limitDateStart);
                    conditionMap.put("limit_date_end", limitDateEnd);
                    conditionMap.put("status_id", String.valueOf(statusId));
                    helper = new MySQLiteOpenHelper(getApplicationContext());
                    String condition = helper.buildSelectionStr(conditionMap);

                    setTaskListView(statusId, condition, order);
                }
            }
        });
    }

    public void toEditActivity(){
        Intent intent = new Intent(getApplication(), TaskActivity.class);
        startActivity(intent);
    }

    private void setTaskListView(Integer statusId, String condition, String order){
        // DB作成
        helper = new MySQLiteOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        //タスク検索
        List<Task> taskList = Task.findAllByConditionStr(db, condition, order);

        //独自リスト表示用のadapterを用意
        TaskListAdapter adapter = new TaskListAdapter(getApplicationContext(), taskList);

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
                listView = (ListView)findViewById(R.id.done_task_list);
                break;

            default:
                //不正な引数が入りましたエラーを投げる
                break;
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //タスクのカードをクリックでそのタスクの編集画面に遷移
                Task item = (Task)parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplication(), TaskActivity.class);
                intent.putExtra("task_id", String.valueOf(item.getId()));
                startActivity(intent);
            }
        });
    }
}
