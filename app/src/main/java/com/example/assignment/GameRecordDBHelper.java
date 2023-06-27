package com.example.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GameRecordDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "gameRecords.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "GamesLog";
    public static final String GAME_ID = "gameID";
    public static final String PLAY_DATE = "playDate";
    public static final String PLAY_TIME = "playTime";
    public static final String MOVES = "moves";

    public GameRecordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + "(" +
                GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAY_DATE + " TEXT, " +
                PLAY_TIME + " INTEGER, " +
                MOVES + " INTEGER" + ")";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<GameRecord> getAllGameRecords() {
        List<GameRecord> gameRecords = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String playDate = cursor.getString(cursor.getColumnIndex(PLAY_DATE));
                    String playTime = cursor.getString(cursor.getColumnIndex(PLAY_TIME));
                    int moves = cursor.getInt(cursor.getColumnIndex(MOVES));

                    GameRecord gameRecord = new GameRecord(playDate, playTime, moves);
                    gameRecords.add(gameRecord);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return gameRecords;
    }
}
