package ru.edu.qamid.ui.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import io.qameta.allure.Step;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.utils.EspressoUtils;

public abstract class BaseScreen {

    @Step("Открыть экран управления новостями")
    public NewsListScreen openNewsManagement() {
        clickMenuItem("Новости");
        return new NewsListScreen();
    }

    @Step("Открыть экран «О приложении» из меню")
    public AboutScreen openAboutFromMenu() {
        clickMenuItem("О приложении");
        return new AboutScreen();
    }

    @Step("Открыть экран «Цитаты»")
    public MissionScreen openMission() {
        EspressoUtils.waitForView(withId(R.id.our_mission_image_button));
        onView(withId(R.id.our_mission_image_button)).perform(click());
        return new MissionScreen();
    }

    @Step("Выбрать пункт меню: {itemTitle}")
    protected void clickMenuItem(String itemTitle) {
        EspressoUtils.waitForView(allOf(withId(R.id.main_menu_image_button),
                withContentDescription("Главное меню")));
        onView(allOf(withId(R.id.main_menu_image_button),
                withContentDescription("Главное меню")))
                .perform(click());

        EspressoUtils.waitForView(allOf(withId(android.R.id.title), withText(itemTitle)));
        onView(allOf(withId(android.R.id.title), withText(itemTitle)))
                .perform(click());
    }
}
