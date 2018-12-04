package com.chou.android.choumediaplayer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chou.android.choumediaplayer.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> bannerImgs = new ArrayList<>();
    private List<String> bannerTags = new ArrayList<>();

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
        bannerTags.add("第一张");
        bannerTags.add("第二张");
        bannerTags.add("第三张");
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
        banner.setAutoPlayAble(bannerImgs.size()> 1);
        banner.setData(R.layout.banner_view,bannerImgs,bannerTags);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}