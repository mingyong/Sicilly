package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.ui.activities.PhotoActivity;
import xyz.shaohui.sicilly.ui.widgets.RatioImageView;

/**
 * Created by kpt on 16/3/31.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private List<Status> dataList;

    public ImageListAdapter(List<Status> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Status status = dataList.get(position);
        final Context context = holder.image.getContext();
        int limit = 50;

        String text = status.getText().length() > limit ? status.getText().substring(0, limit) +
                "..." : status.getText();
        holder.text.setText(text);

        Picasso.with(context)
                .load(status.getImageUrl())
                .into(holder.image);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(PhotoActivity.newIntent(context,
                        status.getImageLargeUrl(), status.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)RatioImageView image;
        @Bind(R.id.text)TextView text;
        View card;

        public MyViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
            image.setOriginalSize(50, 50);
        }
    }
}
