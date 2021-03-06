package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.preferences.DraftSP;
import xyz.shaohui.sicilly.data.services.user.UserService;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.imageUtils.ImageUtils;

public class CreateStatusActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolbar;
    @Bind(R.id.main_edit)EditText mainEdit;
    @Bind(R.id.main_send)TextView mainSend;
    @Bind(R.id.image)ImageView image;
    
    private String rId;
    private String rText;
    private int type;

    private String mCurrentPhotoPath;

    public static final int TYPE_NULL = 1;
    public static final int TYPE_REPOST = 2;
    public static final int TYPE_REPLY = 3;

    public static final int CODE_REQUEST_PICK = 1;
    public static final int CODE_REQUEST_TAKE = 2;

    public static Intent newIntent(Context context,int type, String repostId, String repostText) {
        Intent intent = new Intent(context, CreateStatusActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("r_id", repostId);
        intent.putExtra("r_text", repostText);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initToolbar();

        getIntentData();

        showInput();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitEdit();
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        switch (type) {
            case TYPE_NULL:
                String text = DraftSP.loadDraft(this);
                mainEdit.setText(text);
                mainEdit.setSelection(text.length());
                break;
            case TYPE_REPLY:
                rId = intent.getStringExtra("r_id");
                rText = intent.getStringExtra("r_text");
                mainEdit.setText(rText);
                mainEdit.setSelection(rText.length());
                break;
            case TYPE_REPOST:
                rId = intent.getStringExtra("r_id");
                rText = intent.getStringExtra("r_text");
                mainEdit.setText(rText);
                break;
        }
    }

    private void showInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) mainEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(mainEdit, 0);
            }
        },100);
    }

    private void quitEdit() {
        if (!TextUtils.isEmpty(mainEdit.getText()) && type == TYPE_NULL) {
            showNoticeDialog();
        } else {
            finish();
        }
    }

    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
        builder.setTitle("保存草稿")
                .setMessage("是否保存草稿?")
                .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DraftSP.saveDraft(getApplicationContext(), mainEdit.getText().toString());
                        MyToast.showToast(getApplicationContext(), "已保存");
                        finish();
                    }
                });
        builder.create().show();
    }

    @OnClick(R.id.main_send)
    void clickSend() {
        switch (type) {
            case TYPE_NULL:
                createStatus();
                break;
            case TYPE_REPOST:
                repostStatus();
                break;
            case TYPE_REPLY:
                replyStatus();
                break;
            default:
                createStatus();
                break;
        }
    }

    /**
     * 新建,转发,回复status
     */
    private void repostStatus() {
        UserService.repostStatus(mainEdit.getText().toString(), rId, new UserService.CallBack() {
            @Override
            public void success() {
                MyToast.showToast(getApplicationContext(), "成功");
            }

            @Override
            public void failure() {
                MyToast.showToast(getApplicationContext(), "发送失败");
            }
        });
    }

    private void createStatus() {
        UserService.CallBack callBack = new UserService.CallBack() {
            @Override
            public void success() {
                MyToast.showToast(getApplicationContext(), "发送成功" + mainEdit.getText().toString());
            }

            @Override
            public void failure() {
                MyToast.showToast(getApplicationContext(), "发送失败, 请重试");
            }
        };
        if (TextUtils.isEmpty(mCurrentPhotoPath)) {
            UserService.createStatus(mainEdit.getText().toString(), callBack);
        } else {
            UserService.createStatusImg(Uri.parse(mCurrentPhotoPath), mainEdit.getText().toString(), callBack);
        }

    }

    private void replyStatus() {
        UserService.replyStatus(mainEdit.getText().toString(), rId, new UserService.CallBack() {
            @Override
            public void success() {
                MyToast.showToast(getApplicationContext(), "回复成功");
            }

            @Override
            public void failure() {
                MyToast.showToast(getApplicationContext(), "回复失败");
            }
        });
    }

    @OnClick(R.id.take_photo)
    void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            }
            startActivityForResult(intent, CODE_REQUEST_TAKE);
        } else {
            MyToast.showToast(this, "Are you sure your phone can take a photo?");
        }

    }

    @OnClick(R.id.pick_photo)
    void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        intent = Intent.createChooser(intent, "选择一张图片");
        startActivityForResult(intent, CODE_REQUEST_PICK);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storgeDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storgeDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 通知gallery更新图库
    private void galleryAddPic() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(mCurrentPhotoPath));
        this.sendBroadcast(intent);
    }

    // The path of Pick photo
    private String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor;
        if (Build.VERSION.SDK_INT > 19 && uri.toString().split("\\.").length > 2) {

            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String sel = MediaStore.Images.Media._ID + "=?";

            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, sel, new String[]{ id }, null);

        } else {
            cursor = getContentResolver().query(uri, projection, null, null, null);
        }
        String path = null;
        try {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUEST_TAKE && resultCode == RESULT_OK) {
            galleryAddPic();
            displayImg();
        }

        if (requestCode == CODE_REQUEST_PICK && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                MyToast.showToast(this, "图片格式不对, 请重新选择");
                return;
            }

            mCurrentPhotoPath = getPath(data.getData());
            displayImg();
        }
    }

    private void displayImg() {
        if (TextUtils.isEmpty(mCurrentPhotoPath)) {
           return;
        }

        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(ImageUtils.compSize(mCurrentPhotoPath));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(mainEdit.getText().toString())) {
            showNoticeDialog();
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
