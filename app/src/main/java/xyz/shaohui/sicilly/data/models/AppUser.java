package xyz.shaohui.sicilly.data.models;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/**
 * Created by shaohui on 16/9/19.
 */

@AutoValue
public abstract class AppUser implements AppUserModel, Parcelable {

    public static AppUser create(@NonNull String id, @NonNull String token,
            @NonNull String token_secret, @NonNull String name, @NonNull String avatar,
            @Nullable Boolean current) {
        return new AutoValue_AppUser(id, token, token_secret, name, avatar, current);
    }

    public final static Factory<AppUser> FACTORY =
            new Factory<>(new AppUserModel.Creator<AppUser>() {
                @Override
                public AppUser create(@NonNull String id, @NonNull String token,
                        @NonNull String token_secret, @NonNull String name, @NonNull String avatar,
                        @Nullable Boolean current) {
                    return new AutoValue_AppUser(id, token, token_secret, name, avatar, current);
                }
            });

    public final static Mapper<AppUser> MAPPER = FACTORY.select_allMapper();
}
