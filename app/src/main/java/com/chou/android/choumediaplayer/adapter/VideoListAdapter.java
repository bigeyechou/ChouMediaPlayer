package com.chou.android.choumediaplayer.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chou.android.choumediaplayer.R;
import com.chou.android.choumediaplayer.datas.ShowVideoListBean;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * @author : zgz
 * @time :  2018/9/26 0026 13:33
 * @describe :视频播放列表adapter
 **/
public class VideoListAdapter
    extends BaseMultiItemQuickAdapter<ShowVideoListBean.ListBean, BaseViewHolder> {

    private VideoListHolder holderLand;
    private VideoListHolder holderVertical;


    public VideoListAdapter(@Nullable List<ShowVideoListBean.ListBean> data) {
        super(data);
        addItemType(ShowVideoListBean.ListBean.VIDEO_LAND, R.layout.item_video_list);
        addItemType(ShowVideoListBean.ListBean.VIDEO_VERTICAL, R.layout.item_video_list);
    }


    @Override protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (ShowVideoListBean.ListBean.VIDEO_LAND == viewType) {
            return new VideoListHolder(getItemView(R.layout.item_video_list, parent),
                mContext);
        } else {
            return new VideoListHolder(getItemView(R.layout.item_video_list, parent),
                mContext);
        }
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ShowVideoListBean.ListBean item) {
        if (helper.getItemViewType() == ShowVideoListBean.ListBean.VIDEO_LAND) {
            holderLand = (VideoListHolder) helper;
            holderLand.setDataTag(helper.getPosition(), item.getVideo_href());
        } else {
            holderVertical = (VideoListHolder) helper;
            holderVertical.setDataTag(helper.getPosition(), item.getVideo_href());
        }

        final SimpleDraweeView headImage = helper.getView(R.id.iv_head_video_list);
        headImage.setImageURI(Uri.parse(item.getUser().getIcon()));
        helper.setText(R.id.tv_name_video_list, item.getUser().getUsername());
        helper.setText(R.id.tv_description_video_list, item.getVideo_title());
        helper.setText(R.id.tv_look_video_list, item.getVideo_play_nums());
        helper.setText(R.id.tv_like_video_list, item.getVideo_like_nums());
        helper.setText(R.id.tv_collect_video_list, item.getVideo_collect_nums());
        helper.setTag(R.id.iv_time_video_list,item.getVideo_long_time());
        SimpleDraweeView focusBg = helper.getView(R.id.iv_bg_video_list);
        focusBg.setImageURI(Uri.parse(item.getVideo_cover()));

        SpannableString spannableString = new SpannableString(
            "#" + item.getDance_name() + "# " + item.getVideo_title());
        //设置颜色
        if (item.getDance_name()!=null&&item.getDance_name().length()>0){
            spannableString.setSpan(
                new ForegroundColorSpan(mContext.getResources().getColor(R.color.mainStyle)), 0,
                item.getDance_name().length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_description_video_list, spannableString);
        }else {
            helper.setText(R.id.tv_description_video_list, item.getVideo_title());
        }
        //给子view设置点击事件
        helper.addOnClickListener(R.id.ll_all_video_list);

    }
}
