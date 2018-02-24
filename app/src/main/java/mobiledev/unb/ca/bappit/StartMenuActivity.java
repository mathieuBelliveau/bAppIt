package mobiledev.unb.ca.bappit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class StartMenuActivity extends AppCompatActivity {

    Button startButton;
    Button settingsButton;
    View mainView;

    private Button highScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        mainView = getWindow().getDecorView();

        startButton = (Button) findViewById(R.id.start_btn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartMenuActivity.this, GameActivity.class);
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

        showSystemUI();
    }

    private void showSystemUI() {
        mainView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
