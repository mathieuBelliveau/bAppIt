package mobiledev.unb.ca.bappit;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class FinalScoreActivity extends AppCompatActivity {

    public static final String FINAL_SCORE = "score";

    private Button replayBtn;
    private Button mainMenuBtn;
    private Button saveScoreBtn;
    private Button highScoreBtn;
    private EditText nameEditText;
    private TextView highScoreText;
    private TextView enterNameText;

    private int finalScore;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        dbHelper = new DBHelper(this);

        finalScore = getIntent().getIntExtra(FINAL_SCORE, 0);

        TextView displayScoreText = (TextView) findViewById(R.id.score_display_txt);
        displayScoreText.setText(String.valueOf(finalScore));

        highScoreText = (TextView) findViewById(R.id.highScoreText);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        enterNameText = (TextView) findViewById(R.id.enterNameText);

        replayBtn = (Button) findViewById(R.id.replay_btn);
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replayGame();
            }
        });

        saveScoreBtn = (Button) findViewById(R.id.save_score_btn);
        saveScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveScore(finalScore);
            }
        });

        mainMenuBtn = (Button) findViewById(R.id.main_menu_btn);
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalScoreActivity.this, StartMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        highScoreBtn = (Button) findViewById(R.id.hiScoreButton);
        highScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalScoreActivity.this, HighScoresActivity.class);
                startActivity(intent);
            }
        });

        setButtonVisibility(false);

        QueryTopTenTask task = new QueryTopTenTask();
        task.execute();

    }

    private void replayGame() {
        Intent intent = new Intent(FinalScoreActivity.this, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    private void saveScore(int finalScore) {
        String name = nameEditText.getText().toString();
        StoreScoreTask task = new StoreScoreTask();

        if (name.length() != 0) {
            task.execute(name, String.valueOf(finalScore));

            View view = FinalScoreActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }
        else {
            Toast toast = Toast.makeText(FinalScoreActivity.this, "Please Enter Name", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class StoreScoreTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... params) {
            // Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DBHelper.NAME, params[0]);
            values.put(DBHelper.SCORE, Integer.parseInt(params[1]));

            // Insert the new row
            db.insert(DBHelper.TABLE_NAME, null, values);

            return null;
        }

        protected void onPostExecute(Void result) {
            setButtonVisibility(false);
        }
    }

    private class QueryTopTenTask extends AsyncTask<String, Void, Cursor> {
        protected Cursor doInBackground(String... params) {

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            return db.query(
                    DBHelper.TABLE_NAME,
                    DBHelper.COLUMNS,
                    null,
                    null,
                    null,
                    null,
                    DBHelper.SCORE + " DESC",
                    "10"
            );
        }

        protected void onPostExecute(Cursor result) {
            boolean isInTopTen = false;
            if (result.getCount() == 10) {
                result.moveToLast();
                int score = result.getInt(result.getColumnIndexOrThrow(DBHelper.SCORE));
                isInTopTen = finalScore > score;
            }

            if (result.getCount() < 10 || isInTopTen) {
                highScoreText.setText("New Top\nTen Score!");
                setButtonVisibility(true);
            }
            else {
                result.moveToFirst();
                int bestScore = result.getInt(result.getColumnIndexOrThrow(DBHelper.SCORE));
                highScoreText.setText("Best\n" + bestScore);
            }
        }
    }

    private void setButtonVisibility(boolean isHighScore) {
        if(isHighScore) {
            replayBtn.setVisibility(View.GONE);
            highScoreBtn.setVisibility(View.GONE);
            mainMenuBtn.setVisibility(View.GONE);

            saveScoreBtn.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.VISIBLE);
            enterNameText.setVisibility(View.VISIBLE);
        }
        else {
            nameEditText.setVisibility(View.GONE);
            saveScoreBtn.setVisibility(View.GONE);
            enterNameText.setVisibility(View.GONE);

            replayBtn.setVisibility(View.VISIBLE);
            highScoreBtn.setVisibility(View.VISIBLE);
            mainMenuBtn.setVisibility(View.VISIBLE);
        }
    }
}
