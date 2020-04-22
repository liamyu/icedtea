package com.liam.leetcode.p3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * com.liam.leetcode.p3.
 * <p/>
 * Created by Liam on 2019/3/6.
 */
class SolutionTest {

  @Test
  void lengthOfLongestSubstring() {
    Solution solution = new Solution();
    assertEquals(6, solution.lengthOfLongestSubstring("abcadefbcb"));
//    assertEquals(2, solution.lengthOfLongestSubstring("aba"));
//    assertEquals(4, solution.lengthOfLongestSubstring("abcaeb"));
//    assertEquals(2, solution.lengthOfLongestSubstring("abba"));
  }
}