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
import ru.edu.qamid.ui.screens.AboutScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsSortTest extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        newsScreen = loginAsDefaultUser();
    }

    @After
    public void tearDown() {
        try {
            pressBack();
            newsScreen.logout();
        } catch (Exception e) {
            try {
                newsScreen.logout();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @Description("Фильтр по диапазону, охватывающему обе новости")
    @Severity(SeverityLevel.NORMAL)
    public void shouldToggleNewsSortByDate() {

        LocalDate date1 = LocalDate.now().plusDays(8);
        LocalDate date2 = LocalDate.now().plusDays(9);


        LocalDate startDate = LocalDate.now().plusDays(8);
        LocalDate endDate = LocalDate.now().plusDays(9);

        String title1 = TestDataGenerator.generateNewsTitle();
        String title2 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String description2 = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();
        String randomCategory2 = NewsCategoryRandomizer.getRandomCategory();

        NewsListScreen newsList = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth())
                .setAndConfirmTime(23, 59)
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();

        newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(randomCategory2)
                .enterTitle(title2)
                .setAndConfirmDate(date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth())
                .setAndConfirmTime(0, 1)
                .enterDescription(description2)
                .saveNews();

        newsList.pullToRefresh()
                .openFilter()
                .setStartDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth())
                .setEndDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth())
                .clickFilterButton();

        newsList.pullToRefresh()
                .clickSortButton()
                .assertNewsItemPosition(0, title2)
                .clickSortButton()
                .assertNewsItemPosition(0, title1)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog()
                .deleteItemByTitle(title2)
                .confirmDeleteDialog();
    }

    @Test
    @Description("Фильтр, в диапазоне присутствует Новость, после сортировки также видна")
    @Severity(SeverityLevel.NORMAL)
    public void shouldSortWithSingleNewsItem() {
        LocalDate newsDate = LocalDate.now().plusDays(8);
        LocalDate filterStart = LocalDate.now().plusDays(7);
        LocalDate filterEnd = LocalDate.now().plusDays(9);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsListScreen newsList = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(newsDate.getYear(), newsDate.getMonthValue(), newsDate.getDayOfMonth())
                .setAndConfirmTime(12, 0)
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();

        newsList.openFilter()
                .setStartDate(filterStart.getYear(), filterStart.getMonthValue(), filterStart.getDayOfMonth())
                .setEndDate(filterEnd.getYear(), filterEnd.getMonthValue(), filterEnd.getDayOfMonth())
                .clickFilterButton();

        newsList.scrollToNewsItem(title1)
                .assertNewsItemVisible(title1);

        newsList.clickSortButton();
        newsList.scrollToNewsItem(title1)
                .assertNewsItemVisible(title1);

        newsList.clickSortButton();
        newsList.scrollToNewsItem(title1)
                .assertNewsItemVisible(title1)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }
}