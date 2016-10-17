package xyz.shaohui.sicilly.views.feedback;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Feedback;
import xyz.shaohui.sicilly.utils.FileUtils;
import xyz.shaohui.sicilly.views.feedback.di.FeedbackComponent;
import xyz.shaohui.sicilly.views.feedback.mvp.FeedbackPresenter;
import xyz.shaohui.sicilly.views.feedback.mvp.FeedbackView;

/**
 * Created by shaohui on 16/9/24.
 */

public class FeedbackFragment extends BaseFragment<FeedbackView, FeedbackPresenter>
        implements FeedbackView {

    private static final int CODE_PHOTO = 1;

    @Inject
    EventBus mBus;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    @BindView(R.id.main_edit)
    EditText mEditText;

    private List<Feedback> mDataList;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        FeedbackComponent component = getComponent(FeedbackComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    public void bindViews(View view) {
        mDataList = new ArrayList<>();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        FeedbackAdapter adapter = new FeedbackAdapter(mDataList);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        presenter.loadFeedbackList();
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_submit)
    void btnSubmit() {
        if (TextUtils.isEmpty(mEditText.getText().toString())) {
            return;
        }
        presenter.sendFeedback(mEditText.getText().toString().trim());
        mEditText.setText("");
    }

    @OnClick(R.id.btn_delete)
    void deleteAll() {
        new MaterialDialog.Builder(getActivity()).content(R.string.confirm_delete_feedback)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive(((dialog, which) -> presenter.deleteAll()))
                .show();
    }

    @OnClick(R.id.action_image)
    void actionImage() {
        Intent intent =
                new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == CODE_PHOTO) {
            if (data != null && data.getData() != null) {
                Uri imageFileUri = data.getData();
                String localPath = FileUtils.getPath(getActivity(), imageFileUri);
                presenter.uploadImage(localPath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showFeedback(List<Feedback> feedbacks) {
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        mDataList.addAll(feedbacks);
        mRecyclerView.notifyDataSetChanged();
        mRecyclerView.getRecycler().smoothScrollToPosition(0);
    }

    @Override
    public void showFeedbackFail() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void sendFeedbackFail(String text) {
        ToastUtils.showToast(getActivity(), "发送失败, 请重试");
    }
}
