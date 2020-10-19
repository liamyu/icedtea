package com.liam.umeng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  public static void start(Context context, int index) {
    Intent starter = new Intent(context, MainActivity.class);
    starter.putExtra("index", index);
    context.startActivity(starter);
  }

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button btn = findViewById(R.id.btn);
    final int index = getIntent().getIntExtra("index", 0);
    btn.setText("Hello World! " + index);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MainActivity.start(MainActivity.this, index + 1);
      }
    });
  }
}
