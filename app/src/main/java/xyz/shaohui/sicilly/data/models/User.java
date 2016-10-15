package xyz.shaohui.sicilly.data.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@AutoValue
public abstract class User implements Parcelable {

    public static User create(String birthday, int utc_offset, int friends_count, String gender,
            String profile_background_image_url, int favourites_count, String description,
            Date created_at, boolean is_protected, String screen_name, String profile_link_color,
            String id, String profile_background_color, String profile_image_url_large,
            String profile_sidebar_border_color, String profile_text_color,
            String profile_image_url, String url, boolean profile_background_tile,
            int statuses_count, int followers_count, boolean following, String name,
            String location, String profile_sidebar_fill_color, boolean notifications,
            Status status) {
        return new AutoValue_User(birthday, utc_offset, friends_count, gender,
                profile_background_image_url, favourites_count, description, created_at,
                is_protected, screen_name, profile_link_color, id, profile_background_color,
                profile_image_url_large, profile_sidebar_border_color, profile_text_color,
                profile_image_url, url, profile_background_tile, statuses_count, followers_count,
                following, name, location, profile_sidebar_fill_color, notifications, status);
    }

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }

    public User updateFollow() {
        return new AutoValue_User(birthday(), utc_offset(), friends_count(), gender(),
                profile_background_image_url(), favourites_count(), description(), created_at(),
                is_protected(), screen_name(), profile_link_color(), id(),
                profile_background_color(), profile_image_url_large(),
                profile_sidebar_border_color(), profile_text_color(), profile_image_url(), url(),
                profile_background_tile(), statuses_count(), followers_count(), !following(), name(),
                location(), profile_sidebar_fill_color(), notifications(), status());
    }

    @Nullable
    public abstract String birthday();

    public abstract int utc_offset();

    public abstract int friends_count();

    @Nullable
    public abstract String gender();

    @Nullable
    public abstract String profile_background_image_url();

    public abstract int favourites_count();

    @Nullable
    public abstract String description();

    public abstract Date created_at();

    @SerializedName("protected")
    public abstract boolean is_protected();

    public abstract String screen_name();

    @Nullable
    public abstract String profile_link_color();

    public abstract String id();

    @Nullable
    public abstract String profile_background_color();

    public abstract String profile_image_url_large();

    @Nullable
    public abstract String profile_sidebar_border_color();

    @Nullable
    public abstract String profile_text_color();

    @Nullable
    public abstract String profile_image_url();

    @Nullable
    public abstract String url();

    public abstract boolean profile_background_tile();

    public abstract int statuses_count();

    public abstract int followers_count();

    public abstract boolean following();

    public abstract String name();

    @Nullable
    public abstract String location();

    @Nullable
    public abstract String profile_sidebar_fill_color();

    public abstract boolean notifications();

    @Nullable
    public abstract Status status();
}
