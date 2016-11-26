package xyz.shaohui.sicilly.views.share;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistashareutil.VistaShareUtil;
import me.shaohui.vistashareutil.share.ShareListener;
import me.shaohui.vistashareutil.share.SharePlatform;
import xyz.shaohui.sicilly.R;

/**
 * Created by shaohui on 2016/11/1.
 */

public class ShareDialog extends BaseBottomDialog {

    private static final String WX_ID = "wxe57249751eecd1f5";

    //private static final String WX_ID_PLUS = "wxab4bcc9bc760bf09";

    private static final String WEIBO_ID = "3116987924";

    private static final String QQ_ID = "101361383";

    private VistaShareUtil mVistaShareUtil;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        GridLayoutManager layoutManager =
                new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);

        mVistaShareUtil = VistaShareUtil.init(getActivity());
        mVistaShareUtil.setShareListener(new ShareListener() {
            @Override
            public void shareSuccess() {
                ToastUtils.showToast(getContext(), "分享成功");
            }

            @Override
            public void shareFailure() {
                ToastUtils.showToast(getContext(), "分享失败");
            }

            @Override
            public void shareCancel() {
                ToastUtils.showToast(getContext(), "分享取消");
            }
        });
        VistaShareUtil.setQQId(QQ_ID);
        VistaShareUtil.setWeiboId(WEIBO_ID);
        VistaShareUtil.setWXId(WX_ID);


        mRecyclerView.setLayoutManager(layoutManager);
        ShareAdapter adapter = new ShareAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }

    class ShareAdapter extends RecyclerView.Adapter<ShareViewHolder> {

        private List<ShareItem> mShareItems;

        ShareAdapter(Context context) {
            mShareItems = new ArrayList<>();
            Resources resources = context.getResources();
            mShareItems.add(new ShareItem("微信", resources.getDrawable(R.mipmap.share_wechat)));
            mShareItems.add(new ShareItem("朋友圈", resources.getDrawable(R.mipmap.share_moment)));
            mShareItems.add(new ShareItem("微博", resources.getDrawable(R.mipmap.share_weibo)));
            mShareItems.add(new ShareItem("QQ", resources.getDrawable(R.mipmap.share_qq)));
            mShareItems.add(new ShareItem("QQ空间", resources.getDrawable(R.mipmap.share_qzone)));
            mShareItems.add(new ShareItem("复制文字", resources.getDrawable(R.mipmap.share_card)));
            mShareItems.add(new ShareItem("更多", resources.getDrawable(R.mipmap.share_more)));
            mShareItems.add(new ShareItem("更多", resources.getDrawable(R.mipmap.share_more)));
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

    private String title = "测试消息";

    private String summary = "这是详细信息";

    private String targetUrl = "http://shaohui.me";

    private String imageUrl = "https://pic4.zhimg.com/04ac29e7aef928fd867385286c17f09f_r.jpg";

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
                        mVistaShareUtil.shareText(SharePlatform.WEIBO, summary);
                        break;
                    case 1:
                        mVistaShareUtil.shareImage(SharePlatform.WEIBO, imageUrl);
                        break;
                    case 2:
                        mVistaShareUtil.shareMedia(SharePlatform.WEIBO, summary, summary, targetUrl, imageUrl);
                        break;
                    case 3:
                        mVistaShareUtil.shareImage(SharePlatform.QZONE, imageUrl);
                        break;
                    case 4:
                        mVistaShareUtil.shareImage(SharePlatform.WX, imageUrl);
                        break;
                    case 5:
                        mVistaShareUtil.shareImage(SharePlatform.WX_TIMELINE, imageUrl);
                        break;
                    case 6:
                        mVistaShareUtil.shareText(SharePlatform.WX, summary);
                        break;
                    case 7:
                        //mVistaShareUtil.shareMedia(SharePlatform.WX, summary, summary, targetUrl, imageUrl);
                        mVistaShareUtil.shareImage(SharePlatform.DEFAULT, imageUrl);
                        break;
                }
            });
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
