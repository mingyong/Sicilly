package xyz.shaohui.sicilly.ui.fragments;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.utils.HtmlParse;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;


public class StatusDetailFragment extends Fragment {

    @Bind(R.id.recycler)RecyclerView recyclerView;

    private String id;

    private RetrofitService service;

    public static StatusDetailFragment newInstance(String statusId) {
        StatusDetailFragment fragment = new StatusDetailFragment();
        Bundle args = new Bundle();
        args.putString("id", statusId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString("id");
        service = SicillyFactory.getRetrofitService();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchStatusInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status_detail, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    private void fetchStatusInfo() {
        service.getStatusService().showStatus(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        setUpInfo(jsonObject);
                    }
                });
    }

    private void setUpInfo(JsonObject json) {
        JsonObject userJson = json.get("user").getAsJsonObject();

    }

    @OnClick(R.id.action_favorite)
    void createFavorite() {
        MyToast.showToast(getContext(), "收藏");
    }

    @OnClick(R.id.action_repost)
    void repostStatus() {
        MyToast.showToast(getContext(), "转发");
    }

    @OnClick(R.id.action_reply)
    void replyStatus() {
        MyToast.showToast(getContext(), "回复");
    }



}
