package mobiledev.unb.ca.bappit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StartMenuActivity extends AppCompatActivity {

    ImageView startButton;
    Button settingsButton;
    Button highScoresButton;
    Button tutorialButton;
    Button practiceButton;
    View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        mainView = getWindow().getDecorView();

        startButton = (ImageView) findViewById(R.id.start_btn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartMenuActivity.this, GameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        settingsButton = (Button) findViewById(R.id.settings_btn);
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (StartMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        highScoresButton = (Button) findViewById(R.id.highscore_btn);
        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartMenuActivity.this, HighScoresActivity.class);
                startActivity(intent);
            }
        });

        tutorialButton = (Button) findViewById(R.id.tutorial_btn);
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartMenuActivity.this, TutorialMenuActivity.class);
                startActivity(intent);
            }
        });

        practiceButton = (Button) findViewById(R.id.practice_button);
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartMenuActivity.this, PracticeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//FIXME like in line 30
                startActivity(intent);
            }
        });

        showSystemUI();
    }

    private void showSystemUI() {
        mainView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
