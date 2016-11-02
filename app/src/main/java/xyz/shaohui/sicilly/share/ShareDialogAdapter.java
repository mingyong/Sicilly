package xyz.shaohui.sicilly.share;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import xyz.shaohui.sicilly.R;

/**
 * Created by shaohui on 2016/11/1.
 */

public class ShareDialogAdapter extends RecyclerView.Adapter<ShareDialogAdapter.ShareDialogItem> {

    private List<ShareIconItem> mDataList;

    @Override
    public ShareDialogItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareDialogItem(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_share_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ShareDialogItem holder, int position) {
        ShareIconItem item = mDataList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ShareDialogItem extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView mImageView;

        @BindView(R.id.item_title)
        TextView mTitle;

        public ShareDialogItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
