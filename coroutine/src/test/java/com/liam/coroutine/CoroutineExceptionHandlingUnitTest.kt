package com.liam.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoroutineExceptionHandlingUnitTest {

  @ExperimentalCoroutinesApi
  @get:Rule
  val coroutineRule = MainCoroutineRule()

  @Test
  fun exceptionHandling_isCorrect() = runBlocking {
    runBlocking {
      val job = GlobalScope.launch { // root coroutine with launch
        println("Throwing exception from launch")
        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
      }
      job.join()
      println("Joined failed job")
      val deferred: Deferred<Unit> = GlobalScope.async { // root coroutine with async
        println("Throwing exception from async")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
      }
      try {
        deferred.await()
        println("Unreached")
      } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
      }
    }
  }
}