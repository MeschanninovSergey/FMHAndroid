package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import io.qameta.allure.kotlin.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class NewsScreen extends BaseScreen {

    @Step("Проверить, что открылась страница Все новости")
    public NewsScreen assertAllNewsDisplayed() {
        EspressoUtils.waitForView(allOf(withId(R.id.all_news_text_view),
                withText(containsString("Все новости"))));
        onView(allOf(withId(R.id.all_news_text_view),
                withText(containsString("Все новости"))))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Выход из приложения")
    public AuthScreen logout() {
        try {
            EspressoUtils.waitForView(withId(R.id.authorization_image_button), 5000);
            onView(allOf(withId(R.id.authorization_image_button), isDisplayed()))
                    .perform(click());
        } catch (Throwable e) {
            return new AuthScreen();
        }

        try {
            EspressoUtils.waitForView(withId(android.R.id.title), 5000);
            onView(withId(android.R.id.title))
                    .perform(click());
        } catch (Throwable ignored) {
        }

        return new AuthScreen();
    }

    @Step("Проверить, что экран главная отображается")
    public void assertMainScreenDisplayed() {
        EspressoUtils.waitForView(withId(R.id.trademark_image_view), 5000);
        onView(withId(R.id.trademark_image_view))
                .check(matches(isDisplayed()));
    }

}
