package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.containsString;

import io.qameta.allure.kotlin.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class MissionScreen {

    @Step("Проверить, что заголовок «Цитаты» отображается")
    public void assertTitleVisible() {
        EspressoUtils.waitForView(withId(R.id.our_mission_title_text_view));
        onView(withId(R.id.our_mission_title_text_view))
                .check(matches(isDisplayed()));
    }
}