package ru.edu.qamid.ui;

import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Owner;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.AllureStepHelper;

@LargeTest
@Epic("Выход из аккаунта")
@Feature("Выход из приложения")
@Owner("Мещанинов Сергей")
public class PositiveExitApp extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        if (isOnAuthScreen()) {
            newsScreen = loginAsDefaultUser();
        } else {
            newsScreen = new NewsScreen();
        }
    }

    @Test
    @DisplayName("Успешный выход из приложения")
    @Description("Проверка успешного выхода из аккаунта: выполняется logout, ожидается возврат на экран авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Успешный выход")
    public void appExitTest() {
        AllureStepHelper.step("Выполнение выхода из аккаунта", () -> {
            newsScreen.logout();
        });

        AllureStepHelper.step("Проверка перехода на экран авторизации", () -> {
            openAuthScreen().assertAuthScreenDisplayed();
        });
    }
}