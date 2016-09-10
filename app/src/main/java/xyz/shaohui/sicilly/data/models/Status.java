package xyz.shaohui.sicilly.data.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.Date;

@AutoValue
public abstract class Status implements Parcelable {

    static Status create(String repost_user_id, int rawid, String in_reply_to_status_id,
            Status repost_status, Date created_at, boolean truncated, String source,
            String in_reply_to_screen_name, String repost_screen_name, String id, String text,
            boolean favorited, StatusPhoto photo, User user) {

        return new AutoValue_Status(repost_user_id, in_reply_to_status_id, repost_status,
                created_at, truncated, source, in_reply_to_screen_name, repost_user_id, rawid,
                repost_screen_name, id, text, favorited, photo, user);
    }

    public static TypeAdapter<Status> typeAdapter(Gson gson) {
        return new AutoValue_Status.GsonTypeAdapter(gson);
    }

    @Nullable
    public abstract String repost_user_id();

    @Nullable
    public abstract String in_reply_to_status_id();

    @Nullable
    public abstract Status repost_status();

    public abstract Date created_at();

    public abstract boolean truncated();

    public abstract String source();

    @Nullable
    public abstract String in_reply_to_screen_name();

    @Nullable
    public abstract String repost_status_id();

    public abstract int rawid();

    @Nullable
    public abstract String repost_screen_name();

    public abstract String id();

    public abstract String text();

    public abstract boolean favorited();

    @Nullable
    public abstract StatusPhoto photo();

    @Nullable
    public abstract User user();
}
