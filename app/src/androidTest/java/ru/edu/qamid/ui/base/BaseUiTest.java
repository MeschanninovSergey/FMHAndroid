package ru.edu.qamid.ui.base;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;

import io.qameta.allure.Step;
import ru.edu.qamid.ui.AppActivity;
import ru.edu.qamid.ui.screens.AuthScreen;
import ru.edu.qamid.ui.screens.MainScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;

public abstract class BaseUiTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Step("Открыть экран авторизации")
    protected AuthScreen openAuthScreen() {
        return new MainScreen().clickEnter();
    }

    @Step("Войти как пользователь по умолчанию")
    protected NewsScreen loginAsDefaultUser() {
        return openAuthScreen()
                .fillLogin("login2")
                .fillPassword("password2")
                .clickLoginSuccess();
    }

    @Step("Создать новость и вернуться к списку")
    protected NewsListScreen createNewsAndReturnToList(NewsScreen newsScreen, String category,
                                                       String title, String description) {
        return newsScreen
                .openNewsManagement()
                .openEditMode()
                .createNews(category, title, description);
    }
}