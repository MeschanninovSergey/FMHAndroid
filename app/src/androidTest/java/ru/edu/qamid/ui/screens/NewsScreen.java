package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import io.qameta.allure.Step;
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
            EspressoUtils.waitForView(withId(R.id.authorization_image_button));
            onView(allOf(withId(R.id.authorization_image_button), isDisplayed()))
                    .perform(click());
        } catch (Throwable e) {
            return new AuthScreen();
        }

        try {
            EspressoUtils.waitForView(allOf(withId(android.R.id.title), withText("Выйти")));
            onView(allOf(withId(android.R.id.title), withText("Выйти")))
                    .perform(click());
        } catch (Throwable ignored) {
        }

        return new AuthScreen();
    }
}