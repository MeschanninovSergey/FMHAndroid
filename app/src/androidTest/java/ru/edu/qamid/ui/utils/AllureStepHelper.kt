package ru.edu.qamid.ui.utils

import io.qameta.allure.kotlin.Allure

object AllureStepHelper {
@JvmStatic
fun step(title: String, action: Runnable) {
    Allure.step(title) {
        try {
            action.run()
        } catch (t: Throwable) {
            throw t
        }
    }
}
}