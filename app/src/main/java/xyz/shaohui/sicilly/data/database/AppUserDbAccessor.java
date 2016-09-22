package xyz.shaohui.sicilly.data.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.RowMapper;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.AppUser;

/**
 * Created by shaohui on 16/9/19.
 */

public class AppUserDbAccessor {

    private BriteDatabase mBriteDatabase;

    @Inject
    AppUserDbAccessor(BriteDatabase briteDatabase) {
        mBriteDatabase = briteDatabase;
    }

    List<AppUser> selectAll() {
        mBriteDatabase.createQuery(AppUser.TABLE_NAME, AppUser.SELECT_ALL).map(query -> {
            List<AppUser> users = new ArrayList<>();
            Cursor cursor = query.run();
            while (cursor != null && cursor.moveToNext()) {
                users.add(AppUser.MAPPER.map(cursor));
            }
            return users;
        });
        return null;
    }

    public Observable<Cursor> selectCurrentUser() {
        return mBriteDatabase.createQuery(AppUser.TABLE_NAME, AppUser.SELECT_CURRENT)
                .map(SqlBrite.Query::run);
    }

    public void insertUser(AppUser user) {
        mBriteDatabase.insert(AppUser.TABLE_NAME, AppUser.FACTORY.marshal(user).asContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteUser(String userId) {
        mBriteDatabase.delete(AppUser.TABLE_NAME, AppUser.DELETE_BY_ID, userId);
    }

}
