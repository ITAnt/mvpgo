package com.miekir.mvp.common.preference

/**
 * SharedPreference配置清单
 */
private object Preference {
    var token by SP("token", "")
    var userId by SP("user_id", "")
    var reportStatus by SP("report_status", "")
    var isFirstLaunch by SP("is_first_launch", true)
    var hasCashVoice by SP("has_cash_voice", true)
    var showCashTip by SP("show_cash_tip", true)
    var showPayTip by SP("show_pay_tip", true)
    var stationId by SP("station_id", "")
    var isRegistered by SP("is_register", false)

    var terminalId by SP("terminal_id", "")//终端编号
    var isOffSet by SP("is_offset", true)//是否打印偏移

    //var user by SP("user", User())
}