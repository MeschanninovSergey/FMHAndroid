package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import static ru.edu.qamid.ui.actions.TimePickerActions.setTime;
import static ru.edu.qamid.ui.utils.EspressoUtils.clickOnChildViewWithId;

import android.widget.DatePicker;
import android.widget.TimePicker;

import io.qameta.allure.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.actions.DatePickerActions;
import ru.edu.qamid.ui.actions.TimePickerActions;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class NewsEditScreen extends NewsListScreen {

    @Step("Проверить, что «Панель управления» отображается")
    public NewsEditScreen assertControlPanelVisible() {
        EspressoUtils.waitForView(withText("Панель \n управления"));
        onView(withText("Панель \n управления"))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Проверить, что остались на экране «Создание»")
    public NewsEditScreen assertStillOnCreateScreen() {
        EspressoUtils.waitForView(allOf(withId(R.id.custom_app_bar_title_text_view),
                withText("Создание")));
        onView(allOf(withId(R.id.custom_app_bar_title_text_view), withText("Создание")))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Нажать кнопку добавления новости")
    public NewsEditScreen clickAddNews() {
        closeSoftKeyboard();
        EspressoUtils.waitForView(allOf(withId(R.id.add_news_image_view),
                withContentDescription("Кнопка добавления новости")));
        try {
            onView(allOf(withId(R.id.add_news_image_view),
                    withContentDescription("Кнопка добавления новости")))
                    .perform(scrollTo(), click());
        } catch (Exception e) {
            onView(allOf(withId(R.id.add_news_image_view),
                    withContentDescription("Кнопка добавления новости")))
                    .perform(click());
        }
        return this;
    }

    @Step("Выбрать категорию")
    public NewsEditScreen selectCategory(String categoryText) {
        EspressoUtils.waitForView(withId(R.id.news_category_auto_complete));
        onView(withId(R.id.news_category_auto_complete)).perform(click());
        onData(containsString(categoryText))
                .inRoot(isPlatformPopup())
                .perform(click());
        return this;
    }

    @Step("Ввести текст категории")
    public NewsEditScreen enterCategoryText(String category) {
        EspressoUtils.waitForView(withId(R.id.news_category_auto_complete));
        onView(withId(R.id.news_category_auto_complete))
                .perform(replaceText(category), closeSoftKeyboard());
        return this;
    }

    @Step("Нажать «Сохранить» (ожидаем ошибку)")
    public NewsEditScreen saveNewsExpectingFailure() {
        EspressoUtils.waitForView(withId(R.id.news_save_button));
        onView(allOf(withId(R.id.news_save_button), isDisplayed()))
                .perform(click());
        return this;
    }

    @Step("Ввести заголовок")
    public NewsEditScreen enterTitle(String title) {
        EspressoUtils.waitForView(withId(R.id.news_title_edit_text));
        onView(withId(R.id.news_title_edit_text))
                .perform(replaceText(title), closeSoftKeyboard());
        return this;
    }

    @Step("Подтвердить дату публикации")
    public NewsEditScreen confirmDate() {
        EspressoUtils.waitForView(withId(R.id.news_publish_date_edit_text));
        onView(withId(R.id.news_publish_date_edit_text)).perform(click());
        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(click());
        return this;
    }

    @Step("Подтвердить время публикации")
    public NewsEditScreen confirmTime() {
        EspressoUtils.waitForView(withId(R.id.news_publish_time_edit_text));
        onView(withId(R.id.news_publish_time_edit_text)).perform(click());
        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(click());
        return this;
    }

    @Step("Подтвердить дату и время публикации")
    public NewsEditScreen confirmDateAndTime() {
        return confirmDate().confirmTime();
    }

    @Step("Установить дату")
    public NewsEditScreen setAndConfirmDate(int year, int month, int day) {
        EspressoUtils.waitForView(withId(R.id.news_publish_date_edit_text)); // Замени на свой ID
        onView(withId(R.id.news_publish_date_edit_text)).perform(click());
        EspressoUtils.waitForView(isAssignableFrom(DatePicker.class));
        onView(isAssignableFrom(DatePicker.class))
                .perform(DatePickerActions.setDate(year, month, day));
        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(click());

        return this;
    }

    @Step("Установить время")
    public NewsEditScreen setAndConfirmTime(int hour, int minute) {
        EspressoUtils.waitForView(withId(R.id.news_publish_time_edit_text)); // Замени на свой ID
        onView(withId(R.id.news_publish_time_edit_text)).perform(click());
        EspressoUtils.waitForView(isAssignableFrom(TimePicker.class));
        onView(isAssignableFrom(TimePicker.class))
                .perform(TimePickerActions.setTime(hour, minute));
        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(click());

        return this;
    }

    @Step("Ввести описание")
    public NewsEditScreen enterDescription(String text) {
        EspressoUtils.waitForView(withId(R.id.news_description_edit_text));
        onView(withId(R.id.news_description_edit_text))
                .perform(replaceText(text), closeSoftKeyboard());
        return this;
    }

    @Step("Нажать «Сохранить»")
    public NewsListScreen saveNews() {
        EspressoUtils.waitForView(withId(R.id.news_save_button));
        onView(allOf(withId(R.id.news_save_button), isDisplayed()))
                .perform(click());
        return new NewsListScreen();
    }

    @Step("Нажать «Сохранить» (со скроллом)")
    public NewsListScreen saveNewsWithScroll() {
        EspressoUtils.waitForView(withId(R.id.news_save_button));
        onView(allOf(withId(R.id.news_save_button), isDisplayed()))
                .perform(scrollTo(), click());
        return new NewsListScreen();
    }

    @Step("Переключить статус")
    public NewsEditScreen toggleActiveSwitch(String currentText) {
        EspressoUtils.waitForView(allOf(withId(R.id.news_active_switch), withText(currentText)));
        onView(allOf(withId(R.id.news_active_switch), withText(currentText)))
                .perform(scrollTo(), click());
        return this;
    }
    @Step("Проверить категорию")
    public NewsEditScreen assertCategoryEquals(String expected) {
        EspressoUtils.waitForView(allOf(withId(R.id.news_category_auto_complete),
                withText(expected)));
        onView(allOf(withId(R.id.news_category_auto_complete), withText(expected)))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Нажать «Отмена»")
    public NewsListScreen clickCancelButton() {
        EspressoUtils.waitForView(allOf(withId(R.id.news_cancel_button), withText("Отмена")));
        onView(allOf(withId(R.id.news_cancel_button), withText("Отмена")))
                .perform(scrollTo(), click());
        return new NewsListScreen();
    }

    @Step("Создать новость: категория, заголовок, описание")
    public NewsListScreen createNews(String category, String title, String description) {
        return clickAddNews()
                .selectCategory(category)
                .enterTitle(title)
                .confirmDateAndTime()
                .enterDescription(description)
                .saveNews();
    }

    @Step("Очистить поле заголовка")
    public NewsEditScreen clearTitle() {
        EspressoUtils.waitForView(withId(R.id.news_title_edit_text));
        onView(withId(R.id.news_title_edit_text))
                .perform(androidx.test.espresso.action.ViewActions.clearText(),
                        closeSoftKeyboard());
        return this;
    }

    @Step("Очистить поле описания")
    public NewsEditScreen clearDescription() {
        EspressoUtils.waitForView(withId(R.id.news_description_edit_text));
        onView(withId(R.id.news_description_edit_text))
                .perform(androidx.test.espresso.action.ViewActions.clearText(),
                        closeSoftKeyboard());
        return this;
    }
}