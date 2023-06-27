// RankingActivity.java
package com.example.assignment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private ListView rankingListView;
    private RankingAdapter rankingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.rankingListView);
        rankingAdapter = new RankingAdapter(this, new ArrayList<RankingRecord>());
        rankingListView.setAdapter(rankingAdapter);


        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::goBack);

        // Download JSON data from API server and parse it
        downloadRankingData();
    }

    private void downloadRankingData() {
        String apiURL = "https://ranking-mobileasignment-wlicpnigvf.cn-hongkong.fcapp.run";

        // Create an AsyncTask to perform the network request and JSON data processing
        DownloadRankingTask downloadRankingTask = new DownloadRankingTask();
        downloadRankingTask.execute(apiURL);
    }

    // Parse the JSON data and return a list of ranking records
    private List<RankingRecord> parseRankingJson(String jsonData) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<RankingRecord>>() {}.getType();
        return gson.fromJson(jsonData, listType);
    }


    public void goBack(View view) {
        finish(); // Close the current activity and return to the previous activity (main page)
    }

    private class DownloadRankingTask extends AsyncTask<String, Void, List<RankingRecord>> {

        @Override
        protected List<RankingRecord> doInBackground(String... urls) {
            String urlString = urls[0];
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                Log.d("DownloadRankingTask", "Response Code: " + responseCode); // Log the response code

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder stringBuilder = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    // Print the JSON keys
                    String jsonString = stringBuilder.toString();
                    try {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                Log.d("JSON Keys", "Key: " + key);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Parse the JSON data and return a list of ranking records
                    List<RankingRecord> rankingRecords = parseRankingJson(jsonString);

                    // Print the contents of each RankingRecord
                    for (RankingRecord record : rankingRecords) {
                        Log.d("DownloadRankingTask", "Parsed record: Name=" + record.getName() + ", Moves=" + record.getMoves());
                    }

                    return rankingRecords;
                } else {
                    // Network request failed
                    // You can add handling for network request failure here
                    Log.e("DownloadRankingTask", "Network request failed with response code: " + responseCode);
                    return new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // You can add handling for exceptions here
                return new ArrayList<>();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<RankingRecord> rankingRecords) {
            Log.d("DownloadRankingTask", "Received ranking records: " + rankingRecords.size()); // Log the size of the rankingRecords list

            // Sort the ranking records based on the "Moves" property (ascending order)
            Collections.sort(rankingRecords, (record1, record2) -> record1.getMoves() - record2.getMoves());

// Set the rank for each record
            for (int i = 0; i < rankingRecords.size(); i++) {
                rankingRecords.get(i).setRank(i + 1);  // Assuming the rank starts from 1
            }

            // Pass the ranking records list to the adapter and update the adapter's dataset
            rankingAdapter.setRankingRecords(rankingRecords);
            Log.d("DownloadRankingTask", "Updated ranking records in adapter: " + rankingAdapter.getCount());
            rankingAdapter.notifyDataSetChanged();
            Log.d("DownloadRankingTask", "Notified adapter for data set change.");

        }

    }

}
