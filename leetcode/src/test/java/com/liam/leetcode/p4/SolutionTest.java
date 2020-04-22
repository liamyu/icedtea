package com.liam.leetcode.p4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * com.liam.leetcode.p4.
 * <p/>
 * Created by Liam on 2019/3/20.
 */
class SolutionTest {

  @Test
  void longestPalindrome() {
    Solution solution = new Solution();
    assertEquals("aba", solution.longestPalindrome("aba"));
  }

}