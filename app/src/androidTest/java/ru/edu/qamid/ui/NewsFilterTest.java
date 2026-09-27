package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.UUID;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsFilterTest extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        newsScreen = loginAsDefaultUser();
    }


    @After
    public void tearDown() {
        try {
            newsScreen.logout();
            return;
        } catch (Exception ignored) {
        }
        for (int i = 0; i < 5; i++) {
            try {
                pressBack();
            } catch (Exception ignored) {
            }
            try {
                newsScreen.logout();
                return;
            } catch (Exception ignored) {
            }
        }
        try {
            newsScreen.logout();
        } catch (Exception ignored) {
        }
    }

    @Test
    @Description("Фильтр по дате: новость из будущего не видна после фильтра новостей с сегодняшней датой")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotDisplayFutureDateNewsAfterFilter() {

        LocalDate tomorrow = LocalDate.now().plusDays(7);
        LocalDate today = LocalDate.now();

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsListScreen newsList = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.assertNewsItemVisible(title1);

        newsList.openFilter()
                .setStartDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth())
                .setEndDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth())
                .clickFilterButton();

        newsList.assertNewsItemDoesNotExist(title1);
    }

    @Test
    @Description("Фильтр по дате: новость вне диапазона скрыта")
    @Severity(SeverityLevel.NORMAL)
    public void appFilterToDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dayAfterTomorrow = LocalDate.now().plusDays(3);

        String title1 = TestDataGenerator.generateNewsTitle();
        String category1 = NewsCategoryRandomizer.getRandomCategory();
        String description = "Test_" + UUID.randomUUID().toString();

        String title2 = TestDataGenerator.generateNewsTitle();
        String category2 = NewsCategoryRandomizer.getRandomCategory();
        String description2 = "Test_" + UUID.randomUUID().toString();

        newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(category1)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        NewsListScreen newsList = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(category2)
                .enterTitle(title2)
                .setAndConfirmDate(dayAfterTomorrow.getYear(), dayAfterTomorrow.getMonthValue(), dayAfterTomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description2)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title2);
        newsList.assertNewsItemVisible(title2);

        newsList.openFilter()
                .setStartDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .setEndDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.plusDays(1).getDayOfMonth())
                .clickFilterButton();

        newsList
                .assertNewsItemDoesNotView(title2)
                .scrollUntilNewsItemFound(title1)
                .assertNewsItemVisible(title1)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }

    @Test
    @Description("Фильтр по дате: новость в диапазоне фильтра видна")
    @Severity(SeverityLevel.NORMAL)
    public void shouldDisplayNewsWithinFilterRange() {
        LocalDate tomorrow = LocalDate.now().plusDays(8);
        LocalDate startDate = LocalDate.now().plusDays(7);      // сегодня
        LocalDate endDate = LocalDate.now().plusDays(9); // через 3 дня

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsListScreen newsList = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollToNewsItem(title1)
                .assertNewsItemVisible(title1);

        newsList.openFilter()
                .setStartDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth())
                .setEndDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth())
                .clickFilterButton();

        newsList.scrollToNewsItem(title1)
                .assertNewsItemVisible(title1)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }

    @Test
    @Description("Фильтр по дате: фильтр с одинаковыми датами начала и конца")
    @Severity(SeverityLevel.NORMAL)
    public void shouldDisplayNewsWithSameStartEndFilterDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(7);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsListScreen newsList = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollToNewsItem(title1)
                .assertNewsItemVisible(title1);

        newsList.openFilter()
                .setStartDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .setEndDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .clickFilterButton();

        newsList
                .scrollToNewsItem(title1)
                .assertNewsItemVisible(title1)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }
}