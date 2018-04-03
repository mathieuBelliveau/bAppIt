package mobiledev.unb.ca.bappit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "high" +
            "scores";
    final static String _ID = "_id";
    public static final String NAME = "name";
    public static final String SCORE = "score";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NAME + " TEXT, " +
                    SCORE + " INT);";
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "mydb";
    final static String[] COLUMNS = { _ID, NAME, SCORE };

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
    }


}
