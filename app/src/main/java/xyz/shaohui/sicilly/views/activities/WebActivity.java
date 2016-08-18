package xyz.shaohui.sicilly.views.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.scrollablelayout.ScrollableLayout;
import xyz.shaohui.sicilly.R;

public class WebActivity extends BaseActivity {

    @BindView(R.id.recycler)RecyclerView recyclerView;
    @BindView(R.id.vista_layout)ScrollableLayout vistaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        initRecycler();

        vistaLayout.getHelper().setCurrentScrollableView(recyclerView);
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_status_index, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

    }

    class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
