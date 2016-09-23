package xyz.shaohui.sicilly.data.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by shaohui on 16/9/23.
 */

@AutoValue
public abstract class FanNotification {

    public static TypeAdapter<FanNotification> typeAdapter(Gson gson) {
        return new AutoValue_FanNotification.GsonTypeAdapter(gson);
    }

    public abstract int mentions();

    public abstract int direct_messages();

    public abstract int friend_requests();
}
