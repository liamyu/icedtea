package com.liam.viewflipper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    NewsBoardLayout newsBoard = findViewById(R.id.viewFlipper);
    newsBoard.addNews("3rd child");
    newsBoard.addNews("4th child");
  }
}
