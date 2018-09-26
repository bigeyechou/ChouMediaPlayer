package com.chou.android.choumediaplayer.utils.navigation;

import android.content.Context;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

/**
 * @author wxmylife
 */
public class PagerTitleView extends ColorTransitionPagerTitleView implements IPagerTitleView {

    public PagerTitleView(Context context) {
        super(context);
    }


    @Override public void onSelected(int index, int totalCount) {
        setTextSize(20);
    }


    @Override public void onDeselected(int index, int totalCount) {
        setTextSize(18);
    }

}



