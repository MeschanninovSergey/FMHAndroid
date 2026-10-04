package ru.edu.qamid.ui;

import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
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
@RunWith(AllureAndroidJUnit4.class)
@Epic("Авторизация")
@Feature("Негативные сценарии")
@Owner("Мещанинов Сергей")
public class NegativeAuthTest extends BaseUiTest {

    private AuthScreen authScreen;

    @Before
    public void setUp() {
        if (!isOnAuthScreen()) {
            new NewsScreen().logout();
        }
        authScreen = new AuthScreen();
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным паролем")
    @Description("Авторизация с неверным паролем, проверяет ошибку авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативный сценарий авторизации")
    public void AuthWithWrongPassword() {
        AllureStepHelper.step("Подготовка к авторизации с неверным паролем", () -> {
            authScreen.fillLogin("login2").fillPassword("wrong_password");
        });

        AllureStepHelper.step("Попытка входа и проверка ошибки", () -> {
            authScreen.clickLoginExpectingError();
        });

        AllureStepHelper.step("Проверка, что экран авторизации остался открытым", () -> {
            authScreen.assertAuthScreenDisplayed();
        });
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным логином")
    @Description("Авторизация с неверным логином, проверяет ошибку авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативный сценарий авторизации")
    public void AuthWithWrongLogin() {
        AllureStepHelper.step("Подготовка к авторизации с неверным логином", () -> {
            authScreen.fillLogin("wrong_login").fillPassword("password2");
        });

        AllureStepHelper.step("Попытка входа и проверка ошибки", () -> {
            authScreen.clickLoginExpectingError();
        });

        AllureStepHelper.step("Проверка, что экран авторизации остался открытым", () -> {
            authScreen.assertAuthScreenDisplayed();
        });
    }

    @Test
    @DisplayName("Ошибка авторизации с пустым паролем")
    @Description("Авторизация с пустым полем пароля, проверяет ошибку авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативный сценарий авторизации")
    public void AuthWithEmptyPassword() {
        AllureStepHelper.step("Подготовка к авторизации с пустым паролем", () -> {
            authScreen.fillLogin("login2");
        });

        AllureStepHelper.step("Попытка входа без пароля и проверка ошибки", () -> {
            authScreen.clickLoginExpectingError();
        });

        AllureStepHelper.step("Проверка, что экран авторизации остался открытым", () -> {
            authScreen.assertAuthScreenDisplayed();
        });
    }

    @Test
    @DisplayName("Ошибка авторизации с пустым логином")
    @Description("Авторизация с пустым полем логина, проверяет ошибку авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативный сценарий авторизации")
    public void AuthWithEmptyLogin() {
        AllureStepHelper.step("Подготовка к авторизации с пустым логином", () -> {
            authScreen.fillPassword("password2");
        });

        AllureStepHelper.step("Попытка входа без логина и проверка ошибки", () -> {
            authScreen.clickLoginExpectingError();
        });

        AllureStepHelper.step("Проверка, что экран авторизации остался открытым", () -> {
            authScreen.assertAuthScreenDisplayed();
        });
    }

    @Test
    @DisplayName("Ошибка авторизации с пустыми логином и паролем")
    @Description("Авторизация с пустыми полями логина и пароля, проверяет ошибку авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативный сценарий авторизации")
    public void AuthWithBothFieldsEmpty() {
        AllureStepHelper.step("Попытка входа с пустыми данными и проверка ошибки", () -> {
            authScreen.clickLoginExpectingError();
        });

        AllureStepHelper.step("Проверка, что экран авторизации остался открытым", () -> {
            authScreen.assertAuthScreenDisplayed();
        });
    }
}
