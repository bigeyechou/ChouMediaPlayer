package com.chou.android.choumediaplayer.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.datas.VideoListBean;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * @author : zgz
 * @time :  2018/9/29 0029 16:18
 * @describe :
 **/
public class SupernatantAdapter
    extends BaseQuickAdapter<VideoListBean.ListBean, BaseViewHolder> {
    public SupernatantAdapter(int layoutResId, @Nullable
        List<VideoListBean.ListBean> data) {
        super(layoutResId, data);
    }


    @Override protected void convert(BaseViewHolder helper, VideoListBean.ListBean item) {
        final SimpleDraweeView headImage = helper.getView(R.id.iv_head_sup);
        headImage.setImageURI(Uri.parse(item.getUser().getIcon()));
        helper.setText(R.id.tv_name_sup, item.getUser().getUsername());
        helper.setText(R.id.tv_look_sup, item.getVideo_play_nums());
        helper.setText(R.id.tv_like_sup, item.getVideo_like_nums());
        helper.setText(R.id.tv_collect_sup, item.getVideo_collect_nums());
        SimpleDraweeView focusBg = helper.getView(R.id.iv_video_cover_sup);
        focusBg.setImageURI(Uri.parse(item.getVideo_cover()));

        SpannableString spannableString = new SpannableString(
            "#" + item.getDance_name() + "# " + item.getVideo_title());
        //设置颜色
        if (item.getDance_name()!=null&&item.getDance_name().length()>0){
            spannableString.setSpan(
                new ForegroundColorSpan(mContext.getResources().getColor(R.color.mainStyle)), 0,
                item.getDance_name().length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_title_sup, spannableString);
        }else {
            helper.setText(R.id.tv_title_sup, item.getVideo_title());
        }
    }
}
