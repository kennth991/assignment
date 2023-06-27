// RankingAdapter.java
package com.example.assignment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RankingAdapter extends BaseAdapter {

    private Context context;
    private List<RankingRecord> rankingRecords;

    public RankingAdapter(Context context, List<RankingRecord> rankingRecords) {
        this.context = context;
        this.rankingRecords = rankingRecords;
    }

    public void setRankingRecords(List<RankingRecord> rankingRecords) {
        this.rankingRecords = rankingRecords;
    }

    @Override
    public int getCount() {
        return rankingRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return rankingRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("RankingAdapter", "Getting view for position: " + position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_ranking, parent, false);
        }

        TextView rankTextView = convertView.findViewById(R.id.rankTextView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView moveTextView = convertView.findViewById(R.id.moveTextView);

        RankingRecord rankingRecord = rankingRecords.get(position);

        rankTextView.setText("Rank " + rankingRecord.getRank());
        nameTextView.setText(rankingRecord.getName());
        moveTextView.setText(String.valueOf(rankingRecord.getMoves()));
        Log.d("RankingAdapter", "Got view for position: " + position + ", Name: " + rankingRecord.getName() + ", Moves: " + rankingRecord.getMoves());
        return convertView;
    }

}