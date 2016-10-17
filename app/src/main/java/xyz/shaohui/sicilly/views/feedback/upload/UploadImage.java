package xyz.shaohui.sicilly.views.feedback.upload;

import android.util.Log;
import android.util.Pair;
import com.qiniu.android.storage.UploadManager;
import java.io.File;
import me.shaohui.advancedluban.Luban;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.leanCloud.LeanCloudAPI;
import xyz.shaohui.sicilly.leanCloud.LeanCloudService;
import xyz.shaohui.sicilly.leanCloud.model.QiniuToken;
import xyz.shaohui.sicilly.utils.RxUtils;

/**
 * Created by shaohui on 16/10/17.
 */

public class UploadImage {

    public final static String QINIU_BUCKET = "sicilly";

    public static void toQiniu(File image, String key, UploadListener listener) {
        LeanCloudAPI service = LeanCloudService.getInstance();

        Observable<File> compress = Luban.get(SicillyApplication.getContext())
                .putGear(Luban.THIRD_GEAR)
                .load(image)
                .asObservable();
        Observable.zip(compress, service.requestQiniuToken(), Pair::create)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    QiniuToken token = pair.second.getResults().get(0);
                    upload(pair.first, key, token, listener);
                }, RxUtils.ignoreError);
    }

    private static void upload(File file, String key, QiniuToken token, UploadListener listener) {
        QiniuSign sign = QiniuSign.create(token.getAk(), token.getSk());
        String signToken = sign.uploadToken(QINIU_BUCKET, key, 3600, null, true);
        UploadManager manager = new UploadManager();
        manager.put(file, key, signToken, (key1, info, response) -> {
            listener.complete(key1);
        }, null);
    }

    public interface UploadListener {
        void complete(String key);
    }
}
