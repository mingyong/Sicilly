package xyz.shaohui.sicilly.data.database;

import android.content.Context;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import rx.schedulers.Schedulers;

/**
 * Created by shaohui on 2016/10/23.
 */

public class BriteDatabaseProvider {

    public static BriteDatabase instance(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        return sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }

}
