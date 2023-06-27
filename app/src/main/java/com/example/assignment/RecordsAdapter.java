package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RecordsAdapter extends BaseAdapter {

    private Context context;
    private List<GameRecord> gameRecords;

    public RecordsAdapter(Context context, List<GameRecord> gameRecords) {
        this.context = context;
        this.gameRecords = gameRecords;
    }

    public void setGameRecords(List<GameRecord> gameRecords) {
        this.gameRecords = gameRecords;
    }

    @Override
    public int getCount() {
        return gameRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return gameRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_game_record, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        TextView movesTextView = convertView.findViewById(R.id.movesTextView);

        GameRecord gameRecord = gameRecords.get(position);

        dateTextView.setText("Date: " + gameRecord.getPlayDate());
        timeTextView.setText("Time: " + gameRecord.getPlayTime());
        movesTextView.setText("Moves: " + gameRecord.getMoves());

        return convertView;
    }
}
