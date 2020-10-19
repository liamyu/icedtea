package com.liam.umeng;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    setupUmeng();
  }

  /**
   * 初始化友盟统计.
   */
  private void setupUmeng() {
    UMConfigure.init(this, "5c0e1bddb465f5d4d00000f5", null,
        UMConfigure.DEVICE_TYPE_PHONE, "");

    // 选用AUTO页面采集模式
    MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

//    PlatformConfig.setWeixin(BuildConfig.WX_APPID, BuildConfig.WX_APP_SECRET);
//    PlatformConfig.setQQZone(TencentKeys.APP_ID, TencentKeys.APP_SECRET);
//    PlatformConfig.setSinaWeibo(SinaKeys.APP_KEY, SinaKeys.APP_SECRET, "http://sns.whalecloud.com");
//    PlatformConfig.setAlipay("2015111700822536");
  }
}
