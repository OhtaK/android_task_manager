package com.example.kota.task_manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

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

        for (int i = 0; i < cursor.getCount(); i++) {
            sbuilder.append(cursor.getString(0));
            sbuilder.append(",");
            sbuilder.append(cursor.getString(1));
            sbuilder.append(",");
            sbuilder.append(cursor.getString(2));
            sbuilder.append(",");
            sbuilder.append(cursor.getString(3));
            sbuilder.append(",");
            sbuilder.append(cursor.getString(4));
            sbuilder.append("点\n\n");
            cursor.moveToNext();
        }

        cursor.close();

        // 変数textViewに結果を表示
        textView = findViewById(R.id.task_text);
        textView.setText(sbuilder.toString());

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
