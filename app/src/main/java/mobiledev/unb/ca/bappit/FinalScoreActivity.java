package mobiledev.unb.ca.bappit;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FinalScoreActivity extends AppCompatActivity {

    public static final String FINAL_SCORE = "score";

    private TextView displayScoreText;
    private Button replayBtn;
    private Button mainMenuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        int finalScore = getIntent().getIntExtra(FINAL_SCORE, 0);
//        Log.i("score", finalScore + "");

        displayScoreText = (TextView) findViewById(R.id.score_display_txt);
        displayScoreText.setText(String.valueOf(finalScore));

        replayBtn = (Button) findViewById(R.id.replay_btn);
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replayGame();
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
}
