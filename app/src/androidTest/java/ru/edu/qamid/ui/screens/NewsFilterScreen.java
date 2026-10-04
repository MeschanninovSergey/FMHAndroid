package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.widget.DatePicker;

import io.qameta.allure.kotlin.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.actions.DatePickerActions;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class NewsFilterScreen {

    @Step("Установить начальную дату фильтра")
    public NewsFilterScreen setStartDate(int year, int month, int day) {
        EspressoUtils.waitForView(withId(R.id.filter_news_date_start_edit_text));
        onView(withId(R.id.filter_news_date_start_edit_text)).perform(click());

        EspressoUtils.waitForView(isAssignableFrom(DatePicker.class));
        onView(isAssignableFrom(DatePicker.class))
                .perform(DatePickerActions.setDate(year, month, day));

        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(click());

        return this;
    }

    @Step("Установить конечную дату фильтра")
    public NewsFilterScreen setEndDate(int year, int month, int day) {
        EspressoUtils.waitForView(withId(R.id.filter_news_date_end_edit_text));
        onView(withId(R.id.filter_news_date_end_edit_text)).perform(click());

        EspressoUtils.waitForView(isAssignableFrom(DatePicker.class));
        onView(isAssignableFrom(DatePicker.class))
                .perform(DatePickerActions.setDate(year, month, day));

        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(click());

        return this;
    }

    @Step("Нажать кнопку «Фильтровать»")
    public NewsListScreen clickFilterButton() {
        EspressoUtils.waitForView(withId(R.id.filter_news_apply_button));
        onView(withId(R.id.filter_news_apply_button)).perform(click());
        return new NewsListScreen();
    }
}