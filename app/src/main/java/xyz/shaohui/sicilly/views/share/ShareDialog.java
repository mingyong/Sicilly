package xyz.shaohui.sicilly.views.share;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistashareutil.VistaShareUtil;
import me.shaohui.vistashareutil.share.ShareListener;
import xyz.shaohui.sicilly.R;

/**
 * Created by shaohui on 2016/11/1.
 */

public class ShareDialog extends BaseBottomDialog {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_WEB = 3;

    private static final String WX_ID = "wxe57249751eecd1f5";

    private static final String WEIBO_ID = "3116987924";

    private static final String QQ_ID = "101361383";

    private VistaShareUtil mVistaShareUtil;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    public static void shareText(FragmentManager fragmentManager, String text) {
        ShareDialog dialog = new ShareDialog();
        Bundle args = new Bundle();
        args.putInt("type", TYPE_TEXT);
        args.putString("data", text);
        dialog.setArguments(args);
        dialog.show(fragmentManager);
    }

    public static void shareImage(FragmentManager fragmentManager, String path) {
        ShareDialog dialog = new ShareDialog();
        Bundle args = new Bundle();
        args.putInt("type", TYPE_IMAGE);
        args.putString("data", path);
        dialog.setArguments(args);
        dialog.show(fragmentManager);
    }

    public static void shareUrl(FragmentManager fragmentManager, String title, String url) {
        ShareDialog dialog = new ShareDialog();
        Bundle args = new Bundle();
        args.putInt("type", TYPE_WEB);
        args.putString("data", url);
        args.putString("title", title);
        dialog.setArguments(args);
        dialog.show(fragmentManager);
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
        ShareAdapter adapter = new ShareAdapter(getContext(), getShareListener());
        mRecyclerView.setAdapter(adapter);
    }

    private ShareItemListener getShareListener() {
        return new ShareItemListener(mVistaShareUtil, getContext()) {
            @Override
            int getType() {
                return getArguments().getInt("type", TYPE_TEXT);
            }

            @Override
            String getData() {
                return getArguments().getString("data");
            }

            @Override
            String getTitle() {
                return getArguments().getString("title");
            }
        };
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }

    private class ShareAdapter extends RecyclerView.Adapter<ShareViewHolder> {

        private List<ShareItem> mShareItems;

        private final ShareItemListener mShareItemListener;

        ShareAdapter(Context context, ShareItemListener listener) {
            mShareItems = new ArrayList<>();
            Resources resources = context.getResources();
            mShareItems.add(new ShareItem("饭否", resources.getDrawable(R.mipmap.share_fan)));
            mShareItems.add(new ShareItem("微信", resources.getDrawable(R.mipmap.share_wechat)));
            mShareItems.add(new ShareItem("朋友圈", resources.getDrawable(R.mipmap.share_moment)));
            mShareItems.add(new ShareItem("微博", resources.getDrawable(R.mipmap.share_weibo)));
            mShareItems.add(new ShareItem("QQ", resources.getDrawable(R.mipmap.share_qq)));
            mShareItems.add(new ShareItem("QQ空间", resources.getDrawable(R.mipmap.share_qzone)));
            mShareItems.add(new ShareItem(listener.getExtraActionTitle(),
                    resources.getDrawable(R.mipmap.share_card)));
            mShareItems.add(new ShareItem("更多", resources.getDrawable(R.mipmap.share_more)));

            mShareItemListener = listener;
        }

        @Override
        public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShareViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dialog_share_item, parent, false), mShareItemListener);
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

        private final ShareItemListener mListener;

        public ShareViewHolder(View itemView, ShareItemListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;

            itemView.setOnClickListener(v -> {
                int position = (int) v.getTag();
                mListener.opShare(position);
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
