package com.example.assignment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay, btnGameRanking, btnYourRecords, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnGameRanking = findViewById(R.id.btnGameRanking);
        btnYourRecords = findViewById(R.id.btnYourRecords);
        btnClose = findViewById(R.id.btnClose);

        btnPlay.setOnClickListener(this);
        btnGameRanking.setOnClickListener(this);
        btnYourRecords.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlay:
                startGame();
                break;
            case R.id.btnGameRanking:
                showGameRanking();
                break;
            case R.id.btnYourRecords:
                showYourRecords();
                break;
            case R.id.btnClose:
                closeApp();
                break;
        }
    }

    private void startGame() {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    private void showGameRanking() {
        // 创建一个异步任务以执行网络请求和 JSON 数据的处理
        DownloadRankingTask downloadRankingTask = new DownloadRankingTask();
        downloadRankingTask.execute();
    }

    private void showYourRecords() {
        // 在这里添加从本地数据库加载游戏记录并显示的逻辑
        Intent intent = new Intent(MainActivity.this, YourRecordsActivity.class);
        startActivity(intent);
    }

    private void closeApp() {
        finish();
    }

    private class DownloadRankingTask extends AsyncTask<Void, Void, List<RankingRecord>> {

        @Override
        protected List<RankingRecord> doInBackground(Void... voids) {
            // 在后台线程中进行网络请求和 JSON 数据的处理
            // 模拟从服务器下载 JSON 数据
            String jsonData = "[{\"rank\": 1, \"name\": \"John\", \"moves\": 10}, {\"rank\": 2, \"name\": \"Emily\", \"moves\": 15}, {\"rank\": 3, \"name\": \"Michael\", \"moves\": 20}]";

            // 解析 JSON 数据并返回排名记录的列表
            Gson gson = new Gson();
            Type listType = new TypeToken<List<RankingRecord>>() {}.getType();
            return gson.fromJson(jsonData, listType);
        }

        @Override
        protected void onPostExecute(List<RankingRecord> rankingRecords) {
            // 在主线程中更新 UI，显示排行榜
            Intent intent = new Intent(MainActivity.this, RankingActivity.class);
            intent.putParcelableArrayListExtra("rankingRecords", new ArrayList<>(rankingRecords));
            startActivity(intent);
        }
    }
}
