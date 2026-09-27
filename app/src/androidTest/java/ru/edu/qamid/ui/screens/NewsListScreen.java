package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import static ru.edu.qamid.ui.utils.EspressoUtils.clickOnChildViewWithId;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.utils.EspressoUtils;

public class NewsListScreen extends BaseScreen {

    @Step("Проверить, что кнопка редактирования отображается")
    public void assertEditButtonDisplayed() {
        EspressoUtils.waitForView(allOf(withId(R.id.news_edit_button),
                withContentDescription("Кнопка редактирования новости")));
        onView(allOf(withId(R.id.news_edit_button),
                withContentDescription("Кнопка редактирования новости")))
                .check(matches(isDisplayed()));
    }

    @Step("Открыть режим редактирования новостей")
    public NewsEditScreen openEditMode() {
        EspressoUtils.waitForView(allOf(withId(R.id.news_edit_button),
                withContentDescription("Кнопка редактирования новости")));
        onView(allOf(withId(R.id.news_edit_button),
                withContentDescription("Кнопка редактирования новости")))
                .perform(click());
        return new NewsEditScreen();
    }

    @Step("Потянуть список вниз для обновления")
    public NewsListScreen pullToRefresh() {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        onView(withId(R.id.news_list_recycler_view)).perform(swipeDown());
        return this;
    }

    @Step("Проверить, что нужная новость отображается}")
    public NewsListScreen assertNewsItemVisible(String titleText) {
        EspressoUtils.waitForView(allOf(withId(R.id.news_item_title_text_view),
                withText(titleText)));
        onView(allOf(withId(R.id.news_item_title_text_view), withText(titleText)))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Проверить, что нужная новость отсутствует")
    public NewsListScreen assertNewsItemDoesNotExist(String titleText) {
        onView(allOf(withId(R.id.news_item_title_text_view), withText(titleText)))
                .check(doesNotExist());
        return this;
    }

    @Step("Нажать на заголовок нужной новости")
    public NewsListScreen clickNewsItemTitle(String titleText) {
        EspressoUtils.waitForView(allOf(withId(R.id.news_item_title_text_view),
                withText(titleText)));
        onView(allOf(withId(R.id.news_item_title_text_view), withText(titleText)))
                .perform(click());
        return this;
    }

    @Step("Проверить, что описание нужной новости отображается")
    public NewsListScreen assertNewsDescriptionDisplayed(String title, String description) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(title))));
        onView(allOf(
                withId(R.id.news_item_description_text_view),
                withText(description)))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Проверить, что описание новости скрыто")
    public NewsListScreen assertNewsDescriptionNotDisplayed(String description) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        try {
            onView(allOf(
                    withId(R.id.news_item_description_text_view),
                    withText(description)))
                    .check(matches(not(isDisplayed())));
        } catch (NoMatchingViewException e) {
            // view скрыт — тоже значит «не отображается»
        }
        return this;
    }

    @Step("Подтвердить диалог «Отмена» (OK)")
    public NewsListScreen confirmCancelDialog() {
        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());
        return this;
    }

    @Step("Подтвердить диалог удаления (OK)")
    public NewsListScreen confirmDeleteDialog() {
        EspressoUtils.waitForView(allOf(withId(android.R.id.button1), withText("OK")));
        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());
        return this;
    }

    @Step("Вернуться на главную страницу")
    public NewsScreen returnToMainPage() {
        clickMenuItem("Главная");
        return new NewsScreen();
    }

    @Step("Открыть экран фильтрации новостей")
    public NewsFilterScreen openFilter() {
        EspressoUtils.waitForView(withId(R.id.news_filter_button));
        onView(withId(R.id.news_filter_button)).perform(click());
        return new NewsFilterScreen();
    }

    @Step("Нажать кнопку сортировки")
    public NewsListScreen clickSortButton() {
        EspressoUtils.waitForView(withId(R.id.news_sort_button));
        onView(withId(R.id.news_sort_button)).perform(click());
        return this;
    }

    @Step("Открыть редактирование новости с нужным заголовком")
    public NewsEditScreen editItemByTitle(String title) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        onView(withId(R.id.news_list_recycler_view))
                .perform(actionOnItem(hasDescendant(withText(title)),
                        clickOnChildViewWithId(R.id.news_item_edit_image_view)));
        return new NewsEditScreen();
    }

    @Step("Проверить позицию новости: позиция, заголовок ")
    public NewsListScreen assertNewsItemPosition(int position, String titleText) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(position));
        onView(allOf(isDescendantOfA(withId(R.id.news_list_recycler_view)),
                withId(R.id.news_item_title_text_view), withText(titleText)))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Проверить, что новость с нужным заголовком не видна}")
    public NewsListScreen assertNewsItemDoesNotView(String titleText) {
        try {
            onView(allOf(withId(R.id.news_item_title_text_view), withText(titleText)))
                    .check(matches(not(isDisplayed())));
        } catch (NoMatchingViewException e) {
            //
        }
        return this;
    }

    @Step("Проверить статус новости с нужным заголовком")
    public NewsListScreen assertNewsItemStatus(String titleText, String status) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(titleText)), scrollTo()));
        onView(allOf(
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(titleText)))),
                withId(R.id.news_item_published_text_view),
                withText(status)))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Прокрутить к новости с нужным заголовком")
    public NewsListScreen scrollToNewsItem(String titleText) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(titleText))));
        return this;
    }

    @Step("Отменить диалог удаления (Отмена)")
    public NewsListScreen cancelDeleteDialog() {
        EspressoUtils.waitForView(allOf(withId(android.R.id.button2), withText("Отмена")));
        onView(allOf(withId(android.R.id.button2), withText("Отмена")))
                .perform(scrollTo(), click());
        return this;
    }

    @Step("Удалить новость с нужным заголовком")
    public NewsListScreen deleteItemByTitle(String title) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(title)), scrollTo()));
        onView(allOf(
                withId(R.id.news_item_delete_image_view),
                isDescendantOfA(allOf(
                        withId(R.id.news_item_material_card_view),
                        hasDescendant(withText(title))))))
                .perform(click());
        return this;
    }

    @Step("Скроллить до новости с нужным заголовком")
    public NewsListScreen scrollUntilNewsItemFound(String titleText) {
        EspressoUtils.waitForView(withId(R.id.news_list_recycler_view));
        for (int i = 0; i < 30; i++) {
            try {
                onView(withId(R.id.news_list_recycler_view))
                        .perform(RecyclerViewActions.scrollToPosition(i));
                onView(allOf(
                        withId(R.id.news_item_title_text_view),
                        withText(titleText)))
                        .check(matches(isDisplayed()));
                return this;
            } catch (Exception e) {
                //
            }
        }
        onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText(titleText)))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Нажать «Назад» для возврата на главный экран")
    public NewsScreen pressBack() {
        for (int i = 0; i < 3; i++) {
            try {
                androidx.test.espresso.Espresso.pressBack();
                EspressoUtils.waitForView(withId(R.id.all_news_text_view));
                return new NewsScreen();
            } catch (Exception e) {
                //
            }
        }
        androidx.test.espresso.Espresso.pressBack();
        EspressoUtils.waitForView(withId(R.id.all_news_text_view));
        return new NewsScreen();
    }
}
