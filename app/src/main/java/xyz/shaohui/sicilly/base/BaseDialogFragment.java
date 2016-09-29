package xyz.shaohui.sicilly.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import butterknife.ButterKnife;

/**
 * Created by shaohui on 16/9/22.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        if (window != null) {
            //window.setLayout(100,
            //        100);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(layoutRes(), container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return new Dialog(getActivity(), getTheme())
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        bindView(view);
    }

    @LayoutRes
    public abstract int layoutRes();

    public abstract void bindView(View view);
}
