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
        return new AutoValue_Feedback(System.currentTimeMillis(), text, uid, true, false, true);
    }

    public static Feedback receiveCreate(String text, String uid) {
        return new AutoValue_Feedback(System.currentTimeMillis(), text, uid, false, true, false);
    }

    public Feedback sendSuccess() {
        return new AutoValue_Feedback(id(), text(), uid(), is_right(), !send_success(), is_read());
    }

    public static Factory<Feedback> FACTORY = new Factory<>(new Creator<Feedback>() {
        @Override
        public Feedback create(long id, @NonNull String text, @NonNull String uid,
                @Nullable Boolean is_right, @Nullable Boolean send_success,
                @Nullable Boolean is_read) {
            return new AutoValue_Feedback(id, text, uid, is_right, send_success, is_read);
        }
    });

    public static Mapper<Feedback> MAPPER = FACTORY.select_by_uidMapper();

}
