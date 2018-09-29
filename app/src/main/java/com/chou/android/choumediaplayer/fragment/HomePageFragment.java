package com.chou.android.choumediaplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.utils.navigation.PageInfo;
import com.chou.android.choumediaplayer.utils.navigation.PagerTitleView;
import com.chou.android.mediaplayerlibrary.VideoPlayerManager;
import com.chou.android.mediaplayerlibrary.utils.ChouPlayerUtil;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

/**
 * @author : zgz
 * @time :  2018/9/25 0025 17:34
 * @describe :首页
 **/
public class HomePageFragment extends BaseFragment {

    public static final String TAB_TAG = "BUNDLE_KEY_REQUEST_TYPE";
    @Bind(R.id.tab) MagicIndicator tab;
    @Bind(R.id.base_viewPager) ViewPager baseViewPager;
    private CommonNavigator mCommonNavigator;

    protected PageInfo[] mPageInfos;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPageInfos = getPages();

        baseViewPager.setOffscreenPageLimit(mPageInfos.length);
        baseViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), mPageInfos));
        mCommonNavigator = new CommonNavigator(getContext());
        mCommonNavigator.setAdjustMode(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mPageInfos.length;
            }


            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                PagerTitleView titleView = new PagerTitleView(
                    context);
                titleView.setText(mPageInfos[index].title);
                titleView.setNormalColor(
                    ContextCompat.getColor(context, R.color.darkgrey));
                titleView.setSelectedColor(
                    ContextCompat.getColor(context, R.color.black));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseViewPager.setCurrentItem(index);
                    }
                });
                return titleView;
            }


            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(ChouPlayerUtil.dp2px(getContext(),20));
                indicator.setRoundRadius(ChouPlayerUtil.dp2px(getContext(),1));
                indicator.setColors(ContextCompat.getColor(context, R.color.mainStyle));
                return indicator;
            }
        });
        tab.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(tab, baseViewPager);
        baseViewPager.setCurrentItem(0, true);
    }


    private PageInfo[] getPages() {
        final String[] pages = { "列表播放", "抖音", "悬浮播放"};
        return new PageInfo[] {
            new PageInfo(pages[0], ListVideoFragment.class, getBundle(0)),
            new PageInfo(pages[1], DouYinVideoFragment.class, getBundle(1)),
        };
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            VideoPlayerManager.instance().pauseVideoPlayer();
        }
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAB_TAG, catalog);
        return bundle;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private PageInfo[] mInfoList;
        private Fragment mCurFragment;


        public ViewPagerAdapter(FragmentManager fm, PageInfo[] infoList) {
            super(fm);
            mInfoList = infoList;
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                mCurFragment = (Fragment) object;
            }
        }


        public Fragment getCurFragment() {
            return mCurFragment;
        }


        @Override
        public Fragment getItem(int position) {
            PageInfo info = mInfoList[position];
            return Fragment.instantiate(getContext(), info.clx.getName(), info.args);
        }


        @Override
        public int getCount() {
            return mInfoList.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mInfoList[position].title;
        }


        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}