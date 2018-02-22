package mobiledev.unb.ca.bappit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity {
   // private ArrayList<HighScoreCard> mHScoreCards;
    private ListView mHighScoreListView;
    private TextView mResultsTextView;
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        mHighScoreListView = (ListView) findViewById(R.id.hs_listview);
        mResultsTextView = (TextView) findViewById(R.id.hs_results_text);
        mDbHelper = new DBHelper(this);

        QueryTask task = new QueryTask();
        task.execute();
    }

    private class QueryTask extends AsyncTask<String, Void, Cursor> {
        protected Cursor doInBackground(String... params) {

            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            return db.rawQuery(
                    "SELECT * FROM " + DBHelper.TABLE_NAME + ";",
                    null
            );
        }

        protected void onPostExecute(Cursor result) {
            String[] projection = {
                    DBHelper.NAME,
                    DBHelper.SCORE
            };

            if(result.getCount() > 0) {
                SimpleCursorAdapter adapter = new SimpleCursorAdapter (
                        HighScoresActivity.this,
                        R.layout.hs_list,
                        result,
                        projection,
                        new int[] {R.id.name_textview, R.id.score_textview},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                );
                mHighScoreListView.setAdapter(adapter);
            }
            else {
                mResultsTextView.setText("No results found");
            }
        }
    }


}
