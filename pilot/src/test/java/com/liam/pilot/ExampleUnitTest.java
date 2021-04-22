package com.liam.pilot;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  @Test
  public void addition_isCorrect() {
    assertEquals(4, 2 + 2);
  }

  @Test
  public void long_left_shift_isCorrect() {
    for (int i = 0; i < 65; i++) {
      System.out.println("<< " + i + ": " + (1L << i));
    }
  }
}