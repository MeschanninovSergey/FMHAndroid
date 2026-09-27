package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import io.qameta.allure.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class MainScreen {

    @Step("Нажать кнопку «Войти» на главном экране")
    public AuthScreen clickEnter() {
        EspressoUtils.waitForView(withId(R.id.enter_button));
        onView(withId(R.id.enter_button)).perform(click());
        return new AuthScreen();
    }
}