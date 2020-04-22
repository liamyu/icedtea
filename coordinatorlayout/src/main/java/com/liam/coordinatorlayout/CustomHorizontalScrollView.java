package com.liam.coordinatorlayout;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import androidx.annotation.RequiresApi;

/**
 * 自定义滚动条的{@link HorizontalScrollView}.
 * <p>
 * Created by Liam on 2019/6/10.
 */
public class CustomHorizontalScrollView  extends HorizontalScrollView {

  public CustomHorizontalScrollView(Context context) {
    super(context);
  }

  public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = VERSION_CODES.LOLLIPOP)
  public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected int computeHorizontalScrollExtent() {
    return super.computeHorizontalScrollExtent() / 4;
  }

  @Override
  protected int computeHorizontalScrollOffset() {
    return super.computeHorizontalScrollOffset() / 4;
  }

  @Override
  protected int computeHorizontalScrollRange() {
    return super.computeHorizontalScrollRange();
  }
}
