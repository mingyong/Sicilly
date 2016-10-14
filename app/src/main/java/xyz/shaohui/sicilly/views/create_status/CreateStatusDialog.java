package xyz.shaohui.sicilly.views.create_status;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.io.File;
import javax.inject.Inject;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.sicillylib.utils.ToastUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.DataManager;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.FileUtils;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.utils.ImageUtils;
import xyz.shaohui.sicilly.views.friend_list.FriendListActivity;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;

/**
 * Created by shaohui on 16/9/30.
 */

@FragmentWithArgs
public class CreateStatusDialog extends BaseBottomDialog {

    private final int CODE_AT = 1;
    private final int CODE_CAMREA = 2;
    private final int CODE_PHOTO = 3;

    @BindView(R.id.status_text)
    EditText statusText;
    @BindView(R.id.status_image)
    ImageView statusImage;
    @BindView(R.id.action_close_image)
    ImageButton actionCloseImage;

    private Uri imageFileUri;
    private String mLocalImagePath;

    private DialogController mController;

    private StatusAPI mStatusService;

    @Arg
    int mType;

    @Arg(required = false)
    Status mOriginStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        mStatusService = mController.getRetrofit().create(StatusAPI.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogController) {
            mController = (DialogController) context;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_create_status;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        statusText.post(() -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                    Service.INPUT_METHOD_SERVICE);
            imm.showSoftInput(statusText, 0);
        });

        switch (mType) {
            case CreateStatusActivity.TYPE_REPLY:
                statusText.setText(String.format("@%s ", mOriginStatus.user().screen_name()));
                statusText.setSelection(statusText.length());
                break;
            case CreateStatusActivity.TYPE_REPOST:
                statusText.setText(String.format("转@%s %s", mOriginStatus.user().screen_name(),
                        HtmlUtils.cleanAllTag(mOriginStatus.text())));
                statusText.setSelection(0);
                break;
        }
    }

    @OnClick(R.id.action_camera)
    void actionCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis());

        imageFileUri = getActivity().getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (imageFileUri != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, CODE_CAMREA);
            } else {
                ToastUtils.showToast(getActivity(), R.string.no_intent_to_take_photo);
            }
        } else {
            ToastUtils.showToast(getActivity(), R.string.save_photo_fail);
        }
    }

    @OnClick(R.id.action_photo)
    void actionPhoto() {
        Intent intent =
                new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODE_PHOTO);
    }

    @OnClick(R.id.action_at)
    void actionAt() {
        startActivityForResult(
                FriendListActivity.newIntent(getContext(), FriendListActivity.TYPE_TINY), CODE_AT);
    }

    @OnClick(R.id.action_close_image)
    void closeImage() {
        actionCloseImage.setVisibility(View.GONE);
        statusImage.setVisibility(View.GONE);
        mLocalImagePath = null;
    }

    @OnClick(R.id.action_send)
    void actionSend() {
        if (TextUtils.isEmpty(statusText.getText())) {
            return;
        }

        sendStatus(statusText.getText().toString(), mLocalImagePath, mOriginStatus, mType);
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Service.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String realPath;
            switch (requestCode) {
                case CODE_AT:

                    break;
                case CODE_CAMREA:
                    realPath = FileUtils.getPath(getActivity(), imageFileUri);
                    showImage(realPath);
                    mLocalImagePath = realPath;
                    break;
                case CODE_PHOTO:
                    if (data != null && data.getData() != null) {
                        imageFileUri = data.getData();
                        realPath = FileUtils.getPath(getActivity(), imageFileUri);

                        showImage(realPath);
                        mLocalImagePath = realPath;
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showImage(String path) {
        if (path != null) {
            Glide.with(getContext()).load(new File(path)).into(statusImage);
        }
        statusImage.setVisibility(View.VISIBLE);
        actionCloseImage.setVisibility(View.VISIBLE);
    }

    // send

    public void sendStatus(String text, String path, Status originStatus, int type) {
        if (TextUtils.isEmpty(path)) {
            if (originStatus != null && type != CreateStatusActivity.TYPE_NONE) {
                replyOrRepostStatus(text, originStatus, type);
            } else {
                sendTextStatus(text);
            }
        } else {
            sendTextWithPhoto(text, path);
        }

        dismiss();
    }

    private void sendTextStatus(String text) {
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        Intent failureIntent =
                CreateStatusActivity.newIntent(SicillyApplication.getContext(), text, null);
        DataManager.sendStatus(mStatusService.createStatus(status, null, null), failureIntent);
    }

    private void replyOrRepostStatus(String text, Status originStatus, int type) {
        Observable<Status> observable;
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        switch (type) {
            case CreateStatusActivity.TYPE_REPLY:
                observable = mStatusService.createStatus(status, originStatus.id(), null);
                break;
            case CreateStatusActivity.TYPE_REPOST:
                observable = mStatusService.createStatus(status, null, originStatus.id());
                break;
            default:
                observable = mStatusService.createStatus(status, null, null);
        }
        Intent failureIntent =
                CreateStatusActivity.newIntent(SicillyApplication.getContext(), originStatus, type);
        DataManager.sendStatus(observable, failureIntent);
    }

    private void sendTextWithPhoto(String text, String path) {
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), text);
        File pic = new File(path);
        //compress image here
        File compressedFile = null;
        try {
            compressedFile = ImageUtils.compressImage(pic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pic = (compressedFile != null && compressedFile.length() > 0) ? compressedFile : pic;
        RequestBody photo = RequestBody.create(MediaType.parse("multipart/form-data"), pic);
        Intent failureIntent =
                CreateStatusActivity.newIntent(SicillyApplication.getContext(), text, path);
        DataManager.sendStatus(mStatusService.createStatusWithPhoto(status, photo), failureIntent);
    }
}
