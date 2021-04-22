package com.liam.pilot.usecase.userflag

/**
 * 用户Flag（Long类型最多支持左移63位，即支持最多64种Flag，1L<<64==1<<0，1L<<65==1L<<1，循环往复）.
 */
enum class UserFlag(val flag: Long) {
  PRIVACY_POLICY_AGREED(1 shl 0)
}