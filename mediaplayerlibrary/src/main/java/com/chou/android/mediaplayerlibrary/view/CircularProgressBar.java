package com.chou.android.mediaplayerlibrary.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.chou.android.mediaplayerlibrary.R;

import static com.chou.android.mediaplayerlibrary.controllers.AtVideoPlayerController.VIDEO_CUT_MAX;

/**
 * Created by wxmylife on 2018/3/27.
 */

public class CircularProgressBar extends View {

  // Properties
  private float progress = 0;
  private float strokeWidth = getResources().getDimension(R.dimen.default_stroke_width);
  private float lineHight = getResources().getDimension(R.dimen.default_stroke_line_width);
  private float backgroundStrokeWidth = getResources().getDimension(R.dimen.default_background_stroke_width);
  private int color = Color.BLACK;
  private int backgroundColor = Color.GRAY;

  // Object used to draw
  private int startAngle = 0;
  private RectF rectF;
  private Paint backgroundPaint;
  private Paint foregroundPaint;
  private Paint linePaint;
  private Paint textPaint;
  private Paint textBottomPaint;
  private float highStroke;
  private RectF topTextRectF;
  private RectF bottomTextRectF;


  //region Constructor & Init Method
  public CircularProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }


  private void init(Context context, AttributeSet attrs) {
    rectF = new RectF();
    topTextRectF = new RectF();
    bottomTextRectF = new RectF();
    TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0);
    //Reading values from the XML layout
    try {
      // Value
      progress = typedArray.getFloat(R.styleable.CircularProgressBar_cpb_progress, progress);
      // StrokeWidth
      strokeWidth = typedArray.getDimension(R.styleable.CircularProgressBar_cpb_progressbar_width, strokeWidth);
      backgroundStrokeWidth = typedArray.getDimension(R.styleable.CircularProgressBar_cpb_background_progressbar_width, backgroundStrokeWidth);
      // Color
      color = typedArray.getInt(R.styleable.CircularProgressBar_cpb_progressbar_color, color);
      backgroundColor = typedArray.getInt(R.styleable.CircularProgressBar_cpb_background_progressbar_color, backgroundColor);
    } finally {
      typedArray.recycle();
    }

    // Init Background
    backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    backgroundPaint.setColor(backgroundColor);
    backgroundPaint.setStyle(Paint.Style.STROKE);
    backgroundPaint.setStrokeWidth(backgroundStrokeWidth);

    // Init Foreground
    foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    foregroundPaint.setColor(color);
    foregroundPaint.setStyle(Paint.Style.STROKE);
    foregroundPaint.setStrokeWidth(strokeWidth);

    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    linePaint.setColor(color);
    linePaint.setStyle(Paint.Style.STROKE);
    linePaint.setStrokeWidth(lineHight);

    textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setDither(true);
    textPaint.setStyle(Paint.Style.FILL);
    textPaint.setStrokeWidth(8);
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setColor(color);
    // setPathEffect(new CornerPathEffect(0.5F));
    textPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        30, getResources().getDisplayMetrics()));

    textBottomPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    textBottomPaint.setDither(true);
    textBottomPaint.setStyle(Paint.Style.FILL);
    textBottomPaint.setStrokeWidth(8);
    textBottomPaint.setTextAlign(Paint.Align.CENTER);
    textBottomPaint.setColor(Color.parseColor("#b3ffffff"));
    // setPathEffect(new CornerPathEffect(0.5F));
    textBottomPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        12, getResources().getDisplayMetrics()));

  }
  //endregion


  //region Draw Method
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawOval(rectF, backgroundPaint);
    // Log.e("wxmylife", "setProgress: ---------" + progress);
    float angle = 360 * progress / VIDEO_CUT_MAX;
    canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    canvas.drawLine(rectF.left + highStroke * 2, rectF.height() / 2 + highStroke / 2, rectF.right - highStroke * 2, rectF.height() / 2 + highStroke / 2, linePaint);

    String progressText = String.valueOf(((int) Math.ceil(VIDEO_CUT_MAX-progress))/1000);
    String textNum = "剩余" + ((int) Math.ceil(progress/1000) )+ "秒";

    // Log.e("wxmylife", "setProgress: ----top-----" +progressText);
    // Log.e("wxmylife", "setProgress: ----bottom-----剩余" + Math.ceil(progress/1000) + "秒");

    // textPaint.set
    // // Get progress text bounds
    // textPaint.setTextSize(mProgressModelSize * 0.35f);
    // textPaint.getTextBounds(
    //     percentProgress, 0, percentProgress.length(), model.mTextBounds);
    //
    Paint.FontMetrics fontMetricsTop = textPaint.getFontMetrics();
    float distanceTop = (fontMetricsTop.bottom - fontMetricsTop.top) / 2 - fontMetricsTop.bottom;
    float baselineTop = topTextRectF.centerY() + distanceTop;
    canvas.drawText(progressText, topTextRectF.centerX(), baselineTop, textPaint);

    Paint.FontMetrics fontMetricsBottom = textBottomPaint.getFontMetrics();
    float distanceBottom = (fontMetricsBottom.bottom - fontMetricsBottom.top) / 2 - fontMetricsBottom.bottom;
    float baselineBottom = bottomTextRectF.centerY() + distanceBottom;
    canvas.drawText(textNum, bottomTextRectF.centerX(), baselineBottom, textBottomPaint);

  }
  //endregion


  //region Mesure Method
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    final int min = Math.min(width, height);
    setMeasuredDimension(min, min);
    highStroke = (strokeWidth > backgroundStrokeWidth) ? strokeWidth : backgroundStrokeWidth;
    rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    topTextRectF.set(0 + highStroke, 0 + highStroke, min - highStroke, min / 2 - highStroke / 2);
    bottomTextRectF.set(0 + highStroke, min / 2 - highStroke, min - highStroke, min - highStroke);
  }
  //endregion


  //region Method Get/Set
  public float getProgress() {
    return progress;
  }


  public void setProgress(float progress) {
    this.progress = (progress <= VIDEO_CUT_MAX) ? progress : VIDEO_CUT_MAX;
    invalidate();
  }


  public float getProgressBarWidth() {
    return strokeWidth;
  }


  public void setProgressBarWidth(float strokeWidth) {
    this.strokeWidth = strokeWidth;
    foregroundPaint.setStrokeWidth(strokeWidth);
    requestLayout();//Because it should recalculate its bounds
    invalidate();
  }


  public float getBackgroundProgressBarWidth() {
    return backgroundStrokeWidth;
  }


  public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
    this.backgroundStrokeWidth = backgroundStrokeWidth;
    backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
    requestLayout();//Because it should recalculate its bounds
    invalidate();
  }


  public int getColor() {
    return color;
  }


  public void setColor(int color) {
    this.color = color;
    foregroundPaint.setColor(color);
    invalidate();
    requestLayout();
  }


  public int getBackgroundColor() {
    return backgroundColor;
  }


  @Override
  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
    backgroundPaint.setColor(backgroundColor);
    invalidate();
    requestLayout();
  }
  //endregion

  //region Other Method


  /**
   * Set the progress with an animation.
   * Note that the {@link ObjectAnimator} Class automatically set the progress
   * so don't call the {@link CircularProgressBar#setProgress(float)} directly within this method.
   *
   * @param progress The progress it should animate to it.
   */
  public void setProgressWithAnimation(float progress) {
    setProgressWithAnimation(progress, 1500);
  }


  /**
   * Set the progress with an animation.
   * Note that the {@link ObjectAnimator} Class automatically set the progress
   * so don't call the {@link CircularProgressBar#setProgress(float)} directly within this method.
   *
   * @param progress The progress it should animate to it.
   * @param duration The length of the animation, in milliseconds.
   */
  public void setProgressWithAnimation(float progress, int duration) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
    objectAnimator.setDuration(duration);
    objectAnimator.setInterpolator(new DecelerateInterpolator());
    objectAnimator.start();
  }
  //endregion
}