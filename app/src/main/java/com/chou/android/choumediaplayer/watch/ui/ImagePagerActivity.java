package com.chou.android.choumediaplayer.watch.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.watch.adapter.ImagePagerAdapter;
import com.chou.android.choumediaplayer.watch.widget.DragViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 图片查看器
 */
public class ImagePagerActivity extends AppCompatActivity {
    public static final String STATE_POSITION = "STATE_POSITION";
    private static final String EXTRA_IMAGE_INDEX = "image_index";
    private static final String EXTRA_IMAGE_URLS = "image_urls";
    @Bind(R.id.pager)
    DragViewPager pager;
    @Bind(R.id.indicator)
    TextView indicator;
    private ImagePagerAdapter mAdapter;
    private ArrayList<String> mImgs;

    private int pagerPosition;


    public static void startImagePage(Activity context, ArrayList<String> urls, int pos, @Nullable View view) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,从数据库中或网络中获取
        intent.putExtra(EXTRA_IMAGE_URLS, urls);
        intent.putExtra(EXTRA_IMAGE_INDEX, pos);
        ActivityOptionsCompat compat;
        if (view == null) {
            compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context);
        } else {
            compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, "img");
        }
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // 隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置使用分享元素
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.image_detail_page);
        ButterKnife.bind(this);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);

        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION, 0);
        }

        initView();


    }

    private void initView() {
        mImgs = getIntent().getStringArrayListExtra(
                EXTRA_IMAGE_URLS);

        pager.setIAnimClose(new DragViewPager.IAnimClose() {
            @Override
            public void onPictureClick() {
                transitionFinish();
            }

            @Override
            public void onPictureRelease(View view) {
                transitionFinish();
            }
        });
        pager.setOffscreenPageLimit(mImgs.size());

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mImgs, pager);
//        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, pager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        pager.getCurrentItem() + 1, pager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        pager.setCurrentItem(pagerPosition);

        setEnterSharedElementCallback(new SharedElementCallback() {

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                View view = mAdapter.getItem(pager.getCurrentItem()).getView();
                sharedElements.clear();
                sharedElements.put("img", view);
            }
        });

    }

    private void transitionFinish() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(STATE_POSITION, pager.getCurrentItem());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        ActivityCompat.finishAfterTransition(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }


    @Override
    public void onBackPressed() {
        transitionFinish();
    }

}
