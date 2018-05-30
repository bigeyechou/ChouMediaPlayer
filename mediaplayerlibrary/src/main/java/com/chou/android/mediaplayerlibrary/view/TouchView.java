package com.chou.android.mediaplayerlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wxmylife on 2018/2/6.
 */

public class TouchView extends android.support.v7.widget.AppCompatImageView {

  private OnTouchEventListener onTouchEventListener;

  private boolean isTouch;

  private boolean isEnable=true;


  public TouchView(Context context) {
    super(context);
  }


  public TouchView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }


  @Override public boolean onTouchEvent(MotionEvent event) {
    if (!isEnable) {
      return false;
    }

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (onTouchEventListener != null && !isTouch) {
          onTouchEventListener.onTouchDown();
          isTouch = true;
        }
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        if (onTouchEventListener != null) {
          isTouch = false;
          onTouchEventListener.onTouchCancel();
        }
        break;
      default:
        break;
    }
    return true;
  }


  public void setEnable(boolean enable) {
    isEnable = enable;
  }


  public boolean isEnable() {
    return isEnable;
  }


  public void setOnTouchEventListener(OnTouchEventListener onTouchEventListener) {
    this.onTouchEventListener = onTouchEventListener;
  }


  public interface OnTouchEventListener {

    void onTouchDown();

    void onTouchCancel();

  }
}
