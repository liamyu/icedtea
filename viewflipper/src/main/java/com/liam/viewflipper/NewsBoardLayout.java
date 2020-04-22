package com.liam.viewflipper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;
import java.util.List;

/**
 * 新闻公告牌.
 * <p>
 * Created by Liam on 2019/6/6.
 */
public class NewsBoardLayout extends ViewFlipper {

  public NewsBoardLayout(Context context) {
    super(context);
    setupAnim(context);
  }

  public NewsBoardLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    setupAnim(context);
  }

  private void setupAnim(Context context) {
    setInAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_bottom_in));
    setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_top_out));
  }

  /**
   * 添加单条公告.
   *
   * @param news 新闻公告
   */
  public void addNews(String news) {
    TextView child = new TextView(getContext());
    child.setText(news);
    child.setTextColor(getContext().getResources().getColor(android.R.color.white));
    LayoutParams layoutParams = new LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    addView(child, layoutParams);
  }


  /**
   * 添加多条公告.
   *
   * @param newsList 新闻公告列表
   */
  public void addNews(List<String> newsList) {
    for (String news : newsList) {
      addNews(news);
    }
  }
}
