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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kota.task_manager.Constant.StatusId;
import com.example.kota.task_manager.Entity.Task;
import com.example.kota.task_manager.Entity.TaskSQLiteOpenHelper;
import com.example.kota.task_manager.Fragment.DatePickerDialogFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout searchComponent;
    private TextView searchDispSwitch;
    private boolean searchDispFlg;

    private Map<String, String> conditionMap = new HashMap<String, String>();

    private EditText etLimitDateStart;
    private EditText etLimitDateEnd;
    private EditText etTaskTitle;

    private Button searchBtn;
    private Button searchAllBtn;
    private Button toTaskAddButton;

    private Spinner spinnerOrderColumns;
    private Spinner spinnerOrderBy;

    private final StatusId[] statusIds = StatusId.values();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        searchDispFlg = false;

        etLimitDateStart = (EditText) findViewById(R.id.search_limit_date_start);
        etLimitDateEnd = (EditText) findViewById(R.id.search_limit_date_end);
        etTaskTitle = (EditText) findViewById(R.id.search_task_title);

        searchBtn = findViewById(R.id.btn_search);
        searchAllBtn = findViewById(R.id.btn_search_all);
        toTaskAddButton = findViewById(R.id.to_task_edit);

        spinnerOrderColumns = (Spinner) findViewById(R.id.sort_columns_spinner);
        spinnerOrderBy = (Spinner) findViewById(R.id.sort_order_by_spinner);

        //タスクを検索してviewにセット
        for (StatusId statusId : statusIds) {
            conditionMap.clear();
            conditionMap.put("status_id", String.valueOf(statusId.getValue()));

            String condition = TaskSQLiteOpenHelper.buildSelectionStr(conditionMap);
            setTaskListView(statusId.getValue(), condition, null);
        }

        searchComponent = (RelativeLayout)findViewById(R.id.search_component);
        searchComponent.setVisibility(View.GONE);

        searchDispSwitch = (TextView)findViewById(R.id.search_disp_switch);
        searchDispSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSearchComponentDisp();
            }
        });

        etLimitDateStart.setKeyListener(null);
        etLimitDateStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    //datepickerを呼び出し
                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.showDateView = (EditText) findViewById(R.id.search_limit_date_start);
                    datePicker.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });

        etLimitDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLimitDateStart.getOnFocusChangeListener().onFocusChange(etLimitDateStart, true);
            }
        });

        etLimitDateEnd.setKeyListener(null);
        etLimitDateEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    //datepickerを呼び出し
                    DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                    datePicker.showDateView = (EditText) findViewById(R.id.search_limit_date_end);
                    datePicker.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });

        etLimitDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLimitDateEnd.getOnFocusChangeListener().onFocusChange(etLimitDateEnd, true);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //入力した検索、ソート条件を元にタスクリストを作り変える

                // Spinnerから選択したステータスを取得
                String selectedOrderColumns = (String) spinnerOrderColumns.getSelectedItem();//ソートする基準となるカラム
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

                //検索＋ソートして、adapterにセットし直す
                //StatusId[] statusIds = StatusId.values();
                for (StatusId statusId : statusIds) {
                    //SQLiteのqueryメソッドに入れるconditionのString作成
                    conditionMap.clear();
                    conditionMap.put("limit_date_start", etLimitDateStart.getText().toString());
                    conditionMap.put("limit_date_end", etLimitDateEnd.getText().toString());
                    conditionMap.put("task_title", etTaskTitle.getText().toString());
                    conditionMap.put("status_id", String.valueOf(statusId.getValue()));
                    String condition = TaskSQLiteOpenHelper.buildSelectionStr(conditionMap);

                    setTaskListView(statusId.getValue(), condition, order);
                }

                switchSearchComponentDisp();
            }
        });

        //全件検索
        searchAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (StatusId statusId : statusIds) {
                    conditionMap.clear();
                    conditionMap.put("status_id", String.valueOf(statusId.getValue()));

                    String condition = TaskSQLiteOpenHelper.buildSelectionStr(conditionMap);
                    setTaskListView(statusId.getValue(), condition, null);
                }

                switchSearchComponentDisp();
            }
        });

        toTaskAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditActivity();
            }
        });
    }

    private void switchSearchComponentDisp(){
        if(searchDispFlg){
            searchComponent.setVisibility(View.GONE);
            searchDispSwitch.setText("検索、ソート条件を表示");
        }
        else{
            searchComponent.setVisibility(View.VISIBLE);
            searchDispSwitch.setText("検索、ソート条件を非表示");
        }

        searchDispFlg = !searchDispFlg;
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
