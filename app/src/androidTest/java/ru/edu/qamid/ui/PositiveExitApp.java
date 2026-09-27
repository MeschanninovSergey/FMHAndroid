package ru.edu.qamid.ui;

import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsScreen;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class PositiveExitApp extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        newsScreen = openAuthScreen()
                .fillLogin("login2")
                .fillPassword("password2")
                .clickLoginSuccess();
    }

    @Test
    @Description("Успешный выход из приложения")
    @Severity(SeverityLevel.CRITICAL)
    public void appExitTest() {

        newsScreen.logout();
    }
}