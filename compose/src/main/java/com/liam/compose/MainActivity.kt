package com.liam.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.liam.compose.ui.theme.IcedteaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  @ExperimentalMaterialApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      IcedteaTheme() {
        BottomSheet()
      }
    }
  }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  IcedteaTheme {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background) {
      Greeting("Android")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun Greeting(name: String = "Liam") {
  Text(text = "Hello $name!")
}