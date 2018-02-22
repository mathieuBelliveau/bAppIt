package mobiledev.unb.ca.bappit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FinalScoreActivity extends AppCompatActivity {

    public static final String FINAL_SCORE = "score";

    private TextView displayScoreText;
    private Button replayBtn;
    private Button mainMenuBtn;
    private Button saveScoreBtn;
    private EditText nameEditText;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        dbHelper = new DBHelper(this);

        final int finalScore = getIntent().getIntExtra(FINAL_SCORE, 0);

        displayScoreText = (TextView) findViewById(R.id.score_display_txt);
        displayScoreText.setText(String.valueOf(finalScore));

        nameEditText = (EditText) findViewById(R.id.name_edit_text);

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


    }

    private void replayGame() {
        Intent intent = new Intent(FinalScoreActivity.this, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void saveScore(int finalScore) {
        String name = nameEditText.getText().toString();
        StoreScoreTask task = new StoreScoreTask();

        if (name.length() != 0) {
            task.execute(name, String.valueOf(finalScore));
        }
        else {
            task.execute("-----", String.valueOf(finalScore));
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

        }
    }
}
