package ru.edu.qamid.ui.base;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;

import io.qameta.allure.kotlin.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.AppActivity;
import ru.edu.qamid.ui.screens.AuthScreen;
import ru.edu.qamid.ui.screens.MainScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.EspressoUtils;

public abstract class BaseUiTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);
    @Step("Открыть экран авторизации")
    protected AuthScreen openAuthScreen() {
        return new MainScreen().clickEnter();
    }

    protected boolean isOnAuthScreen() {
        try {
            EspressoUtils.waitForView(withId(R.id.login_edit_text), 5000);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    @Step("Войти напрямую с экрана авторизации")
    protected NewsScreen loginDirectly() {
        return new AuthScreen()
                .fillLogin("login2")          // подставь свои тестовые данные
                .fillPassword("password2")
                .clickLoginSuccess();
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
