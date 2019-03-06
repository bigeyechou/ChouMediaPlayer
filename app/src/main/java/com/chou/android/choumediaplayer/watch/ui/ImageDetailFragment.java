package com.chou.android.choumediaplayer.watch.ui;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.watch.widget.AdjustImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    @Bind(R.id.image)
    AdjustImageView image;
    @Bind(R.id.frame_total)
    FrameLayout frameTotal;
    @Bind(R.id.progress)
    ProgressBar progress;
    private String mImageUrl;

    private boolean isNewCreate = false, isVisible = false;//是否第一次加载完成，是否可见。

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser) {
            initData();
        } else {
            isNewCreate = false;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mImageUrl = bundle != null ? bundle.getString("url", "") : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.watch_image_detail_fragment,
                container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isNewCreate = true;//布局新创建
        initData();
    }


    private void initData() {
        if (!isVisible || !isNewCreate) {
            return;
        }
        if (onImageListener != null) {
            onImageListener.onInit();
        }
//        image.setImageURI(Uri.parse(mImageUrl));

        ControllerListener controllerListener = new BaseControllerListener() {
            @Override
            public void onFinalImageSet(String id, @Nullable Object imageInfo, @Nullable Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                progress.setVisibility(View.GONE);
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(Uri.parse(mImageUrl)).build();
        image.setController(controller);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public interface OnImageListener {
        void onInit();
    }

    public void setOnImageListener(OnImageListener onImageListener) {
        this.onImageListener = onImageListener;
    }

    private OnImageListener onImageListener;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}