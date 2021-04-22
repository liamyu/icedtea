package com.liam.pilot.usecase.userflag


/**
 * 用户标识UseCase，非线程安全，多实例时.
 */
class UserFlagUseCase {

  /**
   * 用户二进制标识，用于一次性的行为保存的本地标志位.
   */
  private var userFlag: Long = -1

  /**
   * 查询用户Flag.
   */
  fun queryFlag(): Long {
    if (userFlag < 0) {
//      userFlag = SharedPref.get().getLong(queryKey())
    }
    return userFlag
  }

  /**
   * 查询是否包含Flag.
   */
  fun queryHasFlag(flag: Long) = queryFlag() and flag > 0

  /**
   * 添加用户Flag.
   */
  fun addFlag(flag: Long) {
//    SharedPref.get().putLong(queryKey(), queryFlag() or flag)
  }

  /**
   * 移除用户Flag.
   */
  fun removeFlag(flag: Long) {
//    SharedPref.get().putLong(queryKey(), queryFlag() and flag.inv())
  }

  fun queryKey() = "user_flag"
}