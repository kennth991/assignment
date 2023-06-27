package com.example.assignment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton[][] buttons;
    private int[][] buttonValues;
    private boolean[][] buttonFlipped;
    private int rows = 4;
    private int columns = 2;
    private int moveCount = 0;
    private int pairsFound = 0;
    private boolean[][] buttonMatched;
    private TextView moveCountTextView;
    private List<Integer> numbers;
    private boolean isClickable = true; // 按钮是否可点击的标志
    private GameRecordDBHelper dbHelper; // 声明 GameRecordDBHelper 对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);
        buttonMatched = new boolean[rows][columns];

        rows = 4; // 设置按钮行数
        columns = 2; // 设置按钮列数

        buttons = new ImageButton[rows][columns];
        buttonValues = new int[rows][columns];
        buttonFlipped = new boolean[rows][columns];

        numbers = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4));
        Collections.shuffle(numbers);

        initializeButtons();
        moveCountTextView = findViewById(R.id.moveCountTextView);
        updateMoveCount();

        dbHelper = new GameRecordDBHelper(this); // 初始化 GameRecordDBHelper 对象
    }

    private void initializeButtons() {
        // 计算每个按钮的宽度和高度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int buttonWidth = screenWidth / columns;
        int buttonHeight = screenHeight / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String buttonId = "btn_" + i + j;
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setOnClickListener(this);

                // 设置按钮的宽度和高度
                buttons[i][j].getLayoutParams().width = buttonWidth;
                buttons[i][j].getLayoutParams().height = buttonHeight;

                // 设置按钮的缩放类型为FIT_CENTER
                buttons[i][j].setScaleType(ImageButton.ScaleType.FIT_CENTER);

                // 设置所有按钮的初始图像为背面图像
                buttons[i][j].setImageResource(R.drawable.blackpink);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!isClickable) {
            return;
        }

        Log.d("GameActivity", "Button clicked");
        ImageButton button = (ImageButton) v;
        int row = -1, column = -1;

        // 查找按钮在数组中的位置
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (buttons[i][j].equals(button)) {
                    row = i;
                    column = j;
                    break;
                }
            }
        }

        if (row != -1 && column != -1 && !buttonMatched[row][column]) {
            if (!buttonFlipped[row][column]) {
                // 如果按钮未翻开，则翻开它
                buttonFlipped[row][column] = true;
                int number = numbers.get(row * columns + column);
                // 设置按钮的正面图像资源
                button.setImageResource(getImageResource(number));
                buttonValues[row][column] = number;
            }

            int flippedCount = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (buttonFlipped[i][j] && !buttonMatched[i][j]) {
                        flippedCount++;
                    }
                }
            }

            if (flippedCount == 2) {
                moveCount++;
                updateMoveCount();
                checkMatchingPairs();
            }
        }
    }

    private void checkMatchingPairs() {
        List<Integer> selectedValues = new ArrayList<>();
        int[][] selectedPositions = new int[2][2];
        int selectedCount = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (buttonFlipped[i][j] && !buttonMatched[i][j] && selectedCount < 2) {
                    selectedValues.add(buttonValues[i][j]);
                    selectedPositions[selectedCount][0] = i;
                    selectedPositions[selectedCount][1] = j;
                    selectedCount++;
                }
            }
        }

        if (selectedCount == 2) {
            if (!selectedValues.get(0).equals(selectedValues.get(1))) {
                // 延迟一段时间后重置选中的牌
                new Handler().postDelayed(this::resetSelectedButtons, 500);
            } else {
                pairsFound++;
                buttonMatched[selectedPositions[0][0]][selectedPositions[0][1]] = true;
                buttonMatched[selectedPositions[1][0]][selectedPositions[1][1]] = true;
                buttonFlipped[selectedPositions[0][0]][selectedPositions[0][1]] = false;
                buttonFlipped[selectedPositions[1][0]][selectedPositions[1][1]] = false;
            }
        }

        if (pairsFound == (rows * columns) / 2) {
            showGameResult();
        }
    }

    private void resetSelectedButtons() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (buttonFlipped[i][j] && !buttonMatched[i][j]) {
                    buttonFlipped[i][j] = false;
                    // 设置按钮的背面图像资源
                    buttons[i][j].setImageResource(R.drawable.blackpink);
                    buttonValues[i][j] = 0;
                }
            }
        }
        isClickable = true; // 重置选中的牌后将 isClickable 设置为 true
    }

    private void updateMoveCount() {
        moveCountTextView.setText("Moves: " + moveCount);
    }

    private void showGameResult() {
        // 這裡加入保存遊戲紀錄的代碼
        saveGameRecord(moveCount);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("游戏完成!");
        builder.setMessage("你已经完成了游戏，总共用了 " + moveCount + " 步。你想开始新的游戏吗?");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 用户点击"是"按钮，重新开始游戏
                resetGame();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 用户点击"否"按钮，结束游戏
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        new Handler().postDelayed(this::resetGame, 500); // 延迟500毫秒后重置游戏
    }

    private void saveGameRecord(int moves) {
        // 将数据保存到SQLite数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateAndTime = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put(GameRecordDBHelper.PLAY_DATE, currentDateAndTime);
        values.put(GameRecordDBHelper.PLAY_TIME, "00:00:00"); // 需要你自行实现计算游戏时间的功能
        values.put(GameRecordDBHelper.MOVES, moves);
        db.insert(GameRecordDBHelper.TABLE_NAME, null, values);
    }

    private void resetGame() {
        moveCount = 0;
        pairsFound = 0;
        buttonMatched = new boolean[rows][columns];
        buttonFlipped = new boolean[rows][columns];
        Collections.shuffle(numbers);
        initializeButtons();
        updateMoveCount();
        isClickable = true; // 重置游戏后将 isClickable 设置为 true
    }

    private int getImageResource(int number) {
        switch (number) {
            case 1:
                return R.drawable.image1;
            case 2:
                return R.drawable.image2;
            case 3:
                return R.drawable.image3;
            case 4:
                return R.drawable.image4;
            default:
                return R.drawable.blackpink;
        }
    }
}
