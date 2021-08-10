package com.liam.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun BottomSheetSample(
  bottomSheetScaffoldState: BottomSheetScaffoldState,
  content: @Composable () -> Unit
) {

  val scope = rememberCoroutineScope()
  BottomSheetScaffold(
    scaffoldState = bottomSheetScaffoldState,
    sheetContent = {
      Box(
        Modifier
          .fillMaxWidth()
          .height(300.dp)
          .background(Color(0xAA3fa7cc))
      ) {
        Text(
          text = "Hello from bottom sheet",
          modifier = Modifier
            .align(Alignment.Center)
        )
      }
    },
    sheetPeekHeight = 0.dp,
    sheetBackgroundColor = Color.DarkGray
  ) {
    content()
  }
}


@ExperimentalMaterialApi
@Composable
fun BottomSheet() {
  val bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
  )
  val rememberCoroutineScope = rememberCoroutineScope()
  BottomSheetSample(bottomSheetScaffoldState = bottomSheetScaffoldState) {
    Button(onClick = {
      rememberCoroutineScope.launch {
        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
          bottomSheetScaffoldState.bottomSheetState.expand()
        } else {
          bottomSheetScaffoldState.bottomSheetState.collapse()
        }
      }
    }) {
      Text(text = "打开")
    }
  }
}