package com.example.kota.task_manager;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private boolean search_disp_flg;
    private RelativeLayout searchComponent;
    private TextView search_disp_switch;

    private Map<String, String> conditionMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        search_disp_flg = false;

        //タスクを検索してviewにセット
        StatusId[] statusIds = StatusId.values();
        for (StatusId statusId : statusIds) {
            conditionMap.clear();
            conditionMap.put("status_id", String.valueOf(statusId.getValue()));

            String condition = TaskSQLiteOpenHelper.buildSelectionStr(conditionMap);
            setTaskListView(statusId.getValue(), condition, null);
        }

        searchComponent = (RelativeLayout)findViewById(R.id.search_component);
        searchComponent.setVisibility(View.GONE);

        search_disp_switch = (TextView)findViewById(R.id.search_disp_switch);
        search_disp_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_disp_flg){
                    searchComponent.setVisibility(View.GONE);
                    search_disp_switch.setText("検索、ソート条件を表示");
                }
                else{
                    searchComponent.setVisibility(View.VISIBLE);
                    search_disp_switch.setText("検索、ソート条件を非表示");
                }

                search_disp_flg = !search_disp_flg;
            }
        });

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
                String selectedOrderColumns = (String) spinnerOrderColumns.getSelectedItem();//ソートする基準となるカラム
                Spinner spinnerOrderBy = (Spinner) findViewById(R.id.sort_order_by_spinner);
                String selectedOrderBy = (String) spinnerOrderBy.getSelectedItem();//昇順 or 降順

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
                StatusId[] statusIds = StatusId.values();
                for (StatusId statusId : statusIds) {
                    //SQLiteのqueryメソッドに入れるconditionのString作成
                    conditionMap.clear();
                    conditionMap.put("limit_date_start", limitDateStart);
                    conditionMap.put("limit_date_end", limitDateEnd);
                    conditionMap.put("status_id", String.valueOf(statusId.getValue()));
                    String condition = TaskSQLiteOpenHelper.buildSelectionStr(conditionMap);

                    setTaskListView(statusId.getValue(), condition, order);
                }
            }
        });
    }

    public void toEditActivity(){
        Intent intent = new Intent(getApplication(), TaskActivity.class);
        startActivity(intent);
    }

    private void setTaskListView(int statusId, String condition, String order){

        TaskSQLiteOpenHelper helper = new TaskSQLiteOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        //タスク検索
        List<Task> taskList = TaskSQLiteOpenHelper.findAllByConditionStr(db, condition, order);
        //独自リスト表示用のadapterを用意
        TaskListAdapter adapter = new TaskListAdapter(getApplicationContext(), taskList);

        // ListViewにArrayAdapterを設定する
        ListView listView = null;
        switch(StatusId.getType(statusId)){
            case TODO:
                listView = (ListView)findViewById(R.id.todo_task_list);
                break;

            case DOING:
                listView = (ListView)findViewById(R.id.doing_task_list);
                break;

            case DONE:
                listView = (ListView)findViewById(R.id.done_task_list);
                break;

            default:
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
