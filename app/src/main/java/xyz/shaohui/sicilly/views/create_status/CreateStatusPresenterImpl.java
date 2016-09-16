package xyz.shaohui.sicilly.views.create_status;

import android.text.TextUtils;
import java.io.File;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ImageUtils;
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
    }

    private void sendTextStatus(String text) {
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        statusService.createStatus(status, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        getView().sendSuccess();
                    }
                }, throwable -> getView().sendFailure());
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
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        getView().sendSuccess();
                    }
                }, throwable -> getView().sendFailure());
    }

    private void sendTextWithPhoto(String text, String path) {
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        File pic = new File(path);
        //compress image here
        File compressedFile = null;
        try {
            compressedFile = ImageUtils.compressImage(pic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pic = (compressedFile != null && compressedFile.length() > 0) ? compressedFile : pic;
        RequestBody photo = RequestBody.create(MediaType.parse("multipart/form-data"), pic);
        statusService.createStatusWithPhoto(status, photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        getView().sendSuccess();
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        throwable.printStackTrace();
                        getView().sendFailure();
                    }
                });
    }
}
