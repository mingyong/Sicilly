package xyz.shaohui.sicilly.data.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/**
 * Created by shaohui on 16/9/24.
 */
@AutoValue
public abstract class Feedback implements FeedbackModel {

    public static Feedback sendCreate(String text, String uid) {
        return new AutoValue_Feedback(System.currentTimeMillis(), text, uid, true, false,
                true, null);
    }

    public static Feedback sendImage(String text, String uid, String image) {
        return new AutoValue_Feedback(System.currentTimeMillis(), text, uid, true, false,
                true, image);
    }

    public static Feedback receiveCreate(String text, String uid) {
        return new AutoValue_Feedback(System.currentTimeMillis(), text, uid, false, true,
                false, null);
    }

    public Feedback sendSuccess() {
        return new AutoValue_Feedback(id(), text(), uid(), is_right(), !send_success(),
                is_read(), null);
    }

    public Feedback sendImageSuccess(String text) {
        return new AutoValue_Feedback(id(), text, uid(), is_right(), !send_success(),
                is_read(), image());
    }

    public static Factory<Feedback> FACTORY = new Factory<>(new Creator<Feedback>() {
        @Override
        public Feedback create(long id, @NonNull String text, @NonNull String uid,
                @Nullable Boolean is_right, @Nullable Boolean send_success,
                @Nullable Boolean is_read, String image) {
            return new AutoValue_Feedback(id, text, uid, is_right, send_success, is_read, image);
        }
    });

    public static Mapper<Feedback> MAPPER = FACTORY.select_by_uidMapper();
}
