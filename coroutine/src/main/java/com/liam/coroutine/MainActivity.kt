package com.liam.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    exceptionHandling()
  }

  private fun exceptionHandling() {
    runBlocking {
      val job = GlobalScope.launch { // root coroutine with launch
        Log.d(TAG, "Throwing exception from launch")
        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
      }
      job.join()
      Log.d(TAG, "Joined failed job")
      val deferred: Deferred<String> = GlobalScope.async { // root coroutine with async
        Log.d(TAG, "Throwing exception from async ")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
      }
      try {
        deferred.await()
        Log.d(TAG, "Unreached")
      } catch (e: ArithmeticException) {
        Log.d(TAG, "Caught ArithmeticException ")
      }
    }
  }

  companion object {
    val TAG: String = MainActivity::class.java.simpleName
  }
}