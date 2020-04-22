package com.liam.coordinatorlayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupToolbar();
    setupTabs();
    setupRecyclerView();
  }

  private void setupToolbar() {
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    ActionBar toolbar = getSupportActionBar();
    //    toolbar.setDisplayShowTitleEnabled(true);
    toolbar.setTitle("这是标题");
  }

  private void setupTabs() {
    //    TabLayout tabs = findViewById(R.id.tabs);
    //    Tab tab = new Tab();
    //    tab.setText("tab1");
    //    tab = new Tab();
    //    tab.setText("tab2");
    //    tabs.addTab(tab);
  }

  private void setupRecyclerView() {
    final List<String> list = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      list.add("content " + i);
    }
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    recyclerView.setAdapter(new Adapter() {
      @NonNull
      @Override
      public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        return new HeaderViewHolder(textView);
      }

      @Override
      public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(list.get(position));
      }

      @Override
      public int getItemCount() {
        return list.size();
      }
    });
  }

  public class HeaderViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public HeaderViewHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView;
    }
  }
}
