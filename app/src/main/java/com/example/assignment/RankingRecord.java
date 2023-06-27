package com.example.assignment;

import android.os.Parcel;
import android.os.Parcelable;

public class RankingRecord implements Parcelable {

    private int rank;
    private String Name;
    private int Moves;

    public RankingRecord(int rank, String name, int moves) {
        this.rank = rank;
        this.Name = Name;
        this.Moves = Moves;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return Name;
    }

    public int getMoves() {
        return Moves;
    }

    // Parcelable 接口方法
    public void setRank(int rank) {
        this.rank = rank;
    }

    protected RankingRecord(Parcel in) {
        rank = in.readInt();
        Name = in.readString();
        Moves = in.readInt();
    }

    public static final Creator<RankingRecord> CREATOR = new Creator<RankingRecord>() {
        @Override
        public RankingRecord createFromParcel(Parcel in) {
            return new RankingRecord(in);
        }

        @Override
        public RankingRecord[] newArray(int size) {
            return new RankingRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rank);
        dest.writeString(Name);
        dest.writeInt(Moves);
    }
}