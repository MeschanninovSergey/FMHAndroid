package ru.edu.qamid.ui;

import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.edu.qamid.ui.base.BaseUiTest;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NegativeAuthTest extends BaseUiTest {

    @Test
    @Description("Ввод неверного пароля")
    @Severity(SeverityLevel.CRITICAL)
    public void AuthWithWrongPassword() {
        openAuthScreen()
                .fillLogin("login2")
                .fillPassword("wrong_password")
                .clickLoginExpectingError()
                .assertAuthScreenDisplayed();
    }

    @Test
    @Description("Ввод неверного логина")
    @Severity(SeverityLevel.CRITICAL)
    public void AuthWithWrongLogin() {
        openAuthScreen()
                .fillLogin("wrong_login")
                .fillPassword("password2")
                .clickLoginExpectingError()
                .assertAuthScreenDisplayed();
    }

    @Test
    @Description("Авторизация с пустым полем пароля")
    @Severity(SeverityLevel.MINOR)
    public void AuthWithEmptyPassword() {
        openAuthScreen()
                .fillLogin("login2")
                .clickLoginExpectingError()
                .assertAuthScreenDisplayed();
    }

    @Test
    @Description("Авторизация с пустым полем логина")
    @Severity(SeverityLevel.MINOR)
    public void AuthWithEmptyLogin() {
        openAuthScreen()
                .fillPassword("password2")
                .clickLoginExpectingError()
                .assertAuthScreenDisplayed();
    }

    @Test
    @Description("Авторизация с пустыми полями логина и пароля")
    @Severity(SeverityLevel.MINOR)
    public void AuthWithBothFieldsEmpty() {
        openAuthScreen()
                .clickLoginExpectingError()
                .assertAuthScreenDisplayed();
    }

}