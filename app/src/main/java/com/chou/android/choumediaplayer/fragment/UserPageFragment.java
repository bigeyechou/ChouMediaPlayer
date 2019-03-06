package com.chou.android.choumediaplayer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.activity.LargeImageActivity;
import com.chou.android.choumediaplayer.watch.ui.ImagePagerActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * @author : zgz
 * @time :  2018/9/25 0025 17:32
 * @describe :我的页面
 **/
public class UserPageFragment extends BaseFragment {
    @Bind(R.id.banner)
    BGABanner banner;

    private ArrayList<String> bannerImgs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userpage_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();

    }

    private void initData() {
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543922495213&di=3cb77a01808f7514b64ffba1fd80c28c&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01946559c5b66ba801218e187b33a3.jpg%402o.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543922677793&di=ffd08239ef4a4991421ca1fd186ebec3&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F018d1b58120836a84a0d304f977625.jpg%402o.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543922694153&di=44213b7099663dc8e034c50b9fe92d32&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01b8c2588082ffa801219c771bf36b.jpg%402o.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524477979306&di=3eb07e9302606048abe13d7b6a2bc601&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201406%2F12%2F20140612211118_YYXAC.jpeg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524133463580&di=1315bc4db30999f00b89ef79c3bb06e5&imgtype=0&src=http%3A%2F%2Fpic36.photophoto.cn%2F20150710%2F0005018721870517_b.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524133463575&di=6221f21bcb761675c5d161ebc53d5948&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201410%2F03%2F20141003112442_AkkuH.thumb.700_0.jpeg");
    }


    private void initView() {

        banner.setDelegate(new BGABanner.Delegate<LinearLayout, String>() {

            @Override
            public void onBannerItemClick(BGABanner banner, LinearLayout itemView, @Nullable String model, int position) {

            }
        });
        banner.setAdapter(new BGABanner.Adapter<LinearLayout, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, LinearLayout itemView, @Nullable String model, int position) {
                SimpleDraweeView simpleDraweeView = itemView.findViewById(R.id.banner_fresco_content);
                simpleDraweeView.setImageURI(Uri.parse(model));
            }
        });
        banner.setAutoPlayAble(bannerImgs.size() > 1);
        banner.setData(R.layout.banner_view, bannerImgs,null);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}