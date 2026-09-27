package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
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
public class NewsEditTest extends BaseUiTest {

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
    @Description("Изменение Заголовка на валидное значение и проверка сохранения")
    @Severity(SeverityLevel.NORMAL)
    public void shouldEditNewsTitleAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);

        String title1 = TestDataGenerator.generateNewsTitle();
        String title2 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        NewsListScreen newsList = editScreen
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollToNewsItem(title1)
                .assertNewsItemVisible(title1);

        NewsListScreen updatedList = newsList
                .editItemByTitle(title1)
                .clearTitle()
                .enterTitle(title2)
                .saveNews();

        updatedList
                .pullToRefresh()
                .scrollToNewsItem(title2)
                .assertNewsItemVisible(title2)
                .assertNewsItemDoesNotExist(title1)
                .deleteItemByTitle(title2)
                .confirmDeleteDialog();
    }

    @Test
    @Description("Изменение Даты новости на валидное значение и проверка сохранения")
    @Severity(SeverityLevel.NORMAL)
    public void shouldEditNewsDateAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);
        LocalDate afterTomorrow = LocalDate.now().plusDays(6);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        NewsListScreen newsList = editScreen
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title1);
        newsList.assertNewsItemVisible(title1);

        NewsListScreen updatedList = newsList
                .editItemByTitle(title1)
                .setAndConfirmDate(afterTomorrow.getYear(), afterTomorrow.getMonthValue(), afterTomorrow.getDayOfMonth())
                .saveNews();

        updatedList.pullToRefresh()
                .scrollUntilNewsItemFound(title1)
                .assertNewsItemVisible(title1)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }

    @Test
    @Description("Изменение Времени новости на валидное значение и проверка сохранения")
    @Severity(SeverityLevel.NORMAL)
    public void shouldEditNewsTimeAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        NewsListScreen newsList = editScreen
                .selectCategory(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title1);
        newsList.assertNewsItemVisible(title1);

        NewsListScreen updatedList = newsList
                .editItemByTitle(title1)
                .confirmTime()
                .saveNews();

        updatedList.pullToRefresh()
                   .scrollUntilNewsItemFound(title1)
                   .assertNewsItemVisible(title1)
                   .deleteItemByTitle(title1)
                   .confirmDeleteDialog();
    }

    @Test
    @Description("Изменение Описания на валидное значение и проверка сохранения")
    @Severity(SeverityLevel.NORMAL)
    public void shouldEditNewsDescriptionAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String description2 = "Updated Test_" + UUID.randomUUID().toString();
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
        newsList.scrollUntilNewsItemFound(title1);
        newsList.assertNewsItemVisible(title1);

        newsList = newsList
                .editItemByTitle(title1)
                .clearDescription()
                .enterDescription(description2)
                .saveNews();

        newsList.pullToRefresh();

        newsList.clickNewsItemTitle(title1)
                .assertNewsDescriptionDisplayed(title1, description2)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }
}