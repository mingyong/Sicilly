package xyz.shaohui.sicilly.views.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistashareutil.VistaShareUtil;
import me.shaohui.vistashareutil.share.SharePlatform;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.utils.SimpleUtils;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;

/**
 * Created by shaohui on 2016/11/28.
 */

public abstract class ShareItemListener {

    private final VistaShareUtil mVistaShareUtil;
    private final Context mContext;

    protected ShareItemListener(VistaShareUtil vistaShareUtil, Context context) {
        mVistaShareUtil = vistaShareUtil;
        mContext = context;
    }

    void opShare(int position) {
        switch (position) {
            case 0:
                shareToFan();
                break;
            case 1:
                share(SharePlatform.WX);
                break;
            case 2:
                share(SharePlatform.WX_TIMELINE);
                break;
            case 3:
                share(SharePlatform.WEIBO);
                break;
            case 4:
                share(SharePlatform.QQ);
                break;
            case 5:
                share(SharePlatform.QZONE);
                break;
            case 6:
                opExtraAction();
                break;
            case 7:
                share(SharePlatform.DEFAULT);
                break;
        }
    }

    private void share(int platform) {
        switch (getType()) {
            case ShareDialog.TYPE_TEXT:
                mVistaShareUtil.shareText(platform, getData());
                break;
            case ShareDialog.TYPE_IMAGE:
                mVistaShareUtil.shareImage(platform, getData());
                break;
            case ShareDialog.TYPE_WEB:
                Bitmap bitmap = BitmapFactory.decodeStream(
                        mContext.getResources().openRawResource(R.raw.icon_square));
                mVistaShareUtil.shareMedia(platform, getTitle(), getTitle(), getData(),
                        bitmap);
                break;
        }
    }

    private void shareToFan() {
        switch (getType()) {
            case ShareDialog.TYPE_TEXT:
                mContext.startActivity(
                        CreateStatusActivity.newIntent(SicillyApplication.getContext(), getData(),
                                null));
                break;
            case ShareDialog.TYPE_IMAGE:
                mContext.startActivity(
                        CreateStatusActivity.newIntent(SicillyApplication.getContext(), null,
                                getData()));
                break;
            case ShareDialog.TYPE_WEB:
                mContext.startActivity(
                        CreateStatusActivity.newIntent(SicillyApplication.getContext(),
                                getTitle() + getData(), null));
        }
    }

    abstract int getType();

    public String getExtraActionTitle() {
        switch (getType()) {
            case ShareDialog.TYPE_IMAGE:
                return "保存图片";
            case ShareDialog.TYPE_WEB:
                return "复制链接";
            default:
            case ShareDialog.TYPE_TEXT:
                return "复制文字";
        }
    }

    public void opExtraAction() {
        switch (getType()) {
            case ShareDialog.TYPE_TEXT:
            case ShareDialog.TYPE_WEB:
                SimpleUtils.copyText(SicillyApplication.getContext(), getData());
                break;
            case ShareDialog.TYPE_IMAGE:
                ToastUtils.showToast(SicillyApplication.getContext(), "TODO 保存图片");
                break;
        }
    }

    abstract String getData();

    abstract String getTitle();
}
