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
import ru.edu.qamid.ui.screens.AuthScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.AllureStepHelper;

@LargeTest
@Epic("Авторизация")
@Feature("Позитивный сценарий")
@Owner("Мещанинов Сергей")
public class PositiveAuthTest extends BaseUiTest {

    private AuthScreen authScreen;
    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        if (isOnAuthScreen()) {
            authScreen = new AuthScreen();
        } else {
            // Уже залогинены — выходим, чтобы попасть на экран авторизации
            new NewsScreen().logout();
            authScreen = new AuthScreen();
        }
    }

    @Test
    @DisplayName("Авторизация с валидными данными и переход в главное меню")
    @Description("Успешная авторизация пользователя с корректными учётными данными, проверка перехода в главное меню приложения")
    @Severity(SeverityLevel.CRITICAL)
    public void AuthOK() {
        AllureStepHelper.step("Ввод валидных данных и выполнение входа", () -> {
            newsScreen = authScreen
                    .fillLogin("login2")
                    .fillPassword("password2")
                    .clickLoginSuccess();
        });

        AllureStepHelper.step("Проверка успешного перехода в главное меню", () -> {
            newsScreen.assertMainScreenDisplayed();
        });
    }
}
