package com.example.assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.GameRecord;

import java.util.ArrayList;
import java.util.List;

public class YourRecordsActivity extends AppCompatActivity {

    private ListView recordsListView;
    private RecordsAdapter recordsAdapter;
    private GameRecordDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_records);

        recordsListView = findViewById(R.id.recordsListView);
        recordsAdapter = new RecordsAdapter(this, new ArrayList<GameRecord>());
        recordsListView.setAdapter(recordsAdapter);

        dbHelper = new GameRecordDBHelper(this);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::goBack);

        loadGameRecords();
    }

    private void loadGameRecords() {
        List<GameRecord> gameRecords = dbHelper.getAllGameRecords();
        recordsAdapter.setGameRecords(gameRecords);
        recordsAdapter.notifyDataSetChanged();
    }

    public void goBack(View view) {
        finish();
    }
}
