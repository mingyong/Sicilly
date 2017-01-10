package xyz.shaohui.sicilly.views.share;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;
import me.shaohui.sicillylib.utils.ToastUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.utils.FileUtils;
import xyz.shaohui.sicilly.utils.SimpleUtils;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;

/**
 * Created by shaohui on 2016/11/1.
 */

@FragmentWithArgs
public class ShareDialog extends BaseBottomDialog {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_WEB = 3;

    public static final ShareListener SHARE_LISTENER = new ShareListener() {
        @Override
        public void shareSuccess() {
            ToastUtils.showToast(SicillyApplication.getContext(), R.string.share_success);
        }

        @Override
        public void shareFailure(Exception e) {
            ToastUtils.showToast(SicillyApplication.getContext(), R.string.share_failure);
        }

        @Override
        public void shareCancel() {
            ToastUtils.showToast(SicillyApplication.getContext(), R.string.share_cancel);
        }
    };

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Arg
    int mType;

    @Arg(required = false)
    String mText;

    @Arg(required = false)
    String mImagePath;

    @Arg(required = false)
    String mTitle;

    @Arg(required = false)
    String mUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        GridLayoutManager layoutManager =
                new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        ShareAdapter adapter = new ShareAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }

    private class ShareAdapter extends RecyclerView.Adapter<ShareViewHolder> {

        private List<ShareItem> mShareItems;

        ShareAdapter(Context context) {
            mShareItems = new ArrayList<>();
            Resources resources = context.getResources();
            mShareItems.add(new ShareItem("饭否", resources.getDrawable(R.mipmap.share_fan)));
            mShareItems.add(new ShareItem("微信", resources.getDrawable(R.mipmap.share_wechat)));
            mShareItems.add(new ShareItem("朋友圈", resources.getDrawable(R.mipmap.share_moment)));
            mShareItems.add(new ShareItem("微博", resources.getDrawable(R.mipmap.share_weibo)));
            mShareItems.add(new ShareItem("QQ", resources.getDrawable(R.mipmap.share_qq)));
            mShareItems.add(new ShareItem("QQ空间", resources.getDrawable(R.mipmap.share_qzone)));
            mShareItems.add(new ShareItem(getExtraActionTitle(),
                    resources.getDrawable(R.mipmap.share_card)));
            mShareItems.add(new ShareItem("更多", resources.getDrawable(R.mipmap.share_more)));
        }

        private String getExtraActionTitle() {
            switch (mType) {
                case TYPE_IMAGE:
                    return getString(R.string.share_save_image);
                case TYPE_TEXT:
                    return getString(R.string.share_copy_text);
                case TYPE_WEB:
                    return getString(R.string.share_copy_url);
            }
            return null;
        }

        @Override
        public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShareViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dialog_share_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ShareViewHolder holder, int position) {
            ShareItem item = mShareItems.get(position);

            holder.title.setText(item.getTitle());
            holder.icon.setImageDrawable(item.getIcon());

            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return 8;
        }
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_title)
        TextView title;

        @BindView(R.id.item_image)
        ImageView icon;

        public ShareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int position = (int) v.getTag();
                switch (position) {
                    case 0:
                        shareToFan();
                        break;
                    case 1:
                        socialShare(SharePlatform.WX);
                        break;
                    case 2:
                        socialShare(SharePlatform.WX_TIMELINE);
                        break;
                    case 3:
                        socialShare(SharePlatform.WEIBO);
                        break;
                    case 4:
                        socialShare(SharePlatform.QQ);
                        break;
                    case 5:
                        socialShare(SharePlatform.QZONE);
                        break;
                    case 6:
                        opExtraAction();
                        break;
                    case 7:
                        socialShare(SharePlatform.DEFAULT);
                        break;
                }
                // 隐藏dialog
                dismiss();
            });
        }

        private void shareToFan() {
            switch (mType) {
                case TYPE_TEXT:
                    getContext().startActivity(
                            CreateStatusActivity.newIntent(SicillyApplication.getContext(), mText,
                                    null));
                    break;
                case TYPE_IMAGE:
                    getContext().startActivity(
                            CreateStatusActivity.newIntent(SicillyApplication.getContext(), null,
                                    mImagePath));
                    break;
                case TYPE_WEB:
                    getContext().startActivity(
                            CreateStatusActivity.newIntent(SicillyApplication.getContext(),
                                    mTitle + " " + mUrl, null));
                    break;
            }
        }

        private void socialShare(int platform) {
            switch (mType) {
                case TYPE_IMAGE:
                    ShareUtil.shareImage(getContext(), platform, mImagePath, SHARE_LISTENER);
                    break;
                case TYPE_TEXT:
                    ShareUtil.shareText(getContext(), platform, mText, SHARE_LISTENER);
                    break;
                case TYPE_WEB:
                    InputStream input = getResources().openRawResource(R.raw.icon_square);
                    Bitmap thumb = BitmapFactory.decodeStream(input);
                    try {
                        input.close();
                    } catch (IOException e) {
                        ErrorUtils.catchException(e);
                    }
                    ShareUtil.shareMedia(getContext(), platform, mTitle, mTitle, mUrl, thumb,
                            SHARE_LISTENER);
                    break;
            }
        }

        void opExtraAction() {
            switch (mType) {
                case ShareDialog.TYPE_TEXT:
                    SimpleUtils.copyText(SicillyApplication.getContext(), mTitle);
                    break;
                case ShareDialog.TYPE_WEB:
                    SimpleUtils.copyText(SicillyApplication.getContext(), mUrl);
                    break;
                case ShareDialog.TYPE_IMAGE:
                    Observable.fromCallable(
                            () -> FileUtils.saveImage(SicillyApplication.getContext(),
                                    new File(mImagePath)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(path -> {
                                ToastUtils.showToast(SicillyApplication.getContext(), String.format(
                                        SicillyApplication.getContext()
                                                .getString(R.string.save_image), path));
                            }, throwable -> {
                                ToastUtils.showToast(SicillyApplication.getContext(),
                                        R.string.save_image_fail);
                                ErrorUtils.catchException(throwable);
                            });
                    break;
            }
        }
    }

    class ShareItem {

        public ShareItem(String title, Drawable icon) {
            this.title = title;
            this.icon = icon;
        }

        String title;

        Drawable icon;

        public String getTitle() {
            return title;
        }

        public Drawable getIcon() {
            return icon;
        }
    }
}
