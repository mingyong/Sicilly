package xyz.shaohui.sicilly.views.create_status;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.utils.FileUtils;
import xyz.shaohui.sicilly.views.create_status.di.CreateStatusComponent;
import xyz.shaohui.sicilly.views.create_status.mvp.CreateStatusPresenter;
import xyz.shaohui.sicilly.views.create_status.mvp.CreateStatusView;

/**
 * Created by shaohui on 16/9/11.
 */

public class CreateStatusFragment extends BaseFragment<CreateStatusView, CreateStatusPresenter>
        implements CreateStatusView {

    private static final int TAKE_PHOTO = 1;
    private static final int SELECT_PHOTO = 2;
    private static final int REQUEST_PERMISSION = 3;

    @BindView(R.id.status_image)
    ImageView statusImage;
    @BindView(R.id.status_text)
    EditText statusText;
    @BindView(R.id.text_count)
    TextView textCount;

    @Inject
    EventBus mBus;

    private Uri imageFileUri;
    private String mLocalImagePath;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        CreateStatusComponent component = getComponent(CreateStatusComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_create_status;
    }

    void showSelectPhotoDialog() {
        new MaterialDialog.Builder(getActivity()).items(R.array.select_photo)
                .itemsCallback((dialog, itemView, position, text) -> selectPicture(text))
                .show();
    }

    @Override
    public void bindViews(View view) {
        super.bindViews(view);
        statusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= SicillyFactory.TEXT_COUNT) {
                    textCount.setTextColor(getResources().getColor(R.color.gray_tag));
                    textCount.setText(String.valueOf(SicillyFactory.TEXT_COUNT - s.length()));
                } else {
                    textCount.setText(String.valueOf(s.length() - SicillyFactory.TEXT_COUNT));
                    textCount.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
    }

    @OnClick(R.id.action_photo)
    void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ToastUtils.showToast(getActivity(), R.string.no_permission_to_photo);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        } else {
            showSelectPhotoDialog();
        }
    }

    private void selectPicture(CharSequence text) {
        if (TextUtils.equals(text, getString(R.string.select_photo_gallery))) {
            Intent intent =
                    new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, SELECT_PHOTO);
        } else if (TextUtils.equals(text, getString(R.string.select_photo_take))) {
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
                    startActivityForResult(intent, TAKE_PHOTO);
                } else {
                    ToastUtils.showToast(getActivity(), R.string.no_intent_to_take_photo);
                }
            } else {
                ToastUtils.showToast(getActivity(), R.string.save_photo_fail);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String realPath;
            switch (requestCode) {
                case SELECT_PHOTO:
                    if (data != null && data.getData() != null) {
                        imageFileUri = data.getData();
                        realPath = FileUtils.getPath(getActivity(), imageFileUri);
                        statusImage.setImageURI(FileUtils.path2Uri(realPath));
                        statusImage.setVisibility(View.VISIBLE);
                        mLocalImagePath = realPath;
                    }
                    break;
                case TAKE_PHOTO:
                    realPath = FileUtils.getPath(getActivity(), imageFileUri);
                    statusImage.setImageURI(FileUtils.path2Uri(realPath));
                    statusImage.setVisibility(View.VISIBLE);
                    mLocalImagePath = realPath;
                    break;
                default:
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSelectPhotoDialog();
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showPicture() {

    }

    @Override
    public void sendSuccess() {
        ToastUtils.showToast(getActivity(), "发送成功");
    }

    @Override
    public void sendFailure() {

    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }

    @OnClick(R.id.action_at)
    void actionAt() {

    }

    @OnClick(R.id.action_submit)
    void actionSubmit() {
        presenter.sendStatus(statusText.getText().toString(), mLocalImagePath);
    }
}
