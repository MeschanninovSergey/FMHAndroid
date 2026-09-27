package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.UUID;

import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsStatusTest extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        newsScreen = loginAsDefaultUser();
    }

    @After
    public void tearDown() {
        try {
            pressBack();
            pressBack();
            newsScreen.logout();
        } catch (Exception e) {
            try {
                pressBack();
                newsScreen.logout();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @Description("Перевод новости в статус Неактивна")
    @Severity(SeverityLevel.NORMAL)
    public void shouldDeactivateNews() {

        LocalDate tomorrow = LocalDate.now().plusDays(8);

        String title = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        NewsListScreen newsList = editScreen
                .selectCategory(randomCategory)
                .enterTitle(title)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title);
        newsList.assertNewsItemVisible(title);

        NewsListScreen updatedList = newsList
                .editItemByTitle(title)
                .toggleActiveSwitch("Активна")
                .saveNewsWithScroll();

        updatedList.assertNewsItemStatus(title, "НЕ АКТИВНА")
                .deleteItemByTitle(title)
                .confirmDeleteDialog();
    }

    @Test
    @Description("Перевод новости в статус Неактивна и перевод в статус Активна")
    @Severity(SeverityLevel.NORMAL)
    public void shouldDeactivateAndReactivateNews() {
        LocalDate tomorrow = LocalDate.now().plusDays(8);

        String title = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        NewsListScreen newsList = editScreen
                .selectCategory(randomCategory)
                .enterTitle(title)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title);
        newsList.assertNewsItemVisible(title);

        NewsListScreen deactivatedList = newsList
                .editItemByTitle(title)
                .toggleActiveSwitch("Активна")
                .saveNewsWithScroll();

        deactivatedList.assertNewsItemStatus(title, "НЕ АКТИВНА");

        NewsListScreen reactivatedList = deactivatedList.editItemByTitle(title)
                .toggleActiveSwitch("Не активна")
                .saveNewsWithScroll();

        reactivatedList.pullToRefresh();
        reactivatedList.scrollUntilNewsItemFound(title)
                .assertNewsItemVisible(title)
                .assertNewsItemStatus(title, "АКТИВНА")
                .deleteItemByTitle(title)
                .confirmDeleteDialog();
    }
}