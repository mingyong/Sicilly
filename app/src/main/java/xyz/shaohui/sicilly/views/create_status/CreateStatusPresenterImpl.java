package xyz.shaohui.sicilly.views.create_status;

import android.content.Intent;
import android.text.TextUtils;
import java.io.File;
import javax.inject.Inject;
import me.shaohui.advancedluban.Luban;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.DataManager;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.create_status.mvp.CreateStatusPresenter;

/**
 * Created by shaohui on 16/9/11.
 */

public class CreateStatusPresenterImpl extends CreateStatusPresenter {

    StatusAPI statusService;

    @Inject
    CreateStatusPresenterImpl(StatusAPI statusService) {
        this.statusService = statusService;
    }

    @Override
    public void sendStatus(String text, String path, Status originStatus, int type) {
        if (TextUtils.isEmpty(path)) {
            if (originStatus != null && type != CreateStatusActivity.TYPE_NONE) {
                replyOrRepostStatus(text, originStatus, type);
            } else {
                sendTextStatus(text);
            }
        } else {
            sendTextWithPhoto(text, path);
        }
        if (isViewAttached()) {
            getView().finish();
        }
    }

    private void sendTextStatus(String text) {
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        Intent failureIntent =
                CreateStatusActivity.newIntent(SicillyApplication.getContext(), text, null);
        DataManager.sendStatus(statusService.createStatus(status, null, null), failureIntent);
    }

    private void replyOrRepostStatus(String text, Status originStatus, int type) {
        Observable<Status> observable;
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        switch (type) {
            case CreateStatusActivity.TYPE_REPLY:
                observable = statusService.createStatus(status, originStatus.id(), null);
                break;
            case CreateStatusActivity.TYPE_REPOST:
                observable = statusService.createStatus(status, null, originStatus.id());
                break;
            default:
                observable = statusService.createStatus(status, null, null);
        }
        Intent failureIntent =
                CreateStatusActivity.newIntent(SicillyApplication.getContext(), originStatus, type);
        DataManager.sendStatus(observable, failureIntent);
    }

    private void sendTextWithPhoto(String text, String path) {
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        final File pic = new File(path);
        //compress image here
        Luban.get(SicillyApplication.getContext())
                .load(pic)
                .setMaxSize(path.toLowerCase().endsWith(".gif") ? 2000 : 3000)
                .putGear(Luban.CUSTOM_GEAR)
                .asObservable()
                .subscribe(file -> {
                    File result = (file != null && file.length() > 0) ? file : pic;
                    RequestBody photo =
                            RequestBody.create(MediaType.parse("multipart/form-data"), result);
                    Intent failureIntent =
                            CreateStatusActivity.newIntent(SicillyApplication.getContext(), text,
                                    path);
                    DataManager.sendStatus(statusService.createStatusWithPhoto(status, photo),
                            failureIntent);
                }, RxUtils.ignoreError);
    }
}
