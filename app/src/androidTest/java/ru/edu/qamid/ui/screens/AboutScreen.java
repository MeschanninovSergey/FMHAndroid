package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import io.qameta.allure.Step;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class AboutScreen {

    @Step("Проверить, что экран «О приложении» отображается")
    public AboutScreen assertAboutScreenDisplayed() {
        EspressoUtils.waitForView(withText(containsString("О приложении")));
        onView(withText(containsString("О приложении")))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Нажать «Назад» для возврата на главный экран")
    public NewsScreen pressBackToMain() {
        pressBack();
        return new NewsScreen();
    }
}