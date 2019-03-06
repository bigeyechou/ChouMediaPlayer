package cn.bingoogolapple.bgabanner.transformer;

import android.support.v4.view.ViewCompat;
import android.view.View;

public class CardPageTransformer extends BGAPageTransformer{
    private static final float MIN_SCALE_X = 0.8f;
    private static final float MIN_SCALE_Y = 0.8f;

    @Override
    public void transformPage(View page, float position) {
        final int pageWidth = page.getWidth();
        final float scaleFactorX = MIN_SCALE_X + (1 - MIN_SCALE_X) * (1 - Math.abs(position));
        final float scaleFactorY = MIN_SCALE_Y + (1 - MIN_SCALE_Y) * (1 - Math.abs(position));

        if (position < 0) { // [-1,0]
            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactorX);
            page.setScaleY(scaleFactorY);

        } else if (position == 0) {
            page.setScaleX(1);
            page.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactorX);
            page.setScaleY(scaleFactorY);

        }
    }

    @Override
    public void handleInvisiblePage(View view, float position) {
        ViewCompat.setAlpha(view, 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        float scaleFactorX = MIN_SCALE_X + (1 - MIN_SCALE_X) * (1 - Math.abs(position));
        float scaleFactorY = MIN_SCALE_Y + (1 - MIN_SCALE_Y) * (1 - Math.abs(position));
        ViewCompat.setScaleX(view,scaleFactorX);
        ViewCompat.setScaleY(view,scaleFactorY);
    }

    @Override
    public void handleRightPage(View view, float position) {
        float scaleFactorX = MIN_SCALE_X + (1 - MIN_SCALE_X) * (1 - Math.abs(position));
        float scaleFactorY = MIN_SCALE_Y + (1 - MIN_SCALE_Y) * (1 - Math.abs(position));
        ViewCompat.setScaleX(view,scaleFactorX);
        ViewCompat.setScaleY(view,scaleFactorY);
    }

}
