package com.liam.aosp;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.liam.aosp.swiperefreshlayout.SwipeRefreshLayout;
import com.liam.aosp.swiperefreshlayout.SwipeRefreshLayout.OnRefreshListener;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.srl);
    swipeRefreshLayout.setProgressViewEndTarget(true,
        (int) (getResources().getDisplayMetrics().density * 64));
    swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            swipeRefreshLayout.setRefreshing(false);
          }
        }, 5 * 1000);
      }
    });
  }
}
