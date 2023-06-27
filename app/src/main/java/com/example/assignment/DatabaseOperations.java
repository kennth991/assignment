package com.example.assignment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DatabaseOperations extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private GameRecordDBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_records);

        dbHelper = new GameRecordDBHelper(this);
        try {
            db = dbHelper.getWritableDatabase();
            showGameRecords();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void showGameRecords() {
        Cursor cursor = db.query(GameRecordDBHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        String[] fromColumns = {GameRecordDBHelper.PLAY_DATE,
                GameRecordDBHelper.PLAY_TIME,
                GameRecordDBHelper.MOVES};

        int[] toViews = {R.id.tvDate, R.id.tvTime, R.id.tvMoves};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item_game_record,
                cursor,
                fromColumns,
                toViews,
                0);

        ListView listView = findViewById(R.id.recordsListView);
        listView.setAdapter(adapter);
    }

    public void addGameRecord(String playDate, int playTime, int moves) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameRecordDBHelper.PLAY_DATE, playDate);
        values.put(GameRecordDBHelper.PLAY_TIME, playTime);
        values.put(GameRecordDBHelper.MOVES, moves);
        db.insert(GameRecordDBHelper.TABLE_NAME, null, values);

        db.close();
        showGameRecords();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
