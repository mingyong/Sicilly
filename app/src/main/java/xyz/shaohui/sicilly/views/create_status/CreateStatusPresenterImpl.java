package xyz.shaohui.sicilly.views.create_status;

import android.text.TextUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import java.io.File;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ImageUtils;
import xyz.shaohui.sicilly.views.create_status.mvp.CreateStatusPresenter;

/**
 * Created by shaohui on 16/9/11.
 */

public class CreateStatusPresenterImpl extends CreateStatusPresenter {

    StatusAPI statusService ;

    @Inject
    CreateStatusPresenterImpl(StatusAPI statusService) {
        this.statusService = statusService;
    }

    @Override
    public void sendStatus(String text, String path) {
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        if (TextUtils.isEmpty(path)) {
            statusService.createStatus(status, null, null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (isViewAttached()) {
                            getView().sendSuccess();
                        }
                    }, throwable -> getView().sendFailure());
        } else {
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
}
