package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import io.qameta.allure.kotlin.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class AuthScreen {

    @Step("Ввести логин: {login}")
    public AuthScreen fillLogin(String login) {
        EspressoUtils.waitForView(allOf(withId(R.id.login_edit_text), isDisplayed()));
        onView(allOf(withId(R.id.login_edit_text), isDisplayed()))
                .perform(replaceText(login), closeSoftKeyboard());
        return this;
    }

    @Step("Ввести пароль: {password}")
    public AuthScreen fillPassword(String password) {
        EspressoUtils.waitForView(allOf(withId(R.id.password_edit_text), isDisplayed()));
        onView(allOf(withId(R.id.password_edit_text), isDisplayed()))
                .perform(replaceText(password), closeSoftKeyboard());
        return this;
    }

    @Step("Нажать кнопку «Войти» (успешный вход)")
    public NewsScreen clickLoginSuccess() {
        onView(allOf(withId(R.id.enter_button), withText("Войти"), isDisplayed()))
                .perform(click());
        return new NewsScreen();
    }

    @Step("Нажать кнопку «Войти» (ожидаем ошибку)")
    public AuthScreen clickLoginExpectingError() {
        EspressoUtils.waitForView(allOf(withId(R.id.enter_button), isDisplayed()));
        onView(allOf(withId(R.id.enter_button), isDisplayed()))
                .perform(click());
        return this;
    }

    @Step("Проверить, что экран авторизации отображается")
    public void assertAuthScreenDisplayed() {
        EspressoUtils.waitForView(withId(R.id.enter_button), 5000);
        onView(withId(R.id.enter_button))
                .check(matches(isDisplayed()));
    }
}
