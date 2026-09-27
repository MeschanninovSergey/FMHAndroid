package ru.edu.qamid.ui;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsScreen;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class PositiveAuthTest extends BaseUiTest {

   private NewsScreen newsScreen;

    @After
    public void tearDown() {
        try {
            if (newsScreen != null) {
                newsScreen.logout();
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    @Description("Успешная авторизация")
    @Severity(SeverityLevel.CRITICAL)
    public void AuthOK() {
        newsScreen = openAuthScreen()
                .fillLogin("login2")
                .fillPassword("password2")
                .clickLoginSuccess();
    }
}