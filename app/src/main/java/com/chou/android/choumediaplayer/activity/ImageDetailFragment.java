package com.chou.android.choumediaplayer.activity;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.photozoom.PhotoView;
import com.chou.android.choumediaplayer.photozoom.PhotoViewAttacher;
import java.io.File;

/**
 * 项目名称：hewk1.3
 * 类描述：
 * 创建人：lh
 * 创建时间：2016/7/16 11:53
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private PhotoView mImageView;
    private PhotoViewAttacher mAttacher;

    /**
     * 图片保存
     */
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private View parentView;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (PhotoView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher((ImageView) mImageView);
        mAttacher.setZoomable(true);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
                pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);


                return true;
            }
        });

        mAttacher.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {
                Toast.makeText(getActivity(),"rect:"+rect.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        initPopupWindow();
        return v;
    }
    public void initPopupWindow(){
        pop = new PopupWindow(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.image_save_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        parentView = (RelativeLayout)ll_popup.getParent();
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);


        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveImageToLocal();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageView.setImageURI(Uri.parse(mImageUrl));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 保存图片到本地
     */
    public void saveImageToLocal(){



    }
}
