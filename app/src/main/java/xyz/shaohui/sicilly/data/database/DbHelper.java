package xyz.shaohui.sicilly.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.models.Feedback;

/**
 * Created by shaohui on 16/9/19.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "xyz.shaohui.sicilly";

    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AppUser.CREATE_TABLE);
        db.execSQL(Feedback.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
