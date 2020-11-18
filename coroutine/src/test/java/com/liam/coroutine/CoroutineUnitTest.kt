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
class CoroutineUnitTest {

  @ExperimentalCoroutinesApi
  @get:Rule
  val coroutineRule = MainCoroutineRule()

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  /**
   * No runBlocking, vm exits before coroutine finishes.
   */
  @Test
  fun coroutine_isCorrect() {
    for (i in 1..5) {
      CoroutineScope(IO).launch {
        println("IO: ${Thread.currentThread()}")
      }
    }
  }

  /**
   * With runBlocking.
   */
  @Test
  fun runBlocking_isCorrect() = runBlocking {
    for (i in 1..5) {
      CoroutineScope(IO).launch {
        println("IO: ${Thread.currentThread()}")
      }
    }
    println("Main: ${Thread.currentThread()}")
  }

  @Test
  fun blockMain_isCorrect() = runBlocking {
    CoroutineScope(Main).launch {
      println("Main 1: ${Thread.currentThread()}")
      withContext(IO) {
        delay(5000)
        println("IO: ${Thread.currentThread()}")
      }
      println("Main 2: ${Thread.currentThread()}")
    }.join()
    println("Main: ${Thread.currentThread()}")
  }

  @Test
  fun blockMain_isCorrect2() = runBlocking {
    println("Main 1: ${Thread.currentThread()}")
    CoroutineScope(IO).launch {
      delay(5000)
      println("IO: ${Thread.currentThread()}")
      withContext(Main) {
        println("Main 2: ${Thread.currentThread()}")
      }
    }.join()
    println("Main: ${Thread.currentThread()}")
  }
}