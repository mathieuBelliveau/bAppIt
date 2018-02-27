package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity {

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

//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        db.execSQL("delete from "+ DBHelper.TABLE_NAME);

        QueryTask task = new QueryTask();
        task.execute();
    }

    private class QueryTask extends AsyncTask<String, Void, Cursor> {
        protected Cursor doInBackground(String... params) {

            SQLiteDatabase db = mDbHelper.getReadableDatabase();
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
            String[] projection = {
                    DBHelper.NAME,
                    DBHelper.SCORE,
            };

            if(result.getCount() > 0) {
                ScoreCursorAdaptor adapter = new ScoreCursorAdaptor(HighScoresActivity.this, result);

                View header = getLayoutInflater().inflate(R.layout.hs_list_header, null);
                mHighScoreListView.addHeaderView(header);
                mHighScoreListView.setAdapter(adapter);
                mResultsTextView.setVisibility(View.GONE);
            }
            else {
                mResultsTextView.setText("No high scores yet");
                mResultsTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class ScoreCursorAdaptor extends CursorAdapter {
        public ScoreCursorAdaptor(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.hs_list, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView rankView = (TextView) view.findViewById(R.id.rank_textview);
            TextView nameView = (TextView) view.findViewById(R.id.name_textview);
            TextView scoreView = (TextView) view.findViewById(R.id.score_textview);

            // Extract properties from cursor
            int rank = cursor.getPosition() + 1;
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME));
            int score = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.SCORE));

            // Populate fields with extracted properties
            rankView.setText(String.valueOf(rank));
            nameView.setText(name);
            scoreView.setText(String.valueOf(score));
        }
    }


}
